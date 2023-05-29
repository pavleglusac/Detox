package com.sbnz.detox.model;

public enum UrineTestResult {
    NOT_TESTED,
    PRESENCE_11_HYDROXY_9_TETRAHYDROCANNABINOL,
    PRESENCE_BENZOILECGONINE,
    PRESENCE_AMPHETAMINE;

    // get integer value of enum
    public int getValue() {
        return this.ordinal();
    }

}
