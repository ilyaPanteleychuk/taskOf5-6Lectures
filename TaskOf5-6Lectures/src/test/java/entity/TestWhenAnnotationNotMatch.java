package entity;

import ilya.profitsoft.task2.Property;


public class TestWhenAnnotationNotMatch {
    
    @Property(name = "nonExistingProperty")
    private String notMatchesField;
    
    public void setNotMatchesField(String notMatchesField) {
        this.notMatchesField = notMatchesField;
    }
}
