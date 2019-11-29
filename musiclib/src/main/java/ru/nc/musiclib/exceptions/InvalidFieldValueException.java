package ru.nc.musiclib.exceptions;

public class InvalidFieldValueException extends RuntimeException {
    public InvalidFieldValueException(String s) {
        super("Неверное значение поля: " + s);
    }
}
