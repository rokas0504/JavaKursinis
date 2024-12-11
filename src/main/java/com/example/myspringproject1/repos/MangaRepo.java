package com.example.myspringproject1.repos;

import com.example.myspringproject1.model.Manga;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MangaRepo extends CrudRepository<Manga, Long> {
}
