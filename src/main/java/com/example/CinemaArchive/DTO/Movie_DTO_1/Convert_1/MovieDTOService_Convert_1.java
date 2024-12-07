package com.example.CinemaArchive.DTO.Movie_DTO_1.Convert_1;

import com.example.CinemaArchive.DTO.Movie_DTO_1.ActorDTO;
import com.example.CinemaArchive.DTO.Movie_DTO_1.GenreDTO;
import com.example.CinemaArchive.DTO.Movie_DTO_1.MovieDTO;
import com.example.CinemaArchive.model.Movie;
import java.util.List;
import org.springframework.stereotype.Service;




@Service
public class MovieDTOService_Convert_1 {
    
    // Assuming ActorDTOService exists for mapping Actor to ActorDTO
    private final ActorDTOService_Convert_1 actorDTOService;
    private final GenreDTOService_Convert_1 genreDTOService;

    public MovieDTOService_Convert_1(ActorDTOService_Convert_1 actorDTOService, GenreDTOService_Convert_1 genreDTOService) {
        this.actorDTOService = actorDTOService;
        this.genreDTOService = genreDTOService;
    }

    public MovieDTO convertToDTO(Movie movie) {
        if (movie == null) {
            return null;
        }

        List<ActorDTO> actorDTOs = movie.getActors().stream()
                                        .map(actorDTOService::convertToDTO)
                                        .toList();

        List<GenreDTO> genreDTOs = movie.getGenres().stream()
                                        .map(genreDTOService::convertToDTO)
                                        .toList();

        return new MovieDTO(
            movie.getId(),
            movie.getTitle(),
            movie.getReleaseYear(),
            movie.getDuration(),
            actorDTOs, 
            genreDTOs
        );
    }
}
