package ru.nc.musiclib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nc.musiclib.model.Genre;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Optional<Genre> findByGenreName(String genreName);

}
