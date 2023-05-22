package com.sbnz.detox.model.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.definition.type.Position;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryModel {
    @Position(0)
    String podvrsta;
    @Position(1)
    String vrsta;
    @Position(2)
    String vrednost;
}
