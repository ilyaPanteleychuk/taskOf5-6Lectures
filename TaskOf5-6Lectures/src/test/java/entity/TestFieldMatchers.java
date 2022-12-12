package entity;

import ilya.profitsoft.task2.Property;
import java.time.Instant;


public class TestFieldMatchers {
    
    private String toCheckNameMapping;
    
    @Property(name = "annotationMatches")
    private Integer toCheckAnnotationMapping;
    
    @Property(format = "dd.MM.yyyy HH:mm")
    private Instant formatMatches;
    
    public void setToCheckNameMapping(String toCheckNameMapping) {
        this.toCheckNameMapping = toCheckNameMapping;
    }
    
    public void setToCheckAnnotationMapping(Integer toCheckAnnotationMapping) {
        this.toCheckAnnotationMapping = toCheckAnnotationMapping;
    }
    
    public void setFormatMatches(Instant formatMatches) {
        this.formatMatches = formatMatches;
    }
    
    public String getToCheckNameMapping() {
        return toCheckNameMapping;
    }
    
    public Integer getToCheckAnnotationMapping() {
        return toCheckAnnotationMapping;
    }
    
    public Instant getFormatMatches() {
        return formatMatches;
    }
}
