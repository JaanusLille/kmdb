package com.example.CinemaArchive.service.implementation;

import com.example.CinemaArchive.Exception.BadRequestException;
import com.example.CinemaArchive.Exception.ResourceNotFoundException;
import com.example.CinemaArchive.model.Actor;
import com.example.CinemaArchive.model.Movie;
import com.example.CinemaArchive.repository.ActorRepository;
import com.example.CinemaArchive.repository.MovieRepository;
import com.example.CinemaArchive.service.ActorService;
import com.example.CinemaArchive.service.MovieService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;
    private final MovieService movieService;
    private final MovieRepository movieRepository;

    public ActorServiceImpl(ActorRepository actorRepository, MovieService movieService, MovieRepository movieRepository) {
        this.actorRepository = actorRepository;
        this.movieService = movieService;
        this.movieRepository = movieRepository;
    }

    private void validateBirthDate(String birthDate) {
        // Regex to match basic YYYY-MM-DD format
        if (!birthDate.matches("\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])")) {
            throw new IllegalArgumentException("birthDate format invalid");
        }

        // Additional validation using LocalDate
        try {
            LocalDate date = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int year = date.getYear();

            if (year < 1888 || year > 2024) {
                throw new IllegalArgumentException("birthDate year must be between 1888 and 2024");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date provided.");
        }
    }
    // CREATE ACTOR
    @Override
    public Actor saveActor(Actor actor) {
        validateBirthDate(actor.getBirthDate());
        return actorRepository.save(actor); // Save or update the actor
    }
    // GET ACTOR BY ID
    @Override
    public Optional<Actor> getActorById(Long id) {
        return Optional.of(actorRepository.findById(id) // Find an actor by their ID
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id)));
    }
    // UPDATE ACTOR
    @Override
    public Actor updateActor(Long id, Map<String, Object> updates) {
        Actor actor = actorRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Actor not found"));

        if (updates.containsKey("birthDate")) {
            String newBirthDate = (String) updates.get("birthDate");
            validateBirthDate(newBirthDate);
            actor.setBirthDate(newBirthDate);
        }
        if (updates.containsKey("name")) {
            actor.setName((String) updates.get("name"));
        }

        // Update movies if present
        if (updates.containsKey("movies")) {
            @SuppressWarnings("unchecked")
            List<Integer> newMovieIdsAsInt = (List<Integer>) updates.get("movies");
        
            // Convert List<Integer> to List<Long>
            List<Long> newMovieIds = newMovieIdsAsInt.stream()
                                                    .map(Integer::longValue)
                                                    .toList();
        
            // Fetch the current list of movies associated with the actor
            List<Long> currentMovieIds = actor.getMovies().stream()
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
                movieService.addActorsToMovie(movieId, List.of(id));
            }
            for (Long movieId : moviesToRemove) {
                movieService.removeActorsFromMovie(movieId, List.of(id));
            }

            // Refresh the actor's movie list
            actor.setMovies(new HashSet<>(movieRepository.findAllById(newMovieIds)));
        }
        return actorRepository.save(actor);
    }
    // DELETE ACTOR
    @Override
    public void deleteActor(Long id, boolean force) {
        // Retrieve the actor or throw ResourceNotFoundException if not found
        Actor actor = actorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Actor not found for id: " + id));
    
        if (force) {
            // Clear relationships and perform force deletion
            actor.getMovies().forEach(movie -> movie.getActors().remove(actor)); // Clear actor from related movies
            actor.getMovies().clear(); // Clear movies from the actor
            actorRepository.save(actor); // Persist relationship changes
            actorRepository.delete(actor); // Delete the actor
        } else {
            // Check if there are related movies before deleting
            if (!actor.getMovies().isEmpty()) {
                throw new BadRequestException(
                    "Cannot delete actor '" + actor.getName() + "' because this actor has " 
                    + actor.getMovies().size() + " associated movies."
                );
            }
            actorRepository.delete(actor); // Perform standard deletion
        }
    }
    // GET ALL ACTORS
    // (pagination )
    // (get actors sorted in alphabetical order )
    @Override
    public List<Actor> getActors(String name, Integer page, Integer size, Boolean abc) {
        if(name != null){
            return actorRepository.findByNameContainingIgnoreCase(name);
        }
        if (page != null && size != null) {
            validatePagination(page, size); // Extracted validation logic
            page = page + 1;
            int offset = (page - 1) * size;
            return actorRepository.findActorsWithPagination(size, offset);
        }
        if (Boolean.TRUE.equals(abc)) {
            return actorRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        }
        if (Boolean.FALSE.equals(abc)) {
            return actorRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
        }
        return actorRepository.findAll(); // Default case: return all actors
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
