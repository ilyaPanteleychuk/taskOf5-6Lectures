package ilya.profitsoft.task2;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Properties;


public class PropertyMapper {
    
    public static <T> T loadFromProperties(Class<T> klass, Path propertiesPath) {
        Properties appProperties = new Properties();
        T outputObject;
        try (FileReader reader = new FileReader(propertiesPath.toFile())) {
            appProperties.load(reader);
            Constructor<T> constructor = klass.getConstructor();
            outputObject = constructor.newInstance();
            Field[] fields = klass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                // in case if annotation exists
                if (field.isAnnotationPresent(Property.class)) {
                    Property property = field.getAnnotation(Property.class);
                    if (appProperties.getProperty(property.name()) != null) {
                        Object fieldValue = appProperties.getProperty(property.name());
                        fieldSetter(field, outputObject, fieldValue, klass);
                    } else if (property.name().equals("N/A")
                            && appProperties.getProperty(field.getName()) != null) {
                        Object fieldValue = appProperties.getProperty(field.getName());
                        fieldSetter(field, outputObject, fieldValue, klass);
                    } else {
                        throw new IllegalArgumentException(
                                "Cannot initialize field "
                                + field.getName() + " of class " + klass.getName() +
                                " Cause - bad property mapping");
                    }
                    // in case if field name matches property key and there is no annotation
                } else if (appProperties.getProperty(field.getName()) != null) {
                    Object fieldValue = appProperties.getProperty(field.getName());
                    fieldSetter(field, outputObject, fieldValue, klass);
                } else {
                    throw new IllegalArgumentException("Cannot initialize field "
                            + field.getName() + " of class " + klass.getName() +
                            " Cause - neither annotation was found nor name matchers");
                }
            }
        } catch (IOException | NoSuchMethodException |
                 InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return outputObject;
    }
    
    //method to set already particular type of field
    private static <T> void fieldSetter(Field field, T object, Object fieldValue, Class<T> klass)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String setterName = field.getName();
        //capitalize setter name
        setterName = setterName.substring(0, 1).toUpperCase() + setterName.substring(1);
        Method setter = klass.getDeclaredMethod("set" + setterName, field.getType());
        switch (field.getType().getSimpleName()) {
            case ("String") -> setter.invoke(object, fieldValue);
            case ("int"), ("Integer") ->
                    setter.invoke(object, Integer.valueOf((String) fieldValue));
            case ("Instant") -> {
                Property annotation = field.getAnnotation(Property.class);
                if (annotation.format().equals("N/A")) {
                    throw new IllegalArgumentException("Cannot initialize field "
                            + field.getName() + " of class " + klass.getName() +
                            " Cause - field " + field.getName() + "has no any date format");
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(annotation.format())
                        .withZone(ZoneId.systemDefault());
                setter.invoke(object, formatter.parse((String) fieldValue, Instant::from));
            }
            default ->
                    throw new IllegalArgumentException("Unsupported type " + field.getType());
        }
    }
}
