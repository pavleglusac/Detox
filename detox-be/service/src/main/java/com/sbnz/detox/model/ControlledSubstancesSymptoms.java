package com.sbnz.detox.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class ControlledSubstancesSymptoms extends Symptoms {

    ChromatographicImunoassayTestResult chromatographicImunoassayTest = ChromatographicImunoassayTestResult.NOT_TESTED;

    Boolean LocksonTest = null;

    Boolean TLCAcidicTest = null;

    Boolean Convulsions = null;

    UrineTestResult urineTest = UrineTestResult.NOT_TESTED;

    Boolean ReactsToKVS = null;

    Boolean TLCBasicTest = null;

    Boolean AcidityTest = null;

    Boolean AffectsCNS = null;

    Boolean InDeepComa = null;

    Boolean HasLungFibrosis = null;

    Boolean HasARDS = null;

}
