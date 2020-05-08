package ru.nc.musiclib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nc.musiclib.model.Token;

import java.util.Optional;

public interface TokensRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findOneByValue(String value);
}
