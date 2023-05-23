package com.sbnz.detox.model.spectrophotometry;

import java.util.Date;

public interface SpectrophotometrySample {
    public void setAbsorbance(double concentration);
    public Double getAbsorbance();
    public void setWavelength(double concentration);
    public Double getWavelength();
    void setAmount(int num);
    int getAmount();
}
