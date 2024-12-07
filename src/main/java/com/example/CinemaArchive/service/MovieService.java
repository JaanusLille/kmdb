package com.example.CinemaArchive.service;

import com.example.CinemaArchive.model.Actor;
import com.example.CinemaArchive.model.Movie;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MovieService {
    public List<Movie> getAllMovies();
    public Movie createMovie(Map<String, Object> movieData);
    public Optional<Movie> getMovieById(Long id);
    public Movie updateMovie(Long id, Map<String, Object> updates);
    public void deleteMovie(Long id, boolean force);
    public List<Movie> searchMoviesByTitle(String title);
    public void addActorsToMovie(Long movieId, List<Long> actorIds);
    public void removeActorsFromMovie(Long movieId, List<Long> actorIds);
    public List<Actor> getActorsByMovieId(Long movieId);
    public void addGenresToMovie(Long movieId, List<Long> genreIds);
    public void removeGenresFromMovie(Long movieId, List<Long> genreIds);
    List<Movie> getMovies(Long genre, Integer year, Long actor, Integer page, Integer size, Boolean abc);
}
