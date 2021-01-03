package com.example.prepaiddata.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (methodKey.contains("getAccountByPhoneNumber")) {
            return new CannotGetAccountException("Cannot get account");
        }

        return new FeignException("Cannot get response from other services");
    }
}
