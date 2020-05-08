package ru.nc.musiclib.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.nc.musiclib.utils.exceptions.TrackNotFoundException;

@ControllerAdvice
public class TrackNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(TrackNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String trackNotFoundHandler(TrackNotFoundException ex){
        return ex.getMessage();
    }
}
