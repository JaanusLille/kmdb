package com.example.CinemaArchive.service;

import com.example.CinemaArchive.model.Genre;
import java.util.List;
import java.util.Map;
import java.util.Optional;



public interface GenreService {
    // List<Genre> getAllGenres();  // Get all genres
    Genre saveGenre(Genre genre);  // Save a new genre
    Optional<Genre> getGenreById(Long id);  // Find a genre by ID
    Genre updateGenre(Long id, Map<String, Object> updates);
    void deleteGenre(Long id, boolean force);  // Delete a genre by ID
    public List<Genre> getGenres(Integer page, Integer size, Boolean abc);
}
