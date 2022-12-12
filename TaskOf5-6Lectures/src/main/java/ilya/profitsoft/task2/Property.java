package ilya.profitsoft.task2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD,
        ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
    
    String name() default "N/A";
    
    String format() default "N/A";
}
