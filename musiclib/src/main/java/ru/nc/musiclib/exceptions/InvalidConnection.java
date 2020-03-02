package ru.nc.musiclib.exceptions;

public class InvalidConnection extends RuntimeException {
    public InvalidConnection() {
        super("Нет подключения к базе данных");
    }
}