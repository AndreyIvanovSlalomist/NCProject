package ru.nc.musiclib.repositories;

import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.nc.musiclib.model.User;

import java.util.Optional;


public interface UsersRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
}
