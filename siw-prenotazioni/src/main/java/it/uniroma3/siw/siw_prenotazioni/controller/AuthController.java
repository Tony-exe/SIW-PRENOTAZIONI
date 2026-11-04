package it.uniroma3.siw.siw_prenotazioni.controller;

import it.uniroma3.siw.siw_prenotazioni.service.CredentialsService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.siw_prenotazioni.exception.EmailExistsException;
import it.uniroma3.siw.siw_prenotazioni.exception.UsernameExistsException;
import it.uniroma3.siw.siw_prenotazioni.model.Credentials;
import it.uniroma3.siw.siw_prenotazioni.model.Role;
import it.uniroma3.siw.siw_prenotazioni.model.User;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;




@Controller
public class AuthController {
    
    private final CredentialsService credentialsService;

    AuthController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("credentials", new Credentials());
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult userBindingResult,
            @Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult) {
        if (userBindingResult.hasErrors() || credentialsBindingResult.hasErrors()){
            return "auth/register";
        }
        try {
            credentialsService.registra(user, credentials);
            return "redirect:/login?registrato";
        } catch (UsernameExistsException e) {
            credentialsBindingResult.rejectValue("username", "credentials.duplicate", e.getMessage());
            return "auth/register";
        } catch (EmailExistsException e){
            userBindingResult.rejectValue("email", "user.duplicate", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/success")
    public String success(Authentication authentication) {
        for(GrantedAuthority authority : authentication.getAuthorities()){
            if(authority.getAuthority().equals(Role.ADMIN.name())){
                return "redirect:/admin/servizi";
            }
        }
        return "redirect:/";
    }
    
    
}
