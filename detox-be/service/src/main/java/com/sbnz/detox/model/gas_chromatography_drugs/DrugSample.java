package com.sbnz.detox.model.gas_chromatography_drugs;

import java.util.Date;

public interface DrugSample {
    public void setConcentration(double concentration);
    public Double getConcentration();
    void setTimestamp(Date date);
}
