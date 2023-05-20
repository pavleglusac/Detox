package com.sbnz.detox.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class IndustrySymptoms extends Symptoms {
    Boolean worksWithToxicGases = null;

    Boolean showsMethemoglobinemia = null;

    DamageToTheRespiratoryTract damageToTheRespiratoryTract = DamageToTheRespiratoryTract.NONE;

    Boolean epaTest = null;

    Boolean pulmonaryEdema = null;

    Boolean ormTest = null;

    Boolean ffpTest = null;

    Boolean giTest = null;

    Boolean amilNitriteTest = null;

    Boolean cns = null;

    Boolean potassiumDichromateTest = null;

    Boolean neurotoxicity = null;
}
