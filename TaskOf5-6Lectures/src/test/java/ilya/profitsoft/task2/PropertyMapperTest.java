package ilya.profitsoft.task2;

import entity.*;
import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class PropertyMapperTest {
    
    private static final String PROPERTY_PATH = "src/test/resources/application.properties";
    
    @Test
    void loadFromProperties_shouldSetValue_whenFieldNameMatchesPropKey() {
        TestFieldMatchers testFieldMatchers = PropertyMapper
                .loadFromProperties(TestFieldMatchers.class, Path.of(PROPERTY_PATH));
        assertEquals("value1", testFieldMatchers.getToCheckNameMapping());
    }
    
    @Test
    void loadFromProperties_shouldSetValue_whenAnnotationNameMatchesPropKey() {
        TestFieldMatchers testFieldMatchers = PropertyMapper
                .loadFromProperties(TestFieldMatchers.class, Path.of(PROPERTY_PATH));
        assertEquals(10, testFieldMatchers.getToCheckAnnotationMapping());
    }
    
    @Test
    void loadFromProperties_shouldSetInstantValueByPattern_whenFormatAnnotationExist() {
        TestFieldMatchers testFieldMatchers = PropertyMapper
                .loadFromProperties(TestFieldMatchers.class, Path.of(PROPERTY_PATH));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                .withZone(ZoneId.systemDefault());
        assertEquals(formatter.parse("29.11.2022 18:30", Instant::from),
                testFieldMatchers.getFormatMatches());
    }
    
    @Test
    void loadFromProperties_shouldThrowIllegalArgumentException_whenAnnotationDoesNotMatchPropKey() {
        String expectedMessage =
                "Cannot initialize field notMatchesField of class entity." +
                "TestWhenAnnotationNotMatch Cause - bad property mapping";
        Path propPath = Path.of(PROPERTY_PATH);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                PropertyMapper.loadFromProperties
                        (TestWhenAnnotationNotMatch.class, propPath));
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    void loadFromProperties_shouldThrowIllegalArgumentException_whenHasNoAnnotationAndFieldNameNotMatchPropKey() {
        String expectedMessage =
                "Cannot initialize field nonMatchingField of class entity." +
                "TestWhenHasNoAnnotationAndFieldNotMatch " +
                "Cause - neither annotation was found nor name matchers";
        Path propPath = Path.of(PROPERTY_PATH);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                PropertyMapper.loadFromProperties
                        (TestWhenHasNoAnnotationAndFieldNotMatch.class, propPath));
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    void loadFromProperties_shouldThrowIllegalArgumentException_whenInstantFieldHasNoDateFormat() {
        String expectedMessage = "Cannot initialize field instantWithoutFormat " +
                "of class entity.TestWhenInstantFieldHasNoDateFormat " +
                "Cause - field instantWithoutFormathas no any date format";
        Path propPath = Path.of(PROPERTY_PATH);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                PropertyMapper.loadFromProperties
                        (TestWhenInstantFieldHasNoDateFormat.class, propPath));
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    void loadFromProperties_shouldThrowNoSuchMethodException_whenFieldHasNoSetter() {
        Path propPath = Path.of(PROPERTY_PATH);
        assertThrows(IllegalArgumentException.class, () ->
                PropertyMapper.loadFromProperties
                        (TestWhenHasNoSetter.class, propPath));
    }
}
