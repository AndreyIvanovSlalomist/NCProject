package ru.nc.musiclib.utils.exceptions;

public class TrackNotFoundException extends RuntimeException {
   public TrackNotFoundException(Integer id){
        super("Не найден трек " + id);
    }
}
