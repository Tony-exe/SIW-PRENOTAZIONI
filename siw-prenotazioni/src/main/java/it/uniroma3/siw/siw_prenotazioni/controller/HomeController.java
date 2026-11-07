package it.uniroma3.siw.siw_prenotazioni.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.siw_prenotazioni.service.CredentialsService;

@Controller
public class HomeController {

    private final CredentialsService credentialsService;

    public HomeController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // la home è pubblica: userDetails è null se nessuno ha fatto il login
        if (userDetails != null) {
            // dallo username ricavo il User, che contiene nome e cognome
            model.addAttribute("utente", credentialsService.getCredentials(userDetails.getUsername()).getUser());
        }
        return "index";
    }

}
