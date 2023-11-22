package dev.coauth.module.totp.service;

import dev.coauth.module.messaging.MessageRegisterStatusDto;
import dev.coauth.module.messaging.MessageVerificationStatusDto;
import dev.coauth.module.totp.cache.TotpRegisterCacheDto;
import dev.coauth.module.totp.cache.TotpVerificationCacheDto;
import dev.coauth.module.totp.dto.*;
import dev.coauth.module.totp.entity.TotpMstrEntity;
import dev.coauth.module.totp.exception.NonFatalException;
import dev.coauth.module.totp.producer.MessageBrokerService;
import dev.coauth.module.totp.repository.TotpRepository;
import dev.coauth.module.totp.utils.ApplicationConstants;
import dev.coauth.module.totp.utils.CryptoAlgoUtil;
import dev.coauth.module.totp.utils.DateTimeUtils;
import io.quarkus.infinispan.client.Remote;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.infinispan.client.hotrod.RemoteCache;

@ApplicationScoped
public class TotpService {

    @Inject
    TotpRepository totpRepository;

    @Inject
    TotpUtilService totpUtilService;

    @Inject
    @Remote("totp_register")
    RemoteCache<String, TotpRegisterCacheDto> totpRegisterCache;

    @Inject
    @Remote("totp_verify")
    RemoteCache<String, TotpVerificationCacheDto> totpVerifyCache;

    @ConfigProperty(name = "totp.attempt-expiry-time-minutes", defaultValue = "15")
    int attemptExpiryTimeMinutes;

    @Inject
    MessageBrokerService brokerService;

    public Uni<String> generateSecret(RegisterGenerateRequestDto registerGenerateRequestDto) {
        return getDetails(registerGenerateRequestDto.appId, registerGenerateRequestDto.userId)
                .onItem().transformToUni(entity -> {
                    if (entity == null) {
                        // Generate UUID asynchronously
                        return Uni.createFrom().item(registerGenerateRequestDto.getCode())
                                .onItem().transformToUni(uuid -> {
                                    // Generate secret asynchronously
                                    return totpUtilService.generateSecretKey()
                                            .onItem()
                                            .invoke(secret -> {
                                                TotpRegisterCacheDto totpRegisterCacheDto = new TotpRegisterCacheDto(uuid, registerGenerateRequestDto.getAppId(), registerGenerateRequestDto.getUserId(), secret, registerGenerateRequestDto.getCodeChallenge());
                                                totpRegisterCache.putAsync(uuid, totpRegisterCacheDto);
                                            }).onItem()
                                            .transformToUni(secret -> {
                                                return Uni.createFrom().item(uuid);
                                            });
                                });
                    } else {
                        return Uni.createFrom().failure(new NonFatalException(1000, "Record already exists"));
                    }
                });
    }


    public Uni<TotpMstrEntity> getDetails(int appId, String userId) {
        TotpMstrEntity totpMstrEntity = new TotpMstrEntity();
        totpMstrEntity.setAppId(appId);
        totpMstrEntity.setUserId(userId);
        return totpRepository.get(totpMstrEntity);
    }


    public Uni<String> generateQR(RegisterViewRequestDto registerViewRequestDto) {
        return Uni.createFrom().item(totpRegisterCache.get(registerViewRequestDto.getAppCode()))
                .onItem().transformToUni(totpRegisterCacheDto -> {
                    if (totpRegisterCacheDto == null) {
                        return Uni.createFrom().failure(new NonFatalException(1000, "Record not found"));
                    } else {
                        return totpUtilService.generateQRCode(
                                totpRegisterCacheDto.getSecret(),
                                totpRegisterCacheDto.getUserId(),
                                "Co-Auth");
                    }
                });
    }

    public Uni<String> save(RegisterSaveRequestDto registerSaveRequestDto) {
        return Uni.createFrom().item(() -> totpRegisterCache.get(registerSaveRequestDto.getAppCode()))
                .onItem().transformToUni(totpRegisterCacheDto -> {
                    if (totpRegisterCacheDto == null) {
                        return Uni.createFrom().failure(new NonFatalException(1000, "Record not found"));
                    } else {
                        return totpUtilService.verify(registerSaveRequestDto.getTotpCode(), totpRegisterCacheDto.getSecret())
                                .onItem().transformToUni(isValid -> {
                                    if (isValid) {
                                        TotpMstrEntity totpMstrEntity = createRegisterCacheObject(totpRegisterCacheDto);
                                        return totpRepository.save(totpMstrEntity)
                                                .onItem().transformToUni(entity -> {
                                                    MessageRegisterStatusDto messageRegisterStatusDto = new MessageRegisterStatusDto();
                                                    messageRegisterStatusDto.setAppId(totpRegisterCacheDto.getAppId());
                                                    messageRegisterStatusDto.setCode(totpRegisterCacheDto.getCode());
                                                    messageRegisterStatusDto.setUserId(totpRegisterCacheDto.getUserId());
                                                    messageRegisterStatusDto.setStatus(ApplicationConstants.STATUS_SUCCESS);

                                                    return brokerService.emitTotpRegisterStatus(messageRegisterStatusDto)
                                                            .onItem()

                                                            .transformToUni(entity2 -> {
                                                                // Use removeAsync and map to the original code
                                                                return Uni.createFrom().item(totpRegisterCacheDto.getCode());
                                                            }).invoke(entity1 -> {
                                                                totpRegisterCache.removeAsync(totpRegisterCacheDto.getCode());
                                                            });
                                                });
                                    } else {
                                        return Uni.createFrom().failure(new NonFatalException(1003, "Invalid TOTP"));
                                    }
                                });
                    }
                });
    }


    private static TotpMstrEntity createRegisterCacheObject(TotpRegisterCacheDto totpRegisterCacheDto) {
        TotpMstrEntity totpMstrEntity = new TotpMstrEntity();
        totpMstrEntity.setRowId(totpRegisterCacheDto.getCode());
        totpMstrEntity.setAppId(totpRegisterCacheDto.getAppId());
        totpMstrEntity.setUserId(totpRegisterCacheDto.getUserId());
        totpMstrEntity.setAppId(totpRegisterCacheDto.getAppId());
        totpMstrEntity.setUserId(totpRegisterCacheDto.getUserId());
        totpMstrEntity.setSecret(totpRegisterCacheDto.getSecret());
        return totpMstrEntity;
    }

    public Uni<String> verify(RegisterVerifyRequestDto registerVerifyRequestDto) {
        return Uni.createFrom().item(totpRegisterCache.get(registerVerifyRequestDto.getAppCode()))
                .onItem().transformToUni(totpRegisterCacheDto -> {
                    if (totpRegisterCacheDto == null) {
                        return Uni.createFrom().failure(new NonFatalException(1000, "Record not found in cache"));
                    } else {
                        return CryptoAlgoUtil.calculateSHA256(registerVerifyRequestDto.getCodeVerifier())
                                .onItem().transformToUni(sha256 -> {
                                    if (sha256.equals(totpRegisterCacheDto.getCodeChallenge())) {
                                        TotpMstrEntity totpMstrEntity = new TotpMstrEntity();
                                        totpMstrEntity.setAppId(totpRegisterCacheDto.getAppId());
                                        totpMstrEntity.setUserId(totpRegisterCacheDto.getUserId());
                                        return totpRepository.get(totpMstrEntity)
                                                .onItem()
                                                .invoke(entity ->
                                                        totpRegisterCache.remove(totpRegisterCacheDto.getCode())
                                                )
                                                .onItem().transformToUni(entity -> {
                                                    if (entity != null) {
                                                        return Uni.createFrom().item(totpRegisterCacheDto.getCode());
                                                    } else {
                                                        return Uni.createFrom().failure(new NonFatalException(1003, "TotpMstrEntity not found"));
                                                    }
                                                })
                                                ;
                                    } else {
                                        return Uni.createFrom().failure(new NonFatalException(1002, "Invalid code verifier"));
                                    }
                                });
                    }
                });
    }


    public Uni<String> generateAuthVerification(VerificationGenerateRequestDto verificationGenerateRequestDto) {
        return getDetails(verificationGenerateRequestDto.getAppId(), verificationGenerateRequestDto.getUserId())
                .onItem().transformToUni(entity -> {
                    if (entity == null) {
                        return Uni.createFrom().failure(new NonFatalException(1000, "Record does not exists"));
                    } else {
                        return Uni.createFrom().item(verificationGenerateRequestDto.code)
                                .onItem()
                                .invoke(uuid -> {
                                    TotpVerificationCacheDto totpVerificationCacheDto = new TotpVerificationCacheDto(uuid, verificationGenerateRequestDto.getAppId(), verificationGenerateRequestDto.getUserId(), 0, DateTimeUtils.addMinutes(DateTimeUtils.getCurrentDate(), attemptExpiryTimeMinutes), ApplicationConstants.STATUS_PENDING, verificationGenerateRequestDto.getCodeChallenge());
                                    totpVerifyCache.putAsync(uuid, totpVerificationCacheDto);
                                })
                                .onItem().transformToUni(uuid -> {
                                    return Uni.createFrom().item(uuid);
                                });
                    }
                });
    }

    public Uni<String> viewAuthVerification(VerificationViewRequestDto verificationViewRequestDto) {
        return Uni.createFrom().item(totpVerifyCache.get(verificationViewRequestDto.getAppCode()))
                .onItem().transformToUni(totpVerificationCacheDto -> {
                    if (totpVerificationCacheDto == null) {
                        return Uni.createFrom().failure(new NonFatalException(1000, "Record not found"));
                    } else {
                        return Uni.createFrom().item(verificationViewRequestDto.getAppCode());
                    }
                });
    }

    public Uni<String> authenticateVerification(VerificationAuthenticateRequestDto verificationAuthenticateRequestDto) {
        return Uni.createFrom().item(totpVerifyCache.get(verificationAuthenticateRequestDto.getAppCode()))
                .onItem().transformToUni(totpVerificationCacheDto -> {
                    if (totpVerificationCacheDto == null) {
                        return Uni.createFrom().failure(new NonFatalException(1000, "Record not found"));
                    } else {
                        return getDetails(totpVerificationCacheDto.getAppId(), totpVerificationCacheDto.getUserId())
                                .onItem().transformToUni(entity -> {
                                    if (entity == null) {
                                        return Uni.createFrom().failure(new NonFatalException(1000, "Record does not exists"));
                                    } else {
                                        return totpUtilService.verify(
                                                verificationAuthenticateRequestDto.getTotpCode(),
                                                entity.getSecret()).onItem().transformToUni(isValid -> {
                                            if (isValid) {
                                                totpVerificationCacheDto.setNoOfAttempts(0);
                                                totpVerificationCacheDto.setStatus(ApplicationConstants.STATUS_SUCCESS);
                                                totpVerifyCache.putAsync(totpVerificationCacheDto.getCode(), totpVerificationCacheDto);
                                                return Uni.createFrom().item(totpVerificationCacheDto.getCode()).onItem()
                                                        .transformToUni(entity1 -> {
                                                            MessageVerificationStatusDto messageVerificationStatusDto = new MessageVerificationStatusDto();
                                                            messageVerificationStatusDto.setAppId(totpVerificationCacheDto.getAppId());
                                                            messageVerificationStatusDto.setCode(totpVerificationCacheDto.getCode());
                                                            messageVerificationStatusDto.setUserId(totpVerificationCacheDto.getUserId());
                                                            messageVerificationStatusDto.setStatus(ApplicationConstants.STATUS_SUCCESS);
                                                            return brokerService.emitTotpVerifyStatus(messageVerificationStatusDto).onItem()
                                                                    .transform(entity2 -> totpVerificationCacheDto.getCode());
                                                        })
                                                        .invoke(unUsed ->
                                                                totpVerifyCache.remove(totpVerificationCacheDto.getCode())
                                                        );
                                            } else {
                                                totpVerificationCacheDto.setNoOfAttempts(totpVerificationCacheDto.getNoOfAttempts() + 1);
                                                totpVerifyCache.replace(totpVerificationCacheDto.getCode(), totpVerificationCacheDto);
                                                return Uni.createFrom().failure(new NonFatalException(1003, "Invalid OTP"));
                                            }
                                        });

                                    }

                                });
                    }
                });

    }

/*
    public Uni<String> validateVerification(VerificationValidateRequestDto verificationValidateRequestDto) {
        return Uni.createFrom().item(totpVerifyCache.get(verificationValidateRequestDto.getAppCode()))
                .onItem().transformToUni(totpVerificationCacheDto -> {
                    if (totpVerificationCacheDto == null) {
                        return Uni.createFrom().failure(new NonFatalException(1000, "Record not found in cache"));
                    } else {
                        return CryptoAlgoUtil.calculateSHA256(verificationValidateRequestDto.getCodeVerifier())
                                .onItem().transformToUni(sha256 -> {
                                    if (sha256.equals(totpVerificationCacheDto.getCodeChallenge())) {
                                        if (!totpVerificationCacheDto.getStatus().equals(ApplicationConstants.STATUS_SUCCESS)) {
                                            return Uni.createFrom().failure(new NonFatalException(1002, "Verification not completed"));
                                        } else {
                                            return Uni.createFrom().item(totpVerificationCacheDto.getCode()).onItem()
                                                    .invoke(entity ->
                                                            totpVerifyCache.remove(totpVerificationCacheDto.getCode())
                                                    );
                                        }
                                    } else {
                                        return Uni.createFrom().failure(new NonFatalException(1002, "Invalid code verifier"));
                                    }
                                });
                    }
                });
    }*/
}
