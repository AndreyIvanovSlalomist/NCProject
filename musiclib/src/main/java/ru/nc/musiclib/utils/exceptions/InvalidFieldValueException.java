package ru.nc.musiclib.utils.exceptions;

public class InvalidFieldValueException extends RuntimeException {
    public InvalidFieldValueException(String s) {
        super("Неверное значение поля: " + s);
    }
}
