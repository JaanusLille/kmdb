# CinemaArchive 

## Overview

**CinemaArchive** is a Spring Boot application for managing a collection of movies, actors, and genres, designed with scalability and flexibility in mind. The application supports a variety of operations, including searching, filtering, and CRUD operations on its entities. It leverages SQLite as its database and includes clean DTO-based API responses.

---

## Features

### Core Functionality:
- **Movies**: Manage a catalog of movies, including details like title, release year, and duration.
- **Actors**: Manage actor profiles, including names and birth dates.
- **Genres**: Manage movie genres, enabling movies to belong to multiple genres.

### Advanced Features:
- **Search**: Search movies by title.
- **Filtering**: Filter movies by genres, release year, or actors.
- **Pagination**: Paginate and sort movies and genres.
- **Relationships**:
  - Many-to-many relationships between movies and actors.
  - Many-to-many relationships between movies and genres.

### Extra Functionality:
- **List alphabetically**: List entities alphabetically by ascending or descending order.

---

## Setup Instructions

### Prerequisites:
1. Java 17+ installed.
2. Maven installed.
3. SQLite installed.

### Steps:
1. Clone the repository:
    ```bash
    git clone <repository-url>
    cd CinemaArchive
    ```
2. Build the application:
    ```bash
    mvn clean install
    ```
3. Run the application:
    ```bash
    mvn spring-boot:run
    ```
4. Use an API client like [Postman](https://www.postman.com/) or cURL to interact with the application.

### Database Setup:
The application uses SQLite. The database is automatically configured using `application.properties`. If needed, reinitialize the database using the provided schema:

- **Schema for Entities**:
    ```sql
    CREATE TABLE actors (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        birth_date DATE NOT NULL
    );

    CREATE TABLE genres (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL
    );

    CREATE TABLE movies (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        title TEXT NOT NULL,
        release_year INTEGER NOT NULL,
        duration TEXT
    );


    CREATE TABLE movies_actors (
        movie_id INTEGER NOT NULL,
        actor_id INTEGER NOT NULL,
        FOREIGN KEY (movie_id) REFERENCES movies(id),
        FOREIGN KEY (actor_id) REFERENCES actor(id),
        PRIMARY KEY (movie_id, actor_id));


    CREATE TABLE movies_genres (
        movie_id INTEGER NOT NULL,
        genre_id INTEGER NOT NULL,
        FOREIGN KEY (movie_id) REFERENCES movies(id),
        FOREIGN KEY (genre_id) REFERENCES genres(id),
        PRIMARY KEY (movie_id, genre_id));

    ```

---

## API Documentation

### Base URL:
- `http://localhost:8080`


Refer to the `application.properties` file to configure or customize the application.

---

## ENDPOINTS

### Get all entities
- **Get all actors**: 
  - `GET /api/actors`
- **Get all movies**: 
  - `GET /api/movies`
- **Get all genres**: 
  - `GET /api/genres`

---

### Get specific entity by Id
- **Get actor by Id**: 
  - `GET /api/actors/{id}`
- **Get movie by Id**: 
  - `GET /api/movies/{id}`
- **Get genre by Id**: 
  - `GET /api/genres/{id}`



---

### Create new entity
---
#### Create new actor:
#### POST /api/actors

```json
{
    "name": "Priit Võigemast",
    "birthDate": "1980-04-18"
}
```

#### Create new genre:
#### POST /api/genres

```json
{
    "name": "war"
}
```

#### Create new movie:
#### POST /api/movies

```json
{
    "title": "Nimed Marmortahvlil",
    "releaseYear": 2002,
    "duration": "1h-28m"
}
```

#### If actor(s) and/or genre(s) already exists, create movie and link it with actor(s) and/or genre(s) by Id: 
#### POST /api/movies

```json
{
    "title": "Nimed Marmortahvlil",
    "releaseYear": 2002,
    "duration": "1h-28m",
    "actors": [18, 19],
    "genres": [1, 2, 18, 19, 20]
}
```

---

### Update entity
---
#### Update actor:
#### PATCH /api/actors/15

```json
{
    "name": "Kaarel Kurg",
    "birthDate": "1929-10-11",
    "movies": [50, 51, 52, 53]
}
```
#### You can update the actors name, birthDate and the movie(s) it is associated with. 
---
#### Update genre:
#### PATCH /api/genres/22

```json
{    
    "name": "war epic",
    "movies": [47, 48]
}
```
#### You can update the genres name and the movie(s) it is associated with. 
---
#### Update movie:
#### PATCH /api/movies/68

```json
{
    "title": "Nimed Marmortahvlil",
    "releaseYear": 2002,
    "duration": "1h-28m",
    "actors": [18, 19],
    "genres": [20, 1, 2, 18]
 }
```
#### You can update the movies title, releaseYear, duration and the actors and genres it is associated with. 

---
- **Delete**: 
  - `DELETE /api/actors/{id}`
  - `DELETE /api/genres/{id}`
  - `DELETE /api/movies/{id}`

If an entity has one or more associations, you must use "Force delete". 
"Force delete" will delete the entity and it's associations. 

---
- **Force delete**: 
  - `DELETE /api/actors/{id}?force=true`
  - `DELETE /api/genres/{id}?force=true`
  - `DELETE /api/movies/{id}?force=true`
---
- **Add actors to movie**: 
  - `POST /api/movies/{id}/actors`
---
- **Remove actors from movie**: 
  - `DELETE /api/movies/{id}/actors`
---
- **Add genres to movie**: 
  - `POST /api/movies/{id}/genres`
---
- **Remove genres from movie**: 
  - `DELETE /api/movies/{id}/genres`
---
### Filtering
- **Retrieve movies filtered by genre**: 
  - `GET /api/movies?genre={genreId}`
- **Retrieve movies filtered by release year**: 
  - `GET /api/movies?year={releaseYear}`
- **Retrieve movies that the actor with the given id has starred in**: 
  - `GET /api/movies?actors={Actor.id}`
- **Retrieve all actors starring in a movie**: 
  - `GET /api/movies/{movieId}/actors`
- **Retrieve movies filtered by title**: 
  - `GET /api/movies/search?title={title}`
- **Retrieve actors filtered by name**: 
  - `GET /api/actors?name={name}`
---

### Pagination

- **Get results by page**: 
  - `GET /api/actors?page={page}&size={size}`
  - `GET /api/genres?page={page}&size={size}`
  - `GET /api/movies?page={page}&size={size}`

---


### EXTRAS: 
- **Get results in alphabetical order**: 
  - `GET /api/actors?abc=true`
  - `GET /api/genres?abc=true`
  - `GET /api/movies?abc=true`

- **Get results in reverse alphabetical order**: 
  - `GET /api/actors?abc=false`
  - `GET /api/genres?abc=false`
  - `GET /api/movies?abc=false`

---
