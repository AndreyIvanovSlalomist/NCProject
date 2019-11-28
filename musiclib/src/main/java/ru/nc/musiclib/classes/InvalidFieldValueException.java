package ru.nc.musiclib.classes;

public class InvalidFieldValueException extends RuntimeException {
    public InvalidFieldValueException(String s){
        super("Неверное значение поля: "+s);
    }
}
