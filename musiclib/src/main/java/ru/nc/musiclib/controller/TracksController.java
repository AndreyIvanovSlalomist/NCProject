package ru.nc.musiclib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.db.dao.TrackDao;
import ru.nc.musiclib.model.Model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class TracksController {
    @Autowired
    private TrackDao trackDao;
    @Autowired
    private Model trackModel;

    @RequestMapping(value = "/tracks", method = RequestMethod.GET)
    public String getAllTracks(ModelMap model, @RequestParam(name = "name", required = false, defaultValue = "") String name,
                               @RequestParam(name = "singer", required = false, defaultValue = "") String singer,
                               @RequestParam(name = "album", required = false, defaultValue = "") String album,
                               @RequestParam(name = "genreName", required = false, defaultValue = "") String genreName) {
        List<Track> tracks;
        try {
            name = URLDecoder.decode(name, StandardCharsets.UTF_8.toString());
            singer = URLDecoder.decode(singer, StandardCharsets.UTF_8.toString());
            album = URLDecoder.decode(album, StandardCharsets.UTF_8.toString());
            genreName = URLDecoder.decode(genreName, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!(name.isEmpty() && singer.isEmpty() && album.isEmpty() && genreName.isEmpty())) {
            tracks = trackModel.filter(name.replaceAll("\\+", " "),
                    singer.replaceAll("\\+", " "),
                    album.replaceAll("\\+", " "),
                    genreName.replaceAll("\\+", " "));
        } else {
            tracks = trackModel.getAll();
        }
        try {
            name = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
            singer = URLEncoder.encode(singer, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        model.addAttribute("name", name.replaceAll("\\+", " "));
        model.addAttribute("singer", singer.replaceAll("\\+", " "));
        model.addAttribute("album", album.replaceAll("\\+", " "));
        model.addAttribute("genreName", genreName.replaceAll("\\+", " "));
        model.addAttribute("tracksFromServer", tracks);
        return "tracks";
    }
    @RequestMapping(value = "/tracks/add")
    public String add(){
        return "/addtrack";
    }

}
