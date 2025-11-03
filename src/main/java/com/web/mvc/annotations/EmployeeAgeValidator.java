package com.web.mvc.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class EmployeeAgeValidator implements ConstraintValidator<EmployeeAgeValidation, LocalDate> {
    @Override
    public boolean isValid(LocalDate inputDate, ConstraintValidatorContext constraintValidatorContext) {
        return Period.between(inputDate, LocalDate.now()).getYears() > 18;
    }
}
