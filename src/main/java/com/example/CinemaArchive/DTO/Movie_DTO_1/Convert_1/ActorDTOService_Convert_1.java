package com.example.CinemaArchive.DTO.Movie_DTO_1.Convert_1;
import com.example.CinemaArchive.DTO.Movie_DTO_1.ActorDTO;
import com.example.CinemaArchive.model.Actor;
import org.springframework.stereotype.Service;

@Service
public class ActorDTOService_Convert_1 {

    public ActorDTO convertToDTO(Actor actor) {
        if (actor == null) {
            return null;
        }
        return new ActorDTO(actor.getId(), actor.getName(), actor.getBirthDate());
    }
}
