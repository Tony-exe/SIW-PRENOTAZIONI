package it.uniroma3.siw.siw_prenotazioni.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {
    
    @ModelAttribute("userDetails")
    public UserDetails getUser(){
        UserDetails user = null; 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken)){
            user = (UserDetails) authentication.getPrincipal();
        }
        return user;
    }
}
