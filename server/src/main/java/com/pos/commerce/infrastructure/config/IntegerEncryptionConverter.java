package com.pos.commerce.infrastructure.config;

import com.pos.commerce.util.EncryptionUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Converter
@Component
public class IntegerEncryptionConverter implements AttributeConverter<Integer, String>, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        IntegerEncryptionConverter.applicationContext = applicationContext;
    }

    private EncryptionConfig getEncryptionConfig() {
        return applicationContext.getBean(EncryptionConfig.class);
    }

    @Override
    public String convertToDatabaseColumn(Integer attribute) {
        if (attribute == null) {
            return null;
        }
        return EncryptionUtil.encrypt(attribute.toString(), getEncryptionConfig().getSecretKey());
    }

    @Override
    public Integer convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        String decrypted = EncryptionUtil.decrypt(dbData, getEncryptionConfig().getSecretKey());
        return Integer.parseInt(decrypted);
    }
}

