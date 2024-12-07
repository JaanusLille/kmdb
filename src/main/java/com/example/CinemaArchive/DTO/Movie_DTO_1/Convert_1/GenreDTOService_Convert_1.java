package com.example.CinemaArchive.DTO.Movie_DTO_1.Convert_1;
import com.example.CinemaArchive.DTO.Movie_DTO_1.GenreDTO;
import com.example.CinemaArchive.model.Genre;
import org.springframework.stereotype.Service;

@Service
public class GenreDTOService_Convert_1 {

    public GenreDTO convertToDTO(Genre genre) {
        if (genre == null) {
            return null;
        }
        return new GenreDTO(genre.getId(), genre.getName());
    }
}
