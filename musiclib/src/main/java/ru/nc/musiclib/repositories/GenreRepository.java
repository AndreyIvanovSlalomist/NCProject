package ru.nc.musiclib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nc.musiclib.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

}
