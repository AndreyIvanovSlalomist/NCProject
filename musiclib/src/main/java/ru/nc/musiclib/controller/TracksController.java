package ru.nc.musiclib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.db.dao.TrackDao;

import java.util.List;
@Controller
public class TracksController {
    @Autowired
    private TrackDao trackDao;

    @RequestMapping(value = "/tracks", method = RequestMethod.GET)
    public String getAllTracks(ModelMap model) {
        List<Track> tracks = trackDao.findAll();
        model.addAttribute("tracksFromServer", tracks);
        return "tracks";
    }

}
