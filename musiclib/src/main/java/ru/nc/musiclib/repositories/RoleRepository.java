package ru.nc.musiclib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nc.musiclib.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
