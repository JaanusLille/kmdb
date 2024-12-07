package com.example.CinemaArchive.DTO.Actor_DTO_2;


import java.util.List;

public class ActorDTO {
    private Long id;
    private String name;
    private String birthDate;
    private List<MovieDTO> movies;

    // Constructor
    public ActorDTO(Long id, String name, String birthDate, List<MovieDTO> movies) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public List<MovieDTO> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieDTO> movies) {
        this.movies = movies;
    }
}
