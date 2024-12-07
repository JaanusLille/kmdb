package com.example.CinemaArchive.DTO.Genre_DTO_3;

import java.util.List;

public interface Genre_DTO_3_Service {
    List<GenreDTO> getAllGenresDTO();
    GenreDTO getGenreByIdDTO(Long id);
}