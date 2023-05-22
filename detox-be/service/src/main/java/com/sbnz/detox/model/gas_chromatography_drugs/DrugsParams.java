package com.sbnz.detox.model.gas_chromatography_drugs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrugsParams {

    private String paramsName;
    private Double cocaineConcentrationMin;
    private Double cocaineConcentrationMax;
    private Integer cocaineTime;
    private Double opioidConcentrationMin;
    private Double opioidConcentrationMax;
    private Integer opioidTime;
    private Double methamphetamineConcentrationMin;
    private Double methamphetamineConcentrationMax;
    private Integer methamphetamineTime;
    private Double benzodiazepineConcentrationMin;
    private Double benzodiazepineConcentrationMax;
    private Integer benzodiazepineTime;
    private Double syntheticCannabinoidConcentrationMin;
    private Double syntheticCannabinoidConcentrationMax;
    private Integer syntheticCannabinoidTime;
}
