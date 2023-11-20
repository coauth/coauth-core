package dev.coauth.module.totp.service;

import dev.coauth.module.totp.exception.NonFatalException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.codec.binary.Base32;

import java.security.SecureRandom;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class TotpUtilService {
    @ConfigProperty(name="totp.time-period",defaultValue = "30")
    int allowedTimePeriod;

    @ConfigProperty(name="totp.allowed-discrepancy",defaultValue = "2")
    int allowedDiscrepancy;
    @ConfigProperty(name="totp.digit-length",defaultValue = "6")
    int digitLength;

    public Uni<String> generateSecretKey() {
        return Uni.createFrom().item(() -> {
            SecureRandom random = new SecureRandom();
            byte[] bytes = new byte[20];
            random.nextBytes(bytes);
            Base32 base32 = new Base32();
            return base32.encodeAsString(bytes);
        });
    }

    public Uni<Boolean> verify(String plainTextOtp, String totpSecret) {
        return Uni.createFrom().item(() -> {
            TimeProvider timeProvider = new SystemTimeProvider();
            CodeGenerator codeGenerator = new DefaultCodeGenerator();
            DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

            verifier.setTimePeriod(allowedTimePeriod);
            verifier.setAllowedTimePeriodDiscrepancy(allowedDiscrepancy);

            return verifier.isValidCode(totpSecret, plainTextOtp);
        });
    }

    public Uni<String> generateQRCode(String totpSecret, String email, String appName) {
        return Uni.createFrom().emitter(emitter -> {
            try {
                QrData data =
                        new QrData.Builder()
                                .label(email)
                                .secret(totpSecret)
                                .issuer(appName)
                                .algorithm(HashingAlgorithm.SHA1)
                                .digits(digitLength)
                                .period(allowedTimePeriod)
                                .build();

                QrGenerator generator = new ZxingPngQrGenerator();
                byte[] imageData = generator.generate(data);
                String mimeType = generator.getImageMimeType();
                emitter.complete(Utils.getDataUriForImage(imageData, mimeType));
            } catch (QrGenerationException e) {
                emitter.fail(new NonFatalException(1001,"Error generating QR code", e));
            }
        });
    }

}
