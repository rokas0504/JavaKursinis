package com.example.myspringproject1.repos;

import com.example.myspringproject1.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PublicationRepo extends JpaRepository<Publication, Long> {
    @Modifying
    @Query("DELETE FROM Publication p WHERE p.id = :id")
    void deleteByIdCustom(@Param("id") Long id);
}