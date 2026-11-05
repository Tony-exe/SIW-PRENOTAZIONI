package it.uniroma3.siw.siw_prenotazioni.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import it.uniroma3.siw.siw_prenotazioni.exception.FuoriOrarioException;
import it.uniroma3.siw.siw_prenotazioni.exception.SlotNonDisponibileException;
import it.uniroma3.siw.siw_prenotazioni.model.Prenotazione;
import it.uniroma3.siw.siw_prenotazioni.model.User;
import it.uniroma3.siw.siw_prenotazioni.service.CredentialsService;
import it.uniroma3.siw.siw_prenotazioni.service.PrenotazioneService;
import it.uniroma3.siw.siw_prenotazioni.service.ServizioService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
public class PrenotazioneController {
    private final PrenotazioneService prenotazioneService;
    private final ServizioService servizioService;
    private final CredentialsService credentialsService;

    public PrenotazioneController(PrenotazioneService prenotazioneService, ServizioService servizioService,
            CredentialsService credentialsService) {
        this.prenotazioneService = prenotazioneService;
        this.servizioService = servizioService;
        this.credentialsService = credentialsService;
    }

    private User utenteCorrente(UserDetails userDetails){
        return credentialsService.getCredentials(userDetails.getUsername()).getUser();
    }

    private String mostraForm(Model model, Long id){
        model.addAttribute("servizio", servizioService.getServizio(id));
        return "prenotazioni/form";
    }

    @GetMapping("/prenotazioni/new/{servizioId}")
    public String prenotaForm(@PathVariable ("servizioId") Long servizioId, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("servizio", servizioService.getServizio(servizioId));
        model.addAttribute("prenotazione", new Prenotazione());
        return "prenotazioni/form";
    }

    @PostMapping("/prenotazioni/new/{servizioId}")
    public String prenota(@PathVariable("servizioId") Long servizioId, @Valid @ModelAttribute Prenotazione prenotazione, BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            return mostraForm(model, servizioId);
        }
        try {
            prenotazioneService.prenota(prenotazione, servizioId, utenteCorrente(userDetails));
        } catch (SlotNonDisponibileException | FuoriOrarioException e) {
            bindingResult.rejectValue("ora", "prenotazione.nonValida", e.getMessage());
            return mostraForm(model, servizioId);
        }
        redirectAttributes.addFlashAttribute("messaggio", "Prenotazione confermata");
        return "redirect:/prenotazioni";
    }
    
    
}
