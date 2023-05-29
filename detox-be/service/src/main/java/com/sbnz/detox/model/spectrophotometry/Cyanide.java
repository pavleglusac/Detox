package com.sbnz.detox.model.spectrophotometry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.definition.type.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Role(Role.Type.EVENT)
public class Cyanide implements SpectrophotometrySample{
    private Long id;
    private double absorbance;
    private double wavelength;
    private int amount;
    @Override
    public void setAbsorbance(double concentration) {
        this.absorbance = concentration;
    }

    @Override
    public Double getAbsorbance() {
        return this.absorbance;
    }

    @Override
    public void setWavelength(double concentration) {
        this.wavelength = concentration;
    }

    @Override
    public Double getWavelength() {
        return this.wavelength;
    }

    @Override
    public void setAmount(int num) {
        this.amount = num;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }
}
