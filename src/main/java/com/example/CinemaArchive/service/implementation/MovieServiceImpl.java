package com.example.CinemaArchive.service.implementation;

import com.example.CinemaArchive.Exception.BadRequestException;
import com.example.CinemaArchive.Exception.ResourceNotFoundException;
import com.example.CinemaArchive.model.Actor;
import com.example.CinemaArchive.model.Genre;
import com.example.CinemaArchive.model.Movie;
import com.example.CinemaArchive.repository.ActorRepository;
import com.example.CinemaArchive.repository.GenreRepository;
import com.example.CinemaArchive.repository.MovieRepository;
import com.example.CinemaArchive.service.MovieService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;









@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final GenreRepository genreRepository;

    public MovieServiceImpl(MovieRepository movieRepository, ActorRepository actorRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.genreRepository = genreRepository;
    }
    // GET ALL MOVIES
    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
    // CREATE MOVIE
    @Override
    public Movie createMovie(Map<String, Object> movieData) {
        // Extract basic movie fields
        String title = (String) movieData.get("title");
        Integer releaseYear = (Integer) movieData.get("releaseYear");
        String duration = (String) movieData.get("duration");

        // Create and save the base movie
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setReleaseYear(releaseYear);
        movie.setDuration(duration);
        Movie savedMovie = movieRepository.save(movie);

        // Add actors to the movie if present
        if (movieData.containsKey("actors")) {
            @SuppressWarnings("unchecked")
            List<Long> actorIds = ((List<Integer>) movieData.get("actors"))
                    .stream()
                    .map(Long::valueOf)
                    .toList();
            addActorsToMovie(savedMovie.getId(), actorIds);
        }

        // Add genres to the movie if present
        if (movieData.containsKey("genres")) {
            @SuppressWarnings("unchecked")
            List<Long> genreIds = ((List<Integer>) movieData.get("genres"))
                    .stream()
                    .map(Long::valueOf)
                    .toList();
            addGenresToMovie(savedMovie.getId(), genreIds);
        }
        return movieRepository.save(movie);
    }
    // GET MOVIE BY ID
    @Override
    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }
    // UPDATE MOVIE
    @Transactional
    @Override
    public Movie updateMovie(Long id, Map<String, Object> updates) {
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Movie not found"));
    
        if (updates.containsKey("title")) {
            movie.setTitle((String) updates.get("title"));
        }
        if (updates.containsKey("releaseYear")) {
            movie.setReleaseYear((Integer) updates.get("releaseYear"));
        }
        if (updates.containsKey("duration")) {
            movie.setDuration((String) updates.get("duration"));
        }
        // Update actors if present
        if (updates.containsKey("actors")) {
            @SuppressWarnings("unchecked")

            List<Integer> newActorIdsAsInt = (List<Integer>) updates.get("actors");
        
            List<Long> newActorIds = newActorIdsAsInt.stream()
                                                    .map(Integer::longValue)
                                                    .toList();
        
            // Fetch the current list of actors associated with the movie
            List<Long> currentActorIds = movie.getActors().stream()
                                            .map(Actor::getId)
                                            .toList();

            // Determine actors to add and remove
            List<Long> actorsToAdd = newActorIds.stream()
                                                .filter(actorId -> !currentActorIds.contains(actorId))
                                                .toList();
            List<Long> actorsToRemove = currentActorIds.stream()
                                                    .filter(actorId -> !newActorIds.contains(actorId))
                                                    .toList();
            addActorsToMovie(id, actorsToAdd);
            removeActorsFromMovie(id, actorsToRemove);
            movie.setActors(new HashSet<>(actorRepository.findAllById(newActorIds)));
        }
        // Update genres if present
        if (updates.containsKey("genres")) {
            @SuppressWarnings("unchecked")
            List<Integer> newGenreIdsAsInt = (List<Integer>) updates.get("genres");
        
            List<Long> newGenreIds = newGenreIdsAsInt.stream()
                                                    .map(Integer::longValue)
                                                    .toList();
        
            // Fetch the current list of genres associated with the movie
            List<Long> currentGenreIds = movie.getGenres().stream()
                                            .map(Genre::getId)
                                            .toList();

            // Determine genres to add and remove
            List<Long> genresToAdd = newGenreIds.stream()
                                                .filter(genreId -> !currentGenreIds.contains(genreId))
                                                .toList();
            List<Long> genresToRemove = currentGenreIds.stream()
                                                    .filter(genreId -> !newGenreIds.contains(genreId))
                                                    .toList();
            addGenresToMovie(id, genresToAdd);
            removeGenresFromMovie(id, genresToRemove);
            movie.setGenres(new HashSet<>(genreRepository.findAllById(newGenreIds)));
        }
        return movieRepository.save(movie);
    }
    // ADD ACTORS TO MOVIE
    @Override
    public void addActorsToMovie(Long movieId, List<Long> actorIds) {
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + movieId));

        for (Long actorId : actorIds) {
            Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new NoSuchElementException("Actor not found with id: " + actorId));
            movie.getActors().add(actor);
        }
        movieRepository.save(movie);
    }
    // REMOVE ACTORS FROM MOVIE
    @Override
    public void removeActorsFromMovie(Long movieId, List<Long> actorIds) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + movieId));

        List<Actor> actorsToRemove = actorRepository.findAllById(actorIds);
        movie.getActors().removeAll(actorsToRemove);

        movieRepository.save(movie);
    }
    // ADD GENRES TO MOVIE
    @Override
    public void addGenresToMovie(Long movieId, List<Long> genreIds) {
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + movieId));

        for (Long genreId : genreIds) {
            Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new NoSuchElementException("Genre not found with id: " + genreId));
            movie.getGenres().add(genre);
        }
        movieRepository.save(movie);
    }
    // REMOVE GENRES FROM MOVIE
    @Override
    public void removeGenresFromMovie(Long movieId, List<Long> genreIds) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + movieId));

        List<Genre> genresToRemove = genreRepository.findAllById(genreIds);
        movie.getGenres().removeAll(genresToRemove);

        movieRepository.save(movie);
    }
    // DELETE MOVIE
    @Override
    public void deleteMovie(Long id, boolean force) {
        // Retrieve the movie or throw ResourceNotFoundException if not found
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Movie not found for id: " + id));
    
        if (force) {
            // Clear relationships and perform force deletion
            movie.getActors().forEach(actor -> actor.getMovies().remove(movie)); // Clear movie from related actors
            movie.getActors().clear(); // Clear actor from the movie
            movieRepository.save(movie); // Persist relationship changes
            movie.getGenres().forEach(genre -> genre.getMovies().remove(movie)); // Clear movie from related genres
            movie.getGenres().clear(); // Clear genre from the movie
            movieRepository.save(movie); // Persist relationship changes
            movieRepository.delete(movie); // Delete the movie
        } else {
            // Check if there are related genres or actors before deleting
            if (!movie.getActors().isEmpty()) {
                throw new BadRequestException(
                    "Cannot delete movie '" + movie.getTitle() + "' because this movie has " 
                    + movie.getActors().size() + " associated actors."
                );
            }
            if (!movie.getGenres().isEmpty()) {
                throw new BadRequestException(
                    "Cannot delete movie '" + movie.getTitle() + "' because this movie has " 
                    + movie.getGenres().size() + " associated genres."
                );
            }
            movieRepository.delete(movie); // Perform standard deletion
        }
    }
    // GET ALL MOVIES
    // (get movies by genreId )
    // (get movies by releaseYear )
    // (get movies by actorId )
    // (pagination )
    // (get movies sorted in alphabetical order )
    @Override
    public List<Movie> getMovies(Long genre, Integer year, Long actor, Integer page, Integer size, Boolean abc) {
        if (genre != null) {
            return movieRepository.findByGenresId(genre);
        }
        if (year != null) {
            return movieRepository.findByReleaseYear(year);
        }
        if (actor != null) {
            return movieRepository.findByActorsId(actor);
        }
        if (page != null && size != null) {
            validatePagination(page, size); // Extracted validation logic
            page = page + 1;
            int offset = (page - 1) * size;
            return movieRepository.findMoviesWithPagination(size, offset);
        }
        if (Boolean.TRUE.equals(abc)) {
            return movieRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
        }
        if (Boolean.FALSE.equals(abc)) {
            return movieRepository.findAll(Sort.by(Sort.Direction.DESC, "title"));
        }
        return movieRepository.findAll(); // Default case: return all movies
    }
    private void validatePagination(int page, int size) {
        if (page < 0 && size < 0) {
            throw new BadRequestException("Page and size cannot be smaller than zero.");
        }
        if (page > 100 && size > 1000) {
            throw new BadRequestException("Page cannot be bigger than 100 and Page size cannot be bigger than 1000.");
        }
        if (page < 0) {
            throw new BadRequestException("Page cannot be smaller than zero.");
        }
        if (page > 100) {
            throw new BadRequestException("Page cannot be bigger than 100.");
        }
        if (size < 0) {
            throw new BadRequestException("Page size cannot be smaller than zero.");
        }
        if (size > 1000) {
            throw new BadRequestException("Page size cannot be bigger than 1000.");
        }
    }
    // FIND MOVIES BY MOVIE TITLE
    @Override
    public List<Movie> searchMoviesByTitle(String title) {
        if (title != null && !title.isEmpty()) {
            return movieRepository.findByTitleContainingIgnoreCase(title);
        }
        return new ArrayList<>(); // Return an empty list if title is null or empty
    }
    // GET ALL ACTORS IN MOVIE BY MOVIE ID
    @Override
    public List<Actor> getActorsByMovieId(Long movieId) {
        return actorRepository.findByMoviesId(movieId);
    }

}