package entity;

import ilya.profitsoft.task2.Property;

import java.time.Instant;


public class TestWhenInstantFieldHasNoDateFormat {
    
    @Property(name = "formatMatches")
    private Instant instantWithoutFormat;
    
    public void setInstantWithoutFormat(Instant instantWithoutFormat) {
        this.instantWithoutFormat = instantWithoutFormat;
    }
}
