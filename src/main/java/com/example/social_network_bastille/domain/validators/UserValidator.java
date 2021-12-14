package com.example.social_network_bastille.domain.validators;

import com.example.social_network_bastille.domain.User;

public class UserValidator implements Validator<User> {
    public static final String NEW_LINE = "\n";

    @Override
    public void validate(User user) throws ValidationException {
        StringBuilder errors = new StringBuilder();
        if (user.getLastName().isEmpty() || user.getLastName() == null) {
            errors.append("Invalid last name" + NEW_LINE);
        }
        if (user.getFirstName().isEmpty() || user.getFirstName() == null) {
            errors.append("Invalid first name" + NEW_LINE);
        }
        if (user.getAccount() == null) {
            errors.append("The account cannot be null!" + NEW_LINE);
        } else {
            if (!EmailValidator.isValidEmailAddress(user.getAccount().getEmail())) {
                errors.append("Introduce a valid email!" + NEW_LINE);
            }
            if (user.getAccount().getPassword().isEmpty()) {
                errors.append("Introduce a password that is valid!" + NEW_LINE);
            }
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toString());
        }
    }
}
