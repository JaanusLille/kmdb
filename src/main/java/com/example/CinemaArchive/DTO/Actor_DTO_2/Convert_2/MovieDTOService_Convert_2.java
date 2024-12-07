package com.example.CinemaArchive.DTO.Actor_DTO_2.Convert_2;

import com.example.CinemaArchive.DTO.Actor_DTO_2.MovieDTO;
import com.example.CinemaArchive.model.Movie;
import org.springframework.stereotype.Service;


@Service
public class MovieDTOService_Convert_2 {
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
