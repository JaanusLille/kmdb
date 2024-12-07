package com.example.CinemaArchive.service;

import com.example.CinemaArchive.model.Actor;
import java.util.List;
import java.util.Map;
import java.util.Optional;



public interface ActorService {
    Actor saveActor(Actor actor);         // Create or update an actor
    Optional<Actor> getActorById(Long id); // Retrieve a specific actor by ID
    Actor updateActor(Long id, Map<String, Object> updates);
    public List<Actor> getActors(String name, Integer page, Integer size, Boolean abc);
    void deleteActor(Long id, boolean force); // Delete an actor by ID
}
