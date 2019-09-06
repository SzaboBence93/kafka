package com.nitrowise.basesrvr.exception.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class ErrorJson extends ExceptionJson {

    Map<String, Set<String>> attributeErrors;
    Set<String> globalErrors;

    public ErrorJson(String technikaiAzonosito) {
        super(technikaiAzonosito, "Hiba lépett fel a funkció igénybevétele során.");
    }

    public ErrorJson(String technikaiAzonosito, String status, String message, BindingResult errors) {
        super(technikaiAzonosito, StringUtils.isNotBlank(status) ? status : "Hiba lépett fel a funkció igénybevétele során.", message);
        if (errors != null && errors.hasErrors()) {
            if (!errors.getFieldErrors().isEmpty()) {
                this.attributeErrors = new HashMap();
                errors.getFieldErrors().forEach(fe -> {
                    if (!this.attributeErrors.containsKey(fe.getField())) {
                        this.attributeErrors.put(fe.getField(), new HashSet());
                    }

                    this.attributeErrors.get(fe.getField()).add(fe.getDefaultMessage());
                });
            }

            if (errors.getGlobalErrorCount() > 0) {
                this.globalErrors = new HashSet();
                errors.getGlobalErrors().stream().forEach(e -> this.globalErrors.add(e.getDefaultMessage()));
            }
        }

    }

    public ErrorJson(String technikaiAzonosito, String status, BindingResult errors) {
        this(technikaiAzonosito, status, (String)null, errors);
    }

    public ErrorJson(String technikaiAzonosito, BindingResult errors) {
        this(technikaiAzonosito, (String)null, (String)null, errors);
    }

    public void addAttributeError(String field, String error) {
        if (this.getAttributeErrors() == null) {
            this.attributeErrors = new HashMap();
        }

        if (!this.getAttributeErrors().containsKey(field)) {
            this.getAttributeErrors().put(field, new HashSet());
        }

        ((Set)this.getAttributeErrors().get(field)).add(error);
    }


}

