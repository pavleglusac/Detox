package com.sbnz.detox.model.gas_chromatography_industry;

import java.util.Date;

public interface IndustryToxicSample {

    public void setConcentration(double concentration);
    public Double getConcentration();
    void setTimestamp(Date date);

}
