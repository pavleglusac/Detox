package com.sbnz.detox.model;


import lombok.Data;

@Data
public abstract class DiagnosisResponse {

    String responseType;

    public abstract String getResponseType();

}
