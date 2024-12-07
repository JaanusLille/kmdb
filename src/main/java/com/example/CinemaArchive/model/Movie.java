package com.example.CinemaArchive.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;




@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "title cannot be null.")
    // @NotBlank(message = "title is required.")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 50 characters")
    private String title;

    @Column(name = "releaseYear", nullable = false)
    @NotNull(message = "releaseYear cannot be null.")
    @Min(value = 1888, message = "releaseYear must be a valid year (at least 1888).")
    @Max(value = 9999, message = "releaseYear must be a valid year (no more than 9999).")
    private Integer releaseYear;

    @Column(nullable = false)
    // @NotBlank(message = "duration is required.")
    @NotNull(message = "duration cannot be null.")
    private String duration;

    @ManyToMany
    // @JsonManagedReference
    @JoinTable(
        name = "movies_actors",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "movies_genres",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();


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

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }


    // public List<Long> getActorIds() {
    //     return actorIds;
    // }

    // public void setActorIds(List<Long> actorIds) {
    //     this.actorIds = actorIds;
    // }

    // public List<Long> getGenreIds() {
    //     return genreIds;
    // }

    // public void setGenreIds(List<Long> genreIds) {
    //     this.genreIds = genreIds;
    // }
}
