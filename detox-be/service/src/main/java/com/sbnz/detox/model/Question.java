package com.sbnz.detox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question extends DiagnosisResponse {
    private Long diagnosisId;
    private String content;
    private String type;
    private Boolean answered = false;
    private String target;
    private List<String> answers;

    public Question(String content, String type, String target, List<String> answers) {
        this.content = content;
        this.type = type;
        this.target = target;
        this.answers = answers;
    }

    @Override
    String getType() {
        return "QUESTION";
    }
}
