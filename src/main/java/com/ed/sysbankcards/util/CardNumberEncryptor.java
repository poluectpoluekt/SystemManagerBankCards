package com.ed.sysbankcards.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Converter
@RequiredArgsConstructor
@Component
public class CardNumberEncryptor implements AttributeConverter<String, String> {

    @Value("${app.security}")
    private String encryptionKey;

    private static final String ALGORITHM = "AES";

    @Override
    public String convertToDatabaseColumn(String cardNumber) {
        try {
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(cardNumber.getBytes()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new IllegalStateException("Failed to encrypt card number", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbCardNumber) {
        try {
            SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbCardNumber)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new IllegalStateException("Failed to encrypt card number", e);
        }
    }
}
