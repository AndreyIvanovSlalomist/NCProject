package ru.nc.musiclib.repositories;

import java.util.List;
import java.util.Optional;

/**
 * save - сохранение нового объкта в базу
 *
 */
public interface CrudDao<T> {
    Optional<T> find(Integer id);
    void save(T model);
    void update(T model);
    void delete(Integer id);
    void delete(T model);
    List<T> findAll();
}
