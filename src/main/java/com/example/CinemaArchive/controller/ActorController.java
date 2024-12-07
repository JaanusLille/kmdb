package com.example.CinemaArchive.controller;

import com.example.CinemaArchive.DTO.Actor_DTO_2.ActorDTO;
import com.example.CinemaArchive.DTO.Actor_DTO_2.Actor_DTO_2_Service;
import com.example.CinemaArchive.DTO.Actor_DTO_2.Convert_2.ActorDTOService_Convert_2;
import com.example.CinemaArchive.model.Actor;
import com.example.CinemaArchive.service.ActorService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/actors")
public class ActorController {

    private final ActorService actorService;
    private final Actor_DTO_2_Service actorDTOService;
    private final ActorDTOService_Convert_2 actorDTOService_Convert_2;

    public ActorController(ActorService actorService, Actor_DTO_2_Service actorDTOService, ActorDTOService_Convert_2 actorDTOService_Convert_2) {
        this.actorService = actorService;
        this.actorDTOService = actorDTOService;
        this.actorDTOService_Convert_2 = actorDTOService_Convert_2;
    }
    // GET ACTOR BY ID
    @GetMapping("/{id}")
    public ActorDTO getActorById(@PathVariable Long id) {
        return actorDTOService.getActorByIdDTO(id);
    }
    // CREATE ACTOR
    @PostMapping
    public ResponseEntity<?> createActor(@Valid @RequestBody Actor actor) {
        try {
            Actor savedActor = actorService.saveActor(actor);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedActor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // UPDATE ACTOR
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateActor(@PathVariable Long id, @Valid @RequestBody Map<String, Object> updates) {
        actorService.updateActor(id, updates);
        try {
            // return ResponseEntity.ok(updatedActor);
            return ResponseEntity.ok(actorDTOService.getActorByIdDTO(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // DELETE ACTOR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean force) {
        actorService.deleteActor(id, force);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
    // GET ALL ACTORS
    // (Find Actor by name of Actor; is not case sensitive)
    // (pagination )
    // (get actors sorted in alphabetical order )
    @GetMapping
    public List<ActorDTO> getActors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Boolean abc) {
        
        return actorService.getActors(name, page, size, abc).stream()
                .map(actorDTOService_Convert_2::convertToDTO)
                .toList();
    }
}
