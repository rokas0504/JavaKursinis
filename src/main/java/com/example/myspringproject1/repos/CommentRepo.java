package com.example.myspringproject1.repos;

import com.example.myspringproject1.model.Client;
import com.example.myspringproject1.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByClient(Client client);
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.replies WHERE c.id = :id")
    Optional<Comment> findWithReplies(@Param("id") int id);
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.replies WHERE c.client.id = :clientId")
    List<Comment> findAllByClientWithReplies(@Param("clientId") int clientId);

}
