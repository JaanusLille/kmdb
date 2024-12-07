package com.example.CinemaArchive.DTO.Genre_DTO_3;

import com.example.CinemaArchive.Exception.ResourceNotFoundException;
import com.example.CinemaArchive.repository.GenreRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
// import com.example.CinemaArchive.DTO.Genre_DTO_3.GenreDTO;
// import com.example.CinemaArchive.DTO.Genre_DTO_3.Genre_DTO_3_Service;
// import com.example.CinemaArchive.DTO.Genre_DTO_3.MovieDTO;







@Service
public class Genre_DTO_3_ServiceImpl implements Genre_DTO_3_Service {
    private final GenreRepository genreRepository;

    public Genre_DTO_3_ServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<GenreDTO> getAllGenresDTO() {
        return genreRepository.findAll().stream().map(genre -> 
            new GenreDTO(
                genre.getId(),
                genre.getName(),
                genre.getMovies().stream()
                    .map(movie -> new MovieDTO(movie.getId(), movie.getTitle()))
                    .collect(Collectors.toList())
            )
        ).collect(Collectors.toList());
    }


    @Override
    public GenreDTO getGenreByIdDTO(Long id) {
        return genreRepository.findById(id)
                              .map(genre -> new GenreDTO(
                                genre.getId(),
                                genre.getName(),
                                genre.getMovies().stream()
                                       .map(movie -> new MovieDTO(movie.getId(), movie.getTitle()))
                                       .collect(Collectors.toList())
                              ))
                              .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
    }
}

