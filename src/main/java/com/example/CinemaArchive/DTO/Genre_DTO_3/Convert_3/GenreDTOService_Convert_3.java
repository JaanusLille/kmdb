package com.example.CinemaArchive.DTO.Genre_DTO_3.Convert_3;

import com.example.CinemaArchive.DTO.Genre_DTO_3.GenreDTO;
import com.example.CinemaArchive.DTO.Genre_DTO_3.MovieDTO;
import com.example.CinemaArchive.model.Genre;
import java.util.List;
import org.springframework.stereotype.Service;




@Service
public class GenreDTOService_Convert_3 {
    
    private final MovieDTOService_Convert_3 movieDTOService;

    public GenreDTOService_Convert_3(MovieDTOService_Convert_3 movieDTOService) {
        this.movieDTOService = movieDTOService;
    }

    public GenreDTO convertToDTO(Genre genre) {
        if (genre == null) {
            return null;
        }

        List<MovieDTO> movieDTOs = genre.getMovies().stream()
                                        .map(movieDTOService::convertToDTO)
                                        .toList();


        return new GenreDTO(
            genre.getId(),
            genre.getName(),
            movieDTOs
        );

    }
}
// seal võttis actor moviesid
// siin võtab genre moviesid
