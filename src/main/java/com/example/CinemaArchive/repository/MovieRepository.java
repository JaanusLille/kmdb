package com.example.CinemaArchive.repository;

import com.example.CinemaArchive.model.Movie;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;






@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByTitle(String title);

    List<Movie> findByReleaseYear(Integer releaseYear);

    List<Movie> findByGenresId(Long genreId);

    List<Movie> findByActorsId(Long actorId);

    List<Movie> findByTitleContainingIgnoreCase(String title);

    List<Movie> findAll(Sort sort);

    @Query(value = "SELECT * FROM movies LIMIT :size OFFSET :offset", nativeQuery = true)
    List<Movie> findMoviesWithPagination(@Param("size") int size, @Param("offset") int offset);
}

