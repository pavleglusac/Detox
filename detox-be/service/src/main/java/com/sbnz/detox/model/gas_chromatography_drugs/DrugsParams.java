package com.sbnz.detox.model.gas_chromatography_drugs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrugsParams {

    private Double cocaineConcentration;
    private Integer cocaineTime;
    private Double opioidConcentration;
    private Integer opioidTime;
    private Double methamphetamineConcentration;
    private Integer methamphetamineTime;
    private Double benzodiazepineConcentration;
    private Integer benzodiazepineTime;
    private Double syntheticCannabinoidConcentration;
    private Integer syntheticCannabinoidTime;
}
