package com.example.CinemaArchive.controller;

import com.example.CinemaArchive.DTO.Genre_DTO_3.Convert_3.GenreDTOService_Convert_3;
import com.example.CinemaArchive.DTO.Genre_DTO_3.GenreDTO;
import com.example.CinemaArchive.DTO.Genre_DTO_3.Genre_DTO_3_Service;
import com.example.CinemaArchive.model.Genre;
import com.example.CinemaArchive.service.GenreService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;
    private final Genre_DTO_3_Service genreDTOService;
    private final GenreDTOService_Convert_3 genreDTOService_Convert_3;

    public GenreController(GenreService genreService, Genre_DTO_3_Service genreDTOService, GenreDTOService_Convert_3 genreDTOService_Convert_3) {
        this.genreService = genreService;
        this.genreDTOService = genreDTOService;
        this.genreDTOService_Convert_3 = genreDTOService_Convert_3;
    }
    // GET GENRE BY ID
    @GetMapping("/{id}")
    public GenreDTO getGenreById(@PathVariable Long id) {
        return genreDTOService.getGenreByIdDTO(id);
    }
    // CREATE GENRE
    @PostMapping
    public ResponseEntity<Genre> createGenre(@Valid @RequestBody Genre genre) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.saveGenre(genre));
    }
    // UPDATE GENRE
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable Long id, @Valid @RequestBody Map<String, Object> updates) {
        genreService.updateGenre(id, updates);
        try {
            return ResponseEntity.ok(genreDTOService.getGenreByIdDTO(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // DELETE GENRE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean force) {
        genreService.deleteGenre(id, force);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
    // GET ALL GENRES
    // (pagination )
    // (get genres sorted in alphabetical order )
    @GetMapping    
    public List<GenreDTO> getGenres(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Boolean abc) {
        
        return genreService.getGenres(page, size, abc).stream()
                .map(genreDTOService_Convert_3::convertToDTO)
                .toList();
    }
}
