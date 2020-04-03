package ru.nc.musiclib.utils.exceptions;

public class InvalidConnection extends RuntimeException {
    public InvalidConnection() {
        super("Нет подключения к базе данных");
    }
}