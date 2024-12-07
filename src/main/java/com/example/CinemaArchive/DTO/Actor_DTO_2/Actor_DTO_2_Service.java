package com.example.CinemaArchive.DTO.Actor_DTO_2;


import java.util.List;

public interface Actor_DTO_2_Service {
    List<ActorDTO> getAllActorsDTO();
    ActorDTO getActorByIdDTO(Long id);
}
