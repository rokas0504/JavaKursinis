package com.example.myspringproject1.controllers;

import com.example.myspringproject1.model.Client;
import com.example.myspringproject1.model.Comment;
import com.example.myspringproject1.model.User;
import com.example.myspringproject1.repos.ClientRepo;
import com.example.myspringproject1.repos.CommentRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CommentController {

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private CommentRepo commentRepo;

    private void logCommentHierarchy(List<Comment> comments, int level) {
        String indent = "  ".repeat(level);
        for (Comment comment : comments) {
            System.out.println(indent + "Comment ID: " + comment.getId() + ", Body: " + comment.getBody());
            if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
                logCommentHierarchy(comment.getReplies(), level + 1);
            }
        }
    }

    @GetMapping("/view/{clientId}")
    public String viewClient(@PathVariable int clientId, Model model) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        List<Comment> comments = commentRepo.findAllByClientWithReplies(clientId);
        logCommentHierarchy(comments, 0);  // Debug log

        client.setCommentList(comments);
        model.addAttribute("client", client);

        return "viewClient";
    }


    @PostMapping("/comments/add")
    public String addComment(@RequestParam String body, HttpSession session, RedirectAttributes attributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (!(loggedInUser instanceof Client)) {
            attributes.addFlashAttribute("error", "Only clients can add comments.");
            return "redirect:/";
        }
        Client client = (Client) loggedInUser;
        Comment newComment = new Comment("New Comment", body, client, client);
        commentRepo.save(newComment);
        return "redirect:/view/" + client.getId();
    }

    @PostMapping("/comments/reply")
    public String replyToComment(@RequestParam int parentId, @RequestParam String body, HttpSession session, RedirectAttributes attributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (!(loggedInUser instanceof Client)) {
            attributes.addFlashAttribute("error", "Only clients can reply to comments.");
            return "redirect:/";
        }
        Client client = (Client) loggedInUser;

        Comment parentComment = commentRepo.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));

        Comment reply = new Comment("Reply", body, parentComment, client);
        parentComment.getReplies().add(reply);

        commentRepo.save(parentComment);

        return "redirect:/view/" + parentComment.getCommentOwner().getId();
    }



}
