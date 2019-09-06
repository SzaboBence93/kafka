package com.nitrowise.basesrvr.exception.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExceptionJson {

    String technikaiAzonosito;
    String status;
    String message;
    String developerMessage;

    public ExceptionJson(String technikaiAzonosito, String status, String message) {
        this.technikaiAzonosito = technikaiAzonosito;
        this.status = status;
        this.message = message;
    }

    public ExceptionJson(String technikaiAzonosito, String message) {
        this.technikaiAzonosito = technikaiAzonosito;
        this.message = message;
    }

}
