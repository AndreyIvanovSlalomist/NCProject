package ru.nc.musiclib.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.nc.musiclib.model.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class TrackListDto {
    private List<TrackDto> trackListDto;

    public static TrackListDto from(List<Track> tracks){
        return TrackListDto.builder()
                .trackListDto(tracks.stream().map(TrackDto::from).collect(Collectors.toList()))
                .build();
    }

    public TrackListDto() {
        trackListDto = new ArrayList<>();
    }

    public static List<Track> getTrackList(TrackListDto trackListDto){
        return trackListDto.getTrackListDto().stream().map(trackDto ->  new Track(trackDto.getId(),
                trackDto.getName(), trackDto.getSinger(), trackDto.getAlbum(), trackDto.getLength(), trackDto.getGenre())
                ).collect(Collectors.toList());
    }
}
