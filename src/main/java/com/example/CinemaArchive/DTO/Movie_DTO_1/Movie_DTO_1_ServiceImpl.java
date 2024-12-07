package com.example.CinemaArchive.DTO.Movie_DTO_1;

import com.example.CinemaArchive.DTO.Movie_DTO_1.MovieDTO;
import com.example.CinemaArchive.Exception.ResourceNotFoundException;
import com.example.CinemaArchive.repository.MovieRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;




// Paistab et MovieRepository ei saa selles klassis kasutada


@Service
public class Movie_DTO_1_ServiceImpl implements Movie_DTO_1_Service {
    private final MovieRepository movieRepository;


    public Movie_DTO_1_ServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<MovieDTO> getAllMoviesDTO() {
        return movieRepository.findAll()
                              .stream()
                              .map(movie -> {
                                  // Map Actors to ActorDTOs
                                  List<ActorDTO> actorDTOs = movie.getActors()
                                                                  .stream()
                                                                  .map(actor -> new ActorDTO(
                                                                      actor.getId(),
                                                                      actor.getName(), 
                                                                      actor.getBirthDate()
                                                                  ))
                                                                  .collect(Collectors.toList());
                                  // Map Genres to GenreDTOs
                                  List<GenreDTO> genreDTOs = movie.getGenres()
                                                                  .stream()
                                                                  .map(genre -> new GenreDTO(
                                                                      genre.getId(),
                                                                      genre.getName()
                                                                  ))
                                                                  .collect(Collectors.toList());
                                  // Create and return MovieDTO
                                  return new MovieDTO(
                                      movie.getId(),
                                      movie.getTitle(),
                                      movie.getReleaseYear(),
                                      movie.getDuration(),
                                      actorDTOs,
                                      genreDTOs
                                  );
                              })
                              .collect(Collectors.toList());
    }


    @Override
    public MovieDTO getMovieByIdDTO(Long id) {
        return movieRepository.findById(id)
                              .map(movie -> new MovieDTO(
                                  movie.getId(),
                                  movie.getTitle(),
                                  movie.getReleaseYear(),
                                  movie.getDuration(),
                                  movie.getActors()
                                       .stream()
                                       .map(actor -> new ActorDTO(actor.getId(), actor.getName(), actor.getBirthDate()))
                                       .collect(Collectors.toList()),
                                  movie.getGenres()
                                       .stream()
                                       .map(genre -> new GenreDTO(genre.getId(), genre.getName()))
                                       .collect(Collectors.toList())
                              ))
                              .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }



    // @Override
    // public List<MovieDTO> getMoviesByGenreDTO(Long genreId) {
    //     System.out.println(movieRepository.findByGenresId(genreId));
    //     return movieRepository.findByGenresId(genreId).stream().map(movie -> {
    //         // Map Actors to ActorDTOs
    //         List<ActorDTO> actorDTOs = movie.getActors().stream().map(actor -> new ActorDTO(
    //                                             actor.getId(),
    //                                             actor.getName(), 
    //                                             actor.getBirthDate()
    //                                         ))
    //                                         .collect(Collectors.toList());
    //         // Map Genres to GenreDTOs
    //         List<GenreDTO> genreDTOs = movie.getGenres().stream().map(genre -> new GenreDTO(
    //                                             genre.getId(),
    //                                             genre.getName()
    //                                         ))
    //                                         .collect(Collectors.toList());
    //         // Create and return MovieDTO
    //         return new MovieDTO(
    //             movie.getId(),
    //             movie.getTitle(),
    //             movie.getReleaseYear(),
    //             movie.getDuration(),
    //             actorDTOs,
    //             genreDTOs
    //         );
    //     }).collect(Collectors.toList());
    // }
}

