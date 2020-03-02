package ru.nc.musiclib.db.dao.impl;

import ru.nc.musiclib.classes.Genre;
import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.db.dao.TrackDao;
import ru.nc.musiclib.exceptions.InvalidConnection;
import ru.nc.musiclib.logger.MusicLibLogger;

import java.sql.*;
import java.util.*;

import static ru.nc.musiclib.db.ConnectionFormProperties.connectionFormProperties;

public class TrackDaoImpl implements TrackDao {

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
    private final static MusicLibLogger logger = new MusicLibLogger(TrackDaoImpl.class);
    private Optional<Connection> connection;

    public TrackDaoImpl() {
        connection = connectionFormProperties().getConnection();
    }

    private Connection getConnection() {
        return connection.orElseThrow(InvalidConnection::new);
    }

    @Override
    public Track findTrack(String name, String singer, String album, int length) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SQL_SELECT_TRACK);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, singer);
            preparedStatement.setString(3, album);
            preparedStatement.setInt(4, length);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Track(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("singer"),
                        resultSet.getString("album"),
                        resultSet.getInt("length"),
                        findGenre(resultSet.getInt("id_Genre")));
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public Optional<Track> find(Integer id) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SQL_SELECT_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Track(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("singer"),
                        resultSet.getString("album"),
                        resultSet.getInt("length"),
                        findGenre(resultSet.getInt("id_Genre"))));
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    @Override
    public void save(Track model) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SQL_INSERT_TRACK);
            preparedStatement.setString(1, model.getName());
            preparedStatement.setString(2, model.getSinger());
            preparedStatement.setString(3, model.getAlbum());
            preparedStatement.setInt(4, model.getLengthInt());
            preparedStatement.setInt(5, findOrSaveGenre(model.getGenreName()).getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    @Override
    public void update(Track model) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SQL_UPDATE_TRACK_BY_ID);
            preparedStatement.setString(1, model.getName());
            preparedStatement.setString(2, model.getSinger());
            preparedStatement.setString(3, model.getAlbum());
            preparedStatement.setInt(4, model.getLengthInt());
            preparedStatement.setInt(5, findOrSaveGenre(model.getGenreName()).getId());
            preparedStatement.setInt(6, model.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SQL_DELETE_BY_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    @Override
    public void delete(Track model) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SQL_DELETE_TRACK);
            preparedStatement.setString(1, model.getName());
            preparedStatement.setString(2, model.getSinger());
            preparedStatement.setString(3, model.getAlbum());
            preparedStatement.setInt(4, model.getLengthInt());
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    private Genre findOrSaveGenre(String name) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SQL_SELECT_GENRE_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Genre(resultSet.getInt("id"), resultSet.getString("genreName"));
            } else {
                preparedStatement = getConnection().prepareStatement(SQL_INSERT_GENRE, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, name);
                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return new Genre(resultSet.getInt("id"), name);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }

    private Genre findGenre(int id) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SQL_SELECT_GENRE_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Genre(resultSet.getInt("id"), resultSet.getString("genreName"));
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }

    public Map<Integer, Genre> findAllGenre() {
        Map<Integer, Genre> genres = new HashMap<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_GENRE);
            while (resultSet.next()) {
                genres.put(resultSet.getInt("id"), (new Genre(resultSet.getInt("id"), resultSet.getString("genreName"))));
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }
        return genres;
    }

    @Override
    public List<Track> findAll() {
        List<Track> tracks = new ArrayList<>();
        try {
            Map<Integer, Genre> genres = findAllGenre();
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
            while (resultSet.next()) {
                tracks.add(new Track(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("singer"),
                        resultSet.getString("album"),
                        resultSet.getInt("length"),
                        genres.get(resultSet.getInt("id_Genre"))));
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }
        return tracks;
    }
}
