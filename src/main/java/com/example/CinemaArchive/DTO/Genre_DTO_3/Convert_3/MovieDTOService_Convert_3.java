package com.example.CinemaArchive.DTO.Genre_DTO_3.Convert_3;

import com.example.CinemaArchive.DTO.Genre_DTO_3.MovieDTO;
import com.example.CinemaArchive.model.Movie;
import org.springframework.stereotype.Service;


@Service
public class MovieDTOService_Convert_3 {
    public MovieDTO convertToDTO(Movie movie){
        if(movie == null){
            return null;
        }
        return new MovieDTO(
            movie.getId(),
            movie.getTitle()
        );
    }
}
