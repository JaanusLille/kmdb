package com.example.CinemaArchive.controller;

import com.example.CinemaArchive.DTO.Actor_DTO_2.ActorDTO;
import com.example.CinemaArchive.DTO.Actor_DTO_2.Convert_2.ActorDTOService_Convert_2;
import com.example.CinemaArchive.DTO.Movie_DTO_1.Convert_1.MovieDTOService_Convert_1;
import com.example.CinemaArchive.DTO.Movie_DTO_1.MovieDTO;
import com.example.CinemaArchive.DTO.Movie_DTO_1.Movie_DTO_1_Service;
import com.example.CinemaArchive.service.MovieService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final Movie_DTO_1_Service movieDTOService;
    private final MovieDTOService_Convert_1 movieDTOServiceConvert_1;
    private final ActorDTOService_Convert_2 actorDTOServiceConvert_2;

    public MovieController(
                            MovieService movieService, 
                            Movie_DTO_1_Service movieDTOService, 
                            MovieDTOService_Convert_1 movieDTOServiceConvert_1, 
                            ActorDTOService_Convert_2 actorDTOServiceConvert_2
                                                                                ) {
        this.movieService = movieService;
        this.movieDTOService = movieDTOService;
        this.movieDTOServiceConvert_1 = movieDTOServiceConvert_1;
        this.actorDTOServiceConvert_2 = actorDTOServiceConvert_2;
    }
    // GET MOVIE BY ID
    @GetMapping("/{id}")
    public MovieDTO getMovieById(@PathVariable Long id) {
        return movieDTOService.getMovieByIdDTO(id);
    }
    // CREATE MOVIE
    @PostMapping
    public ResponseEntity<?> createMovie(@Valid @RequestBody Map<String, Object> movieData) {
        if (!movieData.containsKey("title") || movieData.get("title") == null) {
            return ResponseEntity.badRequest().body(Map.of("title", "title cannot be null"));
        }
        if (!movieData.containsKey("releaseYear") || movieData.get("releaseYear") == null) {
            return ResponseEntity.badRequest().body(Map.of("releaseYear", "Release year cannot be null"));
        }
        if (!movieData.containsKey("duration") || movieData.get("duration") == null) {
            return ResponseEntity.badRequest().body(Map.of("duration", "duration cannot be null"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(movieDTOService.getMovieByIdDTO((movieService.createMovie(movieData)).getId()));
    }
    // UPDATE MOVIE
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @Valid @RequestBody Map<String, Object> updates) {
        movieService.updateMovie(id, updates);
        try {
            return ResponseEntity.ok(movieDTOService.getMovieByIdDTO(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // Add actors to movie
    @PostMapping("/{movieId}/actors")
    public ResponseEntity<String> addActorsToMovie(@PathVariable Long movieId, @Valid @RequestBody List<Long> actorIds) {
        movieService.addActorsToMovie(movieId, actorIds);
        return ResponseEntity.ok("Actors added to movie successfully.");
    }
    // Remove actors from movie
    @DeleteMapping("/{movieId}/actors")
    public ResponseEntity<String> removeActorsFromMovie(@PathVariable Long movieId, @RequestBody List<Long> actorIds) {
        movieService.removeActorsFromMovie(movieId, actorIds);
        return ResponseEntity.ok("Actors removed from movie successfully.");
    }
    // Add genres to movie
    @PostMapping("/{movieId}/genres")
    public ResponseEntity<String> addGenresToMovie(@PathVariable Long movieId, @Valid @RequestBody List<Long> genreIDs) {
        movieService.addGenresToMovie(movieId, genreIDs);
        return ResponseEntity.ok("Genres added to movie successfully.");
    }
    // Remove genres from movie
    @DeleteMapping("/{movieId}/genres")
    public ResponseEntity<String> removeGenresFromMovie(@PathVariable Long movieId, @RequestBody List<Long> genreIds) {
        movieService.removeGenresFromMovie(movieId, genreIds);
        return ResponseEntity.ok("Genres removed from movie successfully.");
    }
    // GET ALL MOVIES
    // (get movies by genreId )
    // (get movies by releaseYear )
    // (get movies by actorId )
    // (pagination )
    // (get movies sorted in alphabetical order )
    @GetMapping
    public List<MovieDTO> getMovies(
            @RequestParam(required = false) Long genre, 
            @RequestParam(required = false) Integer year, 
            @RequestParam(required = false) Long actor,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Boolean abc) {
        
        return movieService.getMovies(genre, year, actor, page, size, abc).stream()
                .map(movieDTOServiceConvert_1::convertToDTO)
                .toList();
    }
    // FIND MOVIES BY MOVIE TITLE
    @GetMapping("/search")
    public List<MovieDTO> searchMoviesByTitle(@RequestParam(required = false) String title) {
        return movieService.searchMoviesByTitle(title).stream()
                .map(movieDTOServiceConvert_1::convertToDTO)
                .toList();
    }
    // GET ALL ACTORS IN MOVIE BY MOVIE ID
    @GetMapping("/{movieId}/actors")
    public List<ActorDTO> getActorsByMovieId(@PathVariable Long movieId) {
        return movieService.getActorsByMovieId(movieId).stream()
                .map(actorDTOServiceConvert_2::convertToDTO)
                .toList();
    }
    // DELETE MOVIE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean force) {
        movieService.deleteMovie(id, force);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
// ./mvnw clean install && ./mvnw spring-boot:run
