package com.example.CinemaArchive.repository;

import com.example.CinemaArchive.model.Actor;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {


    List<Actor> findByName(String name);

    List<Actor> findByNameContainingIgnoreCase(String name);

    List<Actor> findByBirthDate(String birthDate);

    List<Actor> findByMoviesId(Long movieId);

    List<Actor> findAll(Sort sort);

    @Query(value = "SELECT * FROM actors LIMIT :size OFFSET :offset", nativeQuery = true)
    List<Actor> findActorsWithPagination(@Param("size") int size, @Param("offset") int offset);

}
