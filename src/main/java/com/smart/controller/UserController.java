package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    // ✅ Common data
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {

        if (principal == null) {
            return;
        }

        String email = principal.getName();   // email comes from login
        User user = userRepository.getUserByUserEmail(email);

        model.addAttribute("user", user);
    }

    // Dashboard
    @RequestMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "User Dashboard");
        return "normal/user_dashboard";
    }

    // Open add contact form
    @GetMapping("/add-contact-form")
    public String openAddContactForm(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }

    // ✅ Save contact
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact,
                                @RequestParam("profileImage") MultipartFile file,
                                Principal principal,
                                HttpSession session) {

        try {

            String email = principal.getName();
            User user = this.userRepository.getUserByUserEmail(email);

            // 🔴 Fix for NullPointerException
            if (user == null) {
                throw new Exception("User not found (login issue)");
            }

            // File upload
            if (file.isEmpty()) {
                contact.setImage("contact.png");
            } else {
                contact.setImage(file.getOriginalFilename());

                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            contact.setUser(user);
            user.getContacts().add(contact);

            this.userRepository.save(user);

            session.setAttribute("message",
                    new Message("Your contact is added !!", "success"));

        } catch (Exception e) {
            e.printStackTrace();

            session.setAttribute("message",
                    new Message("Something went wrong !!", "danger"));
        }

        return "normal/add_contact_form";
    }

    // Show contacts
    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page,
                               Model model,
                               Principal principal) {

        model.addAttribute("title", "Show User Contacts");

        String email = principal.getName();
        User user = this.userRepository.getUserByUserEmail(email);

        Pageable pageable = PageRequest.of(page, 5);

        Page<Contact> contacts =
                this.contactRepository.findContactsByUser(user.getId(), pageable);

        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contacts.getTotalPages());

        return "normal/show_contacts";
    }

    // Contact detail
    @RequestMapping("/{cId}/contact")
    public String showContactDetail(@PathVariable("cId") Integer cId,
                                   Model model,
                                   Principal principal) {

        Optional<Contact> contactOptional = this.contactRepository.findById(cId);
        Contact contact = contactOptional.get();

        String email = principal.getName();
        User user = this.userRepository.getUserByUserEmail(email);

        if (user.getId() == contact.getUser().getId()) {
            model.addAttribute("contact", contact);
            model.addAttribute("title", contact.getName());
        }

        return "/normal/contact_detail";
    }

    // Delete contact
    @GetMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid") Integer cid,
                                HttpSession session,
                                Principal principal) {

        Contact contact = this.contactRepository.findById(cid)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        String email = principal.getName();
        User user = this.userRepository.getUserByUserEmail(email);

        // security check
        if (user.getId() != contact.getUser().getId()) {
            session.setAttribute("message",
                    new Message("Unauthorized access!", "danger"));
            return "redirect:/user/show-contacts/0";
        }

        // delete image file (optional but good)
        try {
            File file = new ClassPathResource("static/img").getFile();
            File deleteFile = new File(file, contact.getImage());
            if (deleteFile.exists()) {
                deleteFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔥 ACTUAL DELETE FROM DB
        this.contactRepository.delete(contact);

        session.setAttribute("message",
                new Message("Contact deleted successfully", "success"));

        return "redirect:/user/show-contacts/0";
    }

    // Update form
    @GetMapping("/update-contact/{cid}")
    public String updateForm(@PathVariable("cid") Integer cid, Model model) {

        Contact contact = this.contactRepository.findById(cid).get();

        model.addAttribute("contact", contact);
        model.addAttribute("title", "Update Contact");

        return "normal/update_form";
    }

    // Update handler
    @PostMapping("/process-update")
    public String updateHandler(@ModelAttribute Contact contact,
                                @RequestParam("profileImage") MultipartFile file,
                                HttpSession session,
                                Principal principal) {

        try {

            Contact oldContact = this.contactRepository.findById(contact.getcId()).get();

            if (!file.isEmpty()) {

                File deleteFile = new ClassPathResource("static/img").getFile();
                File file1 = new File(deleteFile, oldContact.getImage());
                file1.delete();

                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                contact.setImage(file.getOriginalFilename());

            } else {
                contact.setImage(oldContact.getImage());
            }

            User user = this.userRepository.getUserByUserEmail(principal.getName());

            contact.setUser(user);

            this.contactRepository.save(contact);

            session.setAttribute("message",
                    new Message("Contact updated successfully", "success"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/user/" + contact.getcId() + "/contact";
    }
    
    // Profile
    @GetMapping("/profile")
    public String yourProfile(Model model) {
        model.addAttribute("title", "Profile Page");
        return "normal/profile";
    }
//    @GetMapping("/update-contact/{cid}")
//    public String updateForm() {
//    	return "update_form";
//    }
    
    
   
}