package ru.nc.musiclib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nc.musiclib.model.Track;

public interface TrackRepository extends JpaRepository<Track, Integer> {
}
