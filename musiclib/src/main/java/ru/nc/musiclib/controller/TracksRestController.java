package ru.nc.musiclib.controller;

import lombok.var;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nc.musiclib.forms.UserForm;
import ru.nc.musiclib.model.Genre;
import ru.nc.musiclib.model.Role;
import ru.nc.musiclib.model.Track;
import ru.nc.musiclib.model.User;
import ru.nc.musiclib.services.LoginService;
import ru.nc.musiclib.services.Model;
import ru.nc.musiclib.services.SignUpService;
import ru.nc.musiclib.services.UserModel;
import ru.nc.musiclib.transfer.*;
import ru.nc.musiclib.utils.exceptions.TrackNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class TracksRestController {
    @Autowired
    private Model trackModel;
    @Autowired
    private UserModel userModel;

    @GetMapping("/api/tracks")
    @ResponseBody
    public TrackListDto getAllTracks() {
        return TrackListDto.from(trackModel.getAll());
    }

    @PostMapping("/api/tracks")
    TrackDto newTrack(@RequestBody Track track) {
        return TrackDto.from(trackModel.save(track));
    }

    //Сохранение
    @GetMapping("/api/track/{id}")
    TrackDto getOneById(@PathVariable Integer id) {
        return trackModel.find(id).map(TrackDto::from).orElseThrow(() -> new TrackNotFoundException(id));
    }
    //Редактирование
    @PostMapping("/api/tracks/{id}/update")
    public Boolean updateTrack(@RequestBody TrackDto newTrack, @PathVariable Integer id){
        Optional<Track> track = trackModel.find(id);
        return track.filter(value -> trackModel.update(value, newTrack.getName(), newTrack.getSinger(), newTrack.getAlbum(), newTrack.getLength(), newTrack.getGenre().getGenreName())).isPresent();
    }

    @PostMapping("/api/tracks/add")
    public void addTrack(@RequestBody TrackDto track){
        trackModel.add(track.getName(),track.getSinger(),track.getAlbum(),track.getLength(),track.getGenre().getGenreName(),false);
    }

    @GetMapping(value = "/api/tracks/{id}/delete")
    public Integer deleteTrack(@PathVariable Integer id) {

        trackModel.delete(id);
        return 0;
    }

    @Autowired
    private LoginService loginService;
    @Autowired
    private SignUpService service;

    @PostMapping("/api/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserForm userForm) {
        return ResponseEntity.ok(loginService.login(userForm));
    }

    @PostMapping("/api/signUp")
    public Boolean signUp(@RequestBody UserForm userForm){
        return service.signUp(userForm);
    }

    @GetMapping("/api/getRole/{userName}")
    @ResponseBody
    public RoleDto getRole(@PathVariable String userName) {
        return RoleDto.fromRole(userModel.getRole(userName));
    }

    @GetMapping("/api/users")
    @ResponseBody
    public UserDto[] getAllUsers() {
        return userModel.getAllUser().stream().map(UserDto::fromUser).toArray(UserDto[]::new);
    }

    @PostMapping (value="api/user/{username}/delete")
    public void deleteUser(@PathVariable String username){
        userModel.delete(username);
    }
    @PostMapping(value="api/user/{username}/setRole")
    public void setRole(@PathVariable String username, @RequestBody RoleDto roleDto){
        if(userModel.findUser(username)!=null){
            userModel.setRole(username,RoleDto.fromRoleDto(roleDto));
        }
    }

}
