package ru.nc.musiclib.repositories;

import java.util.List;
import java.util.Optional;

/**
 * save - сохранение нового объкта в базу
 *
 */
public interface CrudDao<T> {
    Optional<T> find(Integer id);
    boolean save(T model);
    boolean update(T model);
    boolean delete(Integer id);
    boolean delete(T model);
    List<T> findAll();
}
