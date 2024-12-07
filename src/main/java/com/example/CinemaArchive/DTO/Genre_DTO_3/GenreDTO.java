package com.example.CinemaArchive.DTO.Genre_DTO_3;

// import com.example.CinemaArchive.DTO.Genre_DTO_3.MovieDTO;
import java.util.List;

public class GenreDTO {
    private Long id;
    private String name;
    private List<MovieDTO> movies;

    // Constructor
    public GenreDTO(Long id, String name, List<MovieDTO> movies) {
        this.id = id;
        this.name = name;
        this.movies = movies;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MovieDTO> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieDTO> movies) {
        this.movies = movies;
    }
}
