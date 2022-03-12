package com.joserbatista.service.common.validation.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NotBlank
@Pattern(regexp = "^[A-zÀ-ú0-9]+$")
@ReportAsSingleViolation
@Constraint(validatedBy = { })
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.TYPE_USE})
@Repeatable(Word.List.class)
public @interface Word {

    String message() default "not valid";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * Defines several {@code @Word} constraints on the same element.
     *
     * @see Word
     */
    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {

        Word[] value();
    }
}
