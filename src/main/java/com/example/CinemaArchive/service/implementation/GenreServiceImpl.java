package com.example.CinemaArchive.service.implementation;

import com.example.CinemaArchive.Exception.BadRequestException;
import com.example.CinemaArchive.Exception.ResourceNotFoundException;
import com.example.CinemaArchive.model.Genre;
import com.example.CinemaArchive.model.Movie;
import com.example.CinemaArchive.repository.GenreRepository;
import com.example.CinemaArchive.repository.MovieRepository;
import com.example.CinemaArchive.service.GenreService;
import com.example.CinemaArchive.service.MovieService;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final MovieService movieService;
    private final MovieRepository movieRepository;

    public GenreServiceImpl(GenreRepository genreRepository, MovieRepository movieRepository, MovieService movieService) {
        this.genreRepository = genreRepository;
        this.movieService = movieService;
        this.movieRepository = movieRepository;
    }
    // GET ALL GENRES
    // @Override
    // public List<Genre> getAllGenres() {
    //     return genreRepository.findAll();  // Retrieve all genres from the database
    // }
    // CREATE GENRE
    @Override
    public Genre saveGenre(Genre genre) {
        return genreRepository.save(genre);  // Save or update the genre
    }
    // GET GENRE BY ID
    @Override
    public Optional<Genre> getGenreById(Long id) {
        return genreRepository.findById(id);  // Find a genre by ID
    }
    // UPDATE GENRE
    @Override
    public Genre updateGenre(Long id, Map<String, Object> updates) {
        Genre genre = genreRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Genre not found"));

        if (updates.containsKey("name")) {
            genre.setName((String) updates.get("name"));
        }

        // Update movies if present
        if (updates.containsKey("movies")) {
            @SuppressWarnings("unchecked")
            List<Integer> newMovieIdsAsInt = (List<Integer>) updates.get("movies");
        
            // Convert List<Integer> to List<Long>
            List<Long> newMovieIds = newMovieIdsAsInt.stream()
                                                    .map(Integer::longValue)
                                                    .toList();
        
            // Fetch the current list of movies associated with the genre
            List<Long> currentMovieIds = genre.getMovies().stream()
                                            .map(Movie::getId)
                                            .toList();

            // Determine movies to add and remove
            List<Long> moviesToAdd = newMovieIds.stream()
                                                .filter(movieId -> !currentMovieIds.contains(movieId))
                                                .toList();
            List<Long> moviesToRemove = currentMovieIds.stream()
                                                    .filter(movieId -> !newMovieIds.contains(movieId))
                                                    .toList();

            // Use existing methods to handle addition/removal
            for (Long movieId : moviesToAdd) {
                movieService.addGenresToMovie(movieId, List.of(id));
            }
            for (Long movieId : moviesToRemove) {
                movieService.removeGenresFromMovie(movieId, List.of(id));
            }

            // Refresh the genre's movie list
            genre.setMovies(new HashSet<>(movieRepository.findAllById(newMovieIds)));
        }
        return genreRepository.save(genre);
    }
    // DELETE GENRE
    @Override
    public void deleteGenre(Long id, boolean force) {
        // Retrieve the genre or throw ResourceNotFoundException if not found
        Genre genre = genreRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Genre not found for id: " + id));
    
        if (force) {
            // Clear relationships and perform force deletion
            genre.getMovies().forEach(movie -> movie.getGenres().remove(genre)); // Clear genre from related movies
            genre.getMovies().clear(); // Clear movies from the genre
            genreRepository.save(genre); // Persist relationship changes
            genreRepository.delete(genre); // Delete the genre
        } else {
            // Check if there are related movies before deleting
            if (!genre.getMovies().isEmpty()) {
                throw new BadRequestException(
                    "Cannot delete genre '" + genre.getName() + "' because this genre has " 
                    + genre.getMovies().size() + " associated movies."
                );
            }
            genreRepository.delete(genre); // Perform standard deletion
        }
    }
    // GET ALL GENRES
    // (pagination )
    // (get genres sorted in alphabetical order )
    @Override
    public List<Genre> getGenres(Integer page, Integer size, Boolean abc) {
        if (page != null && size != null) {
            validatePagination(page, size); // Extracted validation logic
            page = page + 1;
            int offset = (page - 1) * size;
            return genreRepository.findGenresWithPagination(size, offset);
        }
        if (Boolean.TRUE.equals(abc)) {
            return genreRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        }
        if (Boolean.FALSE.equals(abc)) {
            return genreRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
        }
        return genreRepository.findAll(); // Default case: return all genres
    }
    private void validatePagination(int page, int size) {
        if (page < 0 && size < 0) {
            throw new BadRequestException("Page and size cannot be smaller than zero.");
        }
        if (page < 0) {
            throw new BadRequestException("Page cannot be smaller than zero.");
        }
        if (size < 0) {
            throw new BadRequestException("Page size cannot be smaller than zero.");
        }
    }
}
