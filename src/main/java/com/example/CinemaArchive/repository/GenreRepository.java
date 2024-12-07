package com.example.CinemaArchive.repository;

import com.example.CinemaArchive.model.Genre;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;




@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    // Custom query methods can be added here if needed

    List<Genre> findByName(String name);

    List<Genre> findAll(Sort sort);

    @Query(value = "SELECT * FROM genres LIMIT :size OFFSET :offset", nativeQuery = true)
    List<Genre> findGenresWithPagination(@Param("size") int size, @Param("offset") int offset);
    
}
