package com.example.myspringproject1.controllers;

import com.example.myspringproject1.model.*;
import com.example.myspringproject1.model.enums.Demographic;
import com.example.myspringproject1.model.enums.Format;
import com.example.myspringproject1.model.enums.Genre;
import com.example.myspringproject1.model.enums.PublicationStatus;
import com.example.myspringproject1.repos.ClientRepo;
import com.example.myspringproject1.repos.PublicationRepo;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/publications")
public class PublicationController {

    @Autowired
    private PublicationRepo publicationRepo;

    @Autowired
    private ClientRepo clientRepo;

    @GetMapping("/viewAllPublications")
    public String viewAllPublications(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) Integer year, // We can allow null here to filter by year
            HttpSession session, Model model) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        List<Publication> publications = publicationRepo.findAll();

        if ("Book".equalsIgnoreCase(type)) {
            publications = publications.stream()
                    .filter(publication -> publication instanceof Book)
                    .collect(Collectors.toList());
            model.addAttribute("type", "Book");
        } else if ("Manga".equalsIgnoreCase(type)) {
            publications = publications.stream()
                    .filter(publication -> publication instanceof Manga)
                    .collect(Collectors.toList());
            model.addAttribute("type", "Manga");
        }

        if (status != null && !"All".equalsIgnoreCase(status)) {
            publications = publications.stream()
                    .filter(publication -> publication.getPublicationStatus().name().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        if (author != null && !author.isBlank()) {
            publications = publications.stream()
                    .filter(publication -> publication.getAuthor().equalsIgnoreCase(author))
                    .collect(Collectors.toList());
        }

        if (owner != null && !owner.isBlank()) {
            publications = publications.stream()
                    .filter(publication -> publication.getOwner().getName().equalsIgnoreCase(owner))
                    .collect(Collectors.toList());
        }

        if (year != null) {
            publications = publications.stream()
                    .filter(publication -> publication instanceof Book && ((Book) publication).getPublicationYear() == year)
                    .collect(Collectors.toList());
        }

        model.addAttribute("publications", publications);
        return "allPublications";
    }







    @GetMapping("/addPublication")
    public String showAddPublicationPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (!(loggedInUser instanceof Client)) {
            return "redirect:/publications/viewAllPublications"; // Redirect to a safer page
        }

        model.addAttribute("formats", Format.values());
        model.addAttribute("genres", Genre.values());
        model.addAttribute("demographics", Demographic.values());
        return "addPublication";
    }

    @PostMapping("/addPublication")
    public String addPublication(
            @RequestParam String type,
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) Integer pageCount,
            @RequestParam(required = false) Integer publicationYear,
            @RequestParam(required = false) Format format,
            @RequestParam(required = false) String summary,
            @RequestParam(required = false) String illustrator,
            @RequestParam(required = false) Integer volumeNumber,
            @RequestParam(required = false) Demographic demographic,
            @RequestParam(required = false) Boolean isColor,
            HttpSession session) {
        if (isColor == null) {
            isColor = false;
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !(loggedInUser instanceof Client)) {
            throw new IllegalStateException("Only clients are allowed to add publications.");
        }
        Client owner = (Client) loggedInUser;

        if ("Book".equalsIgnoreCase(type)) {
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setOwner(owner);
            book.setPublisher(publisher);
            book.setIsbn(isbn);
            book.setGenre(genre);
            book.setPageCount(pageCount);
            book.setPublicationYear(publicationYear);
            book.setFormat(format);
            book.setSummary(summary);
            publicationRepo.save(book);
        } else if ("Manga".equalsIgnoreCase(type)) {
            Manga manga = new Manga();
            manga.setTitle(title);
            manga.setAuthor(author);
            manga.setOwner(owner);
            manga.setIllustrator(illustrator);
            manga.setVolumeNumber(volumeNumber);
            manga.setDemographic(demographic);
            manga.setColor(isColor);
            publicationRepo.save(manga);
        }

        return "redirect:/publications/viewAllPublications";
    }
    @PostMapping("/borrow/{id}")
    public String borrowPublication(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in to borrow a publication.");
            return "redirect:/publications/viewAllPublications";
        }
        if (!(loggedInUser instanceof Client)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Only clients can borrow publications.");
            return "redirect:/publications/viewAllPublications";
        }

        Publication publication = publicationRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publication not found"));

        if (publication.getOwner().getId() == loggedInUser.getId()) {
            redirectAttributes.addFlashAttribute("errorMessage", "You cannot reserve your own publication.");
            return "redirect:/publications/viewAllPublications";
        }

        if (publication.getPublicationStatus() == PublicationStatus.RESERVED) {
            redirectAttributes.addFlashAttribute("errorMessage", "This publication is already reserved.");
            return "redirect:/publications/viewAllPublications";
        }

        publication.setPublicationStatus(PublicationStatus.RESERVED);
        publication.setClient((Client) loggedInUser);
        publicationRepo.save(publication);

        Client client = (Client) loggedInUser;
        client.getBorrowedPublications().add(publication);
        clientRepo.save(client);

        redirectAttributes.addFlashAttribute("successMessage", "Publication reserved successfully!");
        return "redirect:/publications/viewAllPublications";
    }

    @Transactional
    @PostMapping("/delete/{id}")
    public String deletePublication(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (!(loggedInUser instanceof Admin)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Only admins can delete publications.");
            return "redirect:/publications/viewAllPublications";
        }

        publicationRepo.deleteByIdCustom(id);
        redirectAttributes.addFlashAttribute("successMessage", "Publication successfully deleted.");
        return "redirect:/publications/viewAllPublications";
    }







}
