package com.paddavoet.bittradr.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
public class UserEntity {
    private String email;
    private String bitfinexApiKey;
    private String bitfinexApiSecret;

    public UserEntity() {
    }

    public UserEntity(String email, String bitfinexApiKey, String bitfinexApiSecret) {
        this.email = email;
        this.bitfinexApiKey = bitfinexApiKey;
        this.bitfinexApiSecret = bitfinexApiSecret;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBitfinexApiKey() {
        return bitfinexApiKey;
    }

    public void setBitfinexApiKey(String bitfinexApiKey) {
        this.bitfinexApiKey = bitfinexApiKey;
    }

    public String getBitfinexApiSecret() {
        return bitfinexApiSecret;
    }

    public void setBitfinexApiSecret(String bitfinexApiSecret) {
        this.bitfinexApiSecret = bitfinexApiSecret;
    }
}
