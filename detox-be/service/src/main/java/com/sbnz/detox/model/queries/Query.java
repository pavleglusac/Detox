package com.sbnz.detox.model.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Query {

    private String polje;
    private String vrednost;
    private String target;
    private Boolean result = false;
}
