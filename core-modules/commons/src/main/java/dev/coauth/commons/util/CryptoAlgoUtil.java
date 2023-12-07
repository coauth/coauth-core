package dev.coauth.commons.util;

import io.smallrye.mutiny.Uni;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoAlgoUtil {

    public static Uni<String> calculateSHA256(String input)  {
        return Uni.createFrom().deferred(() -> {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

                // Convert byte array to a hexadecimal string
                StringBuilder hexString = new StringBuilder();
                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        hexString.append('0');
                    }
                    hexString.append(hex);
                }
                return Uni.createFrom().item(hexString::toString);
            } catch (NoSuchAlgorithmException e) {
                return Uni.createFrom().failure(e);
            }
        });

    }
}
