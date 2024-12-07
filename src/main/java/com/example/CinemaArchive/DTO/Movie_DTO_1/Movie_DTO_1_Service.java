package com.example.CinemaArchive.DTO.Movie_DTO_1;

import java.util.List;



public interface Movie_DTO_1_Service {
    List<MovieDTO> getAllMoviesDTO();
    MovieDTO getMovieByIdDTO(Long id);
    // List<MovieDTO> getMoviesByGenreDTO(Long genreId);
}

