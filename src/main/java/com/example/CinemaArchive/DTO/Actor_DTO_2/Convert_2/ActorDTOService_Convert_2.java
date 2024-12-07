package com.example.CinemaArchive.DTO.Actor_DTO_2.Convert_2;

import com.example.CinemaArchive.DTO.Actor_DTO_2.ActorDTO;
import com.example.CinemaArchive.DTO.Actor_DTO_2.MovieDTO;
import com.example.CinemaArchive.model.Actor;
import java.util.List;
import org.springframework.stereotype.Service;




@Service
public class ActorDTOService_Convert_2 {

    private final MovieDTOService_Convert_2 movieDTOService;

    public ActorDTOService_Convert_2(MovieDTOService_Convert_2 movieDTOService){
        this.movieDTOService = movieDTOService;
    }

    public ActorDTO convertToDTO(Actor actor) {
        if (actor == null) {
            return null;
        }

        List<MovieDTO> movieDTOs = actor.getMovies().stream()
                                        .map(movieDTOService::convertToDTO)
                                        .toList();


        return new ActorDTO(
            actor.getId(), 
            actor.getName(), 
            actor.getBirthDate(), 
            movieDTOs
        );
    }
}
