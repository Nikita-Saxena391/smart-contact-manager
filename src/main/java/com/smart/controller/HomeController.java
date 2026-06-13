package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@GetMapping("/")
	public String home(Model m) {
	m.addAttribute("title","Home - Smart Contact Manager");
		return "home";
	}
	@GetMapping("/about")
	public String about(Model m) {
		m.addAttribute("title","About - Smart Contact Manager");
		return "about";
		}
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("message", null);
		return "signup";
	}
	@PostMapping("/do-register")
	public String handleRegistration(@Valid @ModelAttribute("user") User user, BindingResult result1, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model, HttpSession session) {
		try {
			if(!agreement) {
				System.out.println("You haven't agreed terms and conditions...");
				throw new Exception("You haven't agreed terms and conditions...");
			}
			if(result1.hasErrors()) {
				System.out.println("Something went wrong spring starter validation...");
				System.out.println(result1);
				model.addAttribute("user", user);
				return "signup";
			}
			user.setEnabled(true);
			user.setRole("ROLE_USER");
			user.setImageUrl("default.webp");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
	User result = userRepository.save(user);
	System.out.println("User : " + result);
			model.addAttribute("user",new User());
	model.addAttribute("message", new Message("Successfully registered... " ,"alert-primary"));
		}catch(Exception e) {
			System.out.println(e.getMessage());
			model.addAttribute("user",user);
		model.addAttribute("message", new Message("Something went wrong !!! " + e.getMessage(),"alert-danger"));
		}
		return "signup";	
	}
	//handler for custom Login
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title","Login Page");
		return "login.html";
	}
//	@Postmapping("/update-contact/{cid}")
//	
	
	
	

}
