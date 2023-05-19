package com.sbnz.detox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private Long diagnosisId;
    private String content;
    private String type;
    private Boolean answered = false;
    private HashMap<String, String> answers;

    public Question(String content, String type) {
        this.content = content;
        this.type = type;
    }
}
