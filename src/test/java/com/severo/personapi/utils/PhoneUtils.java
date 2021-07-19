package com.severo.personapi.utils;

import com.severo.personapi.dto.request.PhoneDTO;
import com.severo.personapi.entity.Phone;
import com.severo.personapi.enums.PhoneType;

public class PhoneUtils {

    private static final String PHONE_NUMBER = "(11)999999999";
    private static final PhoneType PHONE_TYPE = PhoneType.MOBILE;
    private static final long PHONE_ID = 1L;

    public static PhoneDTO createFakeDTO() {
        return PhoneDTO.builder()
                .number(PHONE_NUMBER)
                .type(PHONE_TYPE)
                .build();
    }

    public static Phone createFakeEntity() {
        return Phone.builder()
                .id(PHONE_ID)
                .number(PHONE_NUMBER)
                .type(PHONE_TYPE)
                .build();
    }
}
