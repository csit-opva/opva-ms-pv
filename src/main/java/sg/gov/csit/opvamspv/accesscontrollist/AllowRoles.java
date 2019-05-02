package sg.gov.csit.opvamspv.accesscontrollist;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AllowRoles {
    Role[] roles() default {};
    String action() default "Unknown Action";
}
