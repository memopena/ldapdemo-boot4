package com.woltersklower.demo.validation;

public class EmailExistsException extends Exception {

    public EmailExistsException(final String message) {
        super(message);
    }

}
