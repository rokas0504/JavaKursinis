package com.example.myspringproject1.controllers;

import com.example.myspringproject1.model.Client;
import com.example.myspringproject1.model.Comment;
import com.example.myspringproject1.model.Publication;
import com.example.myspringproject1.model.User;
import com.example.myspringproject1.model.enums.PublicationStatus;
import com.example.myspringproject1.repos.ClientRepo;
import com.example.myspringproject1.repos.PublicationRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private PublicationRepo publicationRepo;

    @GetMapping("/profile")
    public String showClientProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null || !(loggedInUser instanceof Client)) {
            return "redirect:/";
        }

        // Reload the Client object to ensure data is up to date
        Client client = clientRepo.findById(loggedInUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
        session.setAttribute("loggedInUser", client); // Update the session with the refreshed client

        List<Publication> clientPublications = client.getOwnedPublications();
        List<Publication> borrowedPublications = client.getBorrowedPublications();

        model.addAttribute("client", client);
        model.addAttribute("clientPublications", clientPublications);
        model.addAttribute("borrowedPublications", borrowedPublications);

        return "clientProfile";
    }


    @PostMapping("/delete/{publicationId}")
    @Transactional
    public String deletePublication(@PathVariable Long publicationId) {
        publicationRepo.deleteByIdCustom(publicationId);
        return "redirect:/clients/profile";
    }





    @PostMapping("/unreserve/{publicationId}")
    public String unreservePublication(@PathVariable Long publicationId, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null || !(loggedInUser instanceof Client)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in as a client to unreserve a publication.");
            return "redirect:/";
        }

        Publication publication = publicationRepo.findById(publicationId)
                .orElseThrow(() -> new IllegalArgumentException("Publication not found"));

        Client client = (Client) loggedInUser;

        if (publication.getClient() == null || publication.getClient().getId() != client.getId()) {
            redirectAttributes.addFlashAttribute("errorMessage", "You cannot unreserve a publication you did not reserve.");
            return "redirect:/publications/viewAllPublications";
        }

        publication.setPublicationStatus(PublicationStatus.AVAILABLE);
        publication.setClient(null);
        System.out.println("Setting publication status to: " + publication.getPublicationStatus());
        publicationRepo.saveAndFlush(publication);

        client.getBorrowedPublications().removeIf(pub -> pub.getId() == publicationId);
        clientRepo.save(client);

        Publication updatedPublication = publicationRepo.findById(publicationId)
                .orElseThrow(() -> new IllegalArgumentException("Publication not found"));
        System.out.println("Publication status after saving: " + updatedPublication.getPublicationStatus());

        return "redirect:/clients/profile";
    }

    @GetMapping("/my-comments")
    public String viewMyComments(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null || !(loggedInUser instanceof Client)) {
            return "redirect:/";
        }

        Client client = (Client) loggedInUser;

        List<Comment> myComments = client.getMyComments();

        model.addAttribute("client", client);
        model.addAttribute("comments", myComments);

        return "viewClient";
    }



}
