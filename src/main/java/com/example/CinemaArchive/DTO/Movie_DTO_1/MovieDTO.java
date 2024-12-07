package com.example.CinemaArchive.DTO.Movie_DTO_1;

import java.util.List;




public class MovieDTO {
    private Long id;
    private String title;
    private Integer releaseYear;
    private String duration;
    private List<ActorDTO> actors;
    private List<GenreDTO> genres;

    // Constructor
    public MovieDTO(Long id, String title, Integer releaseYear, String duration, List<ActorDTO> actors, List<GenreDTO> genres) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
        this.actors = actors;
        this.genres = genres;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<ActorDTO> getActors() {
        return actors;
    }

    public void setActors(List<ActorDTO> actors) {
        this.actors = actors;
    }

    public List<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreDTO> genres) {
        this.genres = genres;
    }
}
