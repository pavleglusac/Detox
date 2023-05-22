package com.sbnz.detox.model.gas_chromatography_industry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Role(Role.Type.EVENT)
@Timestamp("timestamp")
public class Toluene implements IndustryToxicSample {
    private Long id;
    private Date timestamp;
    private Double concentration;

    @Override
    public void setConcentration(double concentration) {
        this.concentration = concentration;
    }

    @Override
    public Double getConcentration() {
        return this.concentration;
    }

}