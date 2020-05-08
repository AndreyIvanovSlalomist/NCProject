package ru.nc.musiclib.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nc.musiclib.model.Genre;
import ru.nc.musiclib.model.Track;

import java.io.Serializable;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TrackDto  implements Serializable, Cloneable {
    private Integer id;
    private String name;
    private String singer;
    private String album;
    private Integer length;
    private Genre genre;

    public static TrackDto from(Track track){
        return TrackDto.builder()
                .id(track.getId())
                .name(track.getName())
                .length(track.getLengthInt())
                .album(track.getAlbum())
                .singer(track.getSinger())
                .genre(track.getGenre())
                .build();
    }
}
