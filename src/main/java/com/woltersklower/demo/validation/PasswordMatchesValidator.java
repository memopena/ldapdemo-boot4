package com.woltersklower.demo.validation;

import com.woltersklower.demo.web.model.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        return obj instanceof User user
                && Objects.equals(user.getPassword(), user.getPasswordConfirmation());
    }

}
