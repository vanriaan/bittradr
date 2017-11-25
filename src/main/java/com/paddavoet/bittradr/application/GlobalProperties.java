package com.paddavoet.bittradr.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalProperties {
    public static String BIT_FIN_EX_API_KEY;
    public static String BIT_FIN_EX_API_SECRET;

    @Value("${api.bitfinex.apikey}")
    public void setBitFinExApiKey(String bitFinExApiKey) {
        BIT_FIN_EX_API_KEY = bitFinExApiKey;
    }

    @Value("${api.bitfinex.apisecret}")
    public void setBitFinExApiSecret(String bitFinExApiSecret) {
        BIT_FIN_EX_API_SECRET = bitFinExApiSecret;
    }
}
