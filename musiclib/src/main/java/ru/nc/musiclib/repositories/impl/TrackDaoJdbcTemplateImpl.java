package ru.nc.musiclib.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.nc.musiclib.model.Genre;
import ru.nc.musiclib.model.Track;
import ru.nc.musiclib.repositories.TrackDao;
import ru.nc.musiclib.utils.MusicLibLogger;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TrackDaoJdbcTemplateImpl implements TrackDao {
    private static final String SQL_SELECT_ALL = "select * from track";
    private static final String SQL_SELECT_TRACK = "select * from track where name = ? and singer = ? and album = ? and length = ?";
    private static final String SQL_SELECT_BY_ID = "select * from track where id =?";
    private static final String SQL_SELECT_ALL_GENRE = "select * from lib_Genre";
    private static final String SQL_SELECT_GENRE_BY_ID = "select * from lib_Genre where id =?";
    private static final String SQL_SELECT_GENRE_BY_NAME = "select * from lib_Genre where genreName =?";
    private static final String SQL_INSERT_TRACK = "INSERT Into track (name, singer, album, length, id_Genre)VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_GENRE = "INSERT Into lib_Genre (genreName)VALUES (?)";
    private static final String SQL_UPDATE_TRACK_BY_ID = "Update track set name = ?, singer = ?, album = ?, length = ?, id_Genre = ?  where id = ?";
    private static final String SQL_UPDATE_GENRE_BY_ID = "Update lib_Genre set genreName = ? where id = ?";
    private static final String SQL_DELETE_BY_ID = "Delete from track where id = ?";
    private static final String SQL_DELETE_TRACK = "Delete from track where name = ? and singer = ? and album = ? and length = ?";
    private final static MusicLibLogger logger = new MusicLibLogger(TrackDaoJdbcTemplateImpl.class);
    JdbcTemplate jdbcTemplate;
    private RowMapper<Genre> genreRowMapper = (resultSet, i) -> new Genre(resultSet.getInt("id"),
            resultSet.getString("genreName"));
    private RowMapper<Track> trackRowMapper = (resultSet, i) -> new Track(resultSet.getInt("id"),
            resultSet.getString("name"),
            resultSet.getString("singer"),
            resultSet.getString("album"),
            resultSet.getInt("length"),
            findGenre(resultSet.getInt("id_Genre")).orElse(null));

    @Autowired
    public TrackDaoJdbcTemplateImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Track findTrack(String name, String singer, String album, int length) {
        try {
            return jdbcTemplate.queryForObject(SQL_SELECT_TRACK, new Object[]{name, singer, album, length}, trackRowMapper);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    public Map<Integer, Genre> findAllGenre() {
        return jdbcTemplate.query(SQL_SELECT_ALL_GENRE, genreRowMapper)
                .stream()
                .collect(Collectors.toMap(Genre::getId, genre -> genre));
    }

    private Optional<Genre> findGenre(int id) {
        List<Genre> result = jdbcTemplate.query(SQL_SELECT_GENRE_BY_ID, genreRowMapper, id);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public Optional<Track> find(Integer id) {
        List<Track> result = jdbcTemplate.query(SQL_SELECT_BY_ID, trackRowMapper, id);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public void save(Track model) {
        jdbcTemplate.update(SQL_INSERT_TRACK, model.getName(), model.getSinger(), model.getAlbum(),model.getLengthInt(), findOrSaveGenre(model.getGenreName()).getId());
    }

    @Override
    public void update(Track model) {
        jdbcTemplate.update(SQL_UPDATE_TRACK_BY_ID, model.getName(), model.getSinger(), model.getAlbum(),model.getLengthInt(), findOrSaveGenre(model.getGenreName()).getId(), model.getId());
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, id);
    }

    @Override
    public void delete(Track model) {
        jdbcTemplate.update(SQL_DELETE_TRACK, model.getName(), model.getSinger(), model.getAlbum(), model.getLengthInt());
    }

    @Override
    public List<Track> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, trackRowMapper);
    }

    private Genre findOrSaveGenre(String name){
        try {
            Genre genre = jdbcTemplate.queryForObject(SQL_SELECT_GENRE_BY_NAME, new Object[]{name}, genreRowMapper);
            return genre;
        }
        catch (DataAccessException ex){
            jdbcTemplate.update(SQL_INSERT_GENRE, name);
            return findOrSaveGenre(name);
        }
    }
}
