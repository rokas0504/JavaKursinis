package com.example.myspringproject1.controllers;

import com.example.myspringproject1.model.Admin;
import com.example.myspringproject1.model.Client;
import com.example.myspringproject1.model.User;
import com.example.myspringproject1.repos.ClientRepo;
import com.example.myspringproject1.repos.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ClientRepo clientRepo;

    @GetMapping("/loginPage")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            Model model) {
        User user = userRepo.findByLoginAndPassword(username, password);

        if (user == null) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        if (user instanceof Admin) {
            return "redirect:/users/viewAllUsers";
        } else if (user instanceof Client) {
            return "redirect:/users/clientHomePage"; // Create a client-specific home page.
        }

        return "login";
    }
    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "redirect:/users/loginPage";
    }

    @GetMapping("/viewAllUsers")
    public String viewAllUsers(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "allUsers";
    }


    @GetMapping("/createPage")
    public String showCreateUserPage() {
        return "createUser";
    }

    @PostMapping("/create")
    public String createUser(@RequestParam String type,
                             @RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String name,
                             @RequestParam String surname,
                             @RequestParam(required = false) String address,
                             @RequestParam(required = false) String birthDate,
                             @RequestParam(required = false) String clientBio,
                             @RequestParam(required = false) String phoneNum) {
        if (type.equals("Client")) {
            Client newClient = new Client(username, password, name, surname, address);
            if (birthDate != null && !birthDate.isEmpty()) {
                newClient.setBirthDate(LocalDate.parse(birthDate));
            }
            newClient.setClientBio(clientBio);
            clientRepo.save(newClient);
        } else if (type.equals("Admin")) {
            Admin newAdmin = new Admin(username, password, name, surname, phoneNum);
            userRepo.save(newAdmin);
        }
        return "redirect:/users/viewAllUsers";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID:" + id));
        model.addAttribute("user", user);
        model.addAttribute("isAdmin", user instanceof Admin); // Add attribute to determine if user is Admin
        return "editUser";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String name,
                             @RequestParam String surname,
                             @RequestParam(required = false) String address,
                             @RequestParam(required = false) LocalDate birthDate,
                             @RequestParam(required = false) String clientBio,
                             @RequestParam(required = false) String phoneNum) {

        User user = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID:" + id));

        user.setLogin(username);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);

        if (user instanceof Client) {
            Client client = (Client) user;
            client.setAddress(address);
            client.setBirthDate(birthDate);
            client.setClientBio(clientBio);
        } else if (user instanceof Admin) {
            Admin admin = (Admin) user;
            admin.setPhoneNum(phoneNum);
        }

        userRepo.save(user);
        return "redirect:/users/viewAllUsers";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepo.deleteById(id);
        return "redirect:/users/viewAllUsers";
    }

}
