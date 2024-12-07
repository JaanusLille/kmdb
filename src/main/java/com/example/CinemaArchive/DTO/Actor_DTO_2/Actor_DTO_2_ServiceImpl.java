package com.example.CinemaArchive.DTO.Actor_DTO_2;


import com.example.CinemaArchive.Exception.ResourceNotFoundException;
import com.example.CinemaArchive.repository.ActorRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;




@Service
public class Actor_DTO_2_ServiceImpl implements Actor_DTO_2_Service {
    private final ActorRepository actorRepository;

    public Actor_DTO_2_ServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public List<ActorDTO> getAllActorsDTO() {
        return actorRepository.findAll().stream().map(actor -> 
            new ActorDTO(
                actor.getId(),
                actor.getName(),
                actor.getBirthDate(),
                actor.getMovies().stream()
                    .map(movie -> new MovieDTO(movie.getId(), movie.getTitle()))
                    .collect(Collectors.toList())
            )
        ).collect(Collectors.toList());
    }


        @Override
    public ActorDTO getActorByIdDTO(Long id) {
        return actorRepository.findById(id)
                              .map(actor -> new ActorDTO(
                                actor.getId(),
                                actor.getName(),
                                actor.getBirthDate(),
                                actor.getMovies().stream()
                                       .map(movie -> new MovieDTO(movie.getId(), movie.getTitle()))
                                       .collect(Collectors.toList())
                              ))
                              .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
    }



}
