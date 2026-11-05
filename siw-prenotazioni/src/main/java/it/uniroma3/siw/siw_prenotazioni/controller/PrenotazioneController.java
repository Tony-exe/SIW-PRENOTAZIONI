package it.uniroma3.siw.siw_prenotazioni.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import it.uniroma3.siw.siw_prenotazioni.exception.AccessoNegatoException;
import it.uniroma3.siw.siw_prenotazioni.exception.FuoriOrarioException;
import it.uniroma3.siw.siw_prenotazioni.exception.PrenotazioneNotFoundException;
import it.uniroma3.siw.siw_prenotazioni.exception.SlotNonDisponibileException;
import it.uniroma3.siw.siw_prenotazioni.model.Prenotazione;
import it.uniroma3.siw.siw_prenotazioni.model.Servizio;
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
        Servizio servizio = servizioService.getServizio(id);
        model.addAttribute("servizio", servizio);
        model.addAttribute("orari", prenotazioneService.getOrariInizio(servizio.getDurata()));
        return "prenotazioni/form";
    }

    @GetMapping("/prenotazioni/new/{servizioId}")
    public String prenotaForm(@PathVariable ("servizioId") Long servizioId, Model model) {
        model.addAttribute("prenotazione", new Prenotazione());
        return mostraForm(model, servizioId);
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
    
    @GetMapping("/prenotazioni")
    public String prenotazioniHome(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("prenotazioni", prenotazioneService.getMiePrenotazioni(utenteCorrente(userDetails)));
        return "prenotazioni/list";
    }
    
    @PostMapping("/prenotazioni/{id}/delete")
    public String delete(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        try {
            prenotazioneService.annulla(id, utenteCorrente(userDetails));
            redirectAttributes.addFlashAttribute("messaggio", "Prenotazione annullata");
        } catch (AccessoNegatoException | PrenotazioneNotFoundException e) {
            redirectAttributes.addFlashAttribute("errore", e.getMessage());
        }
        return "redirect:/prenotazioni";
    }

    @GetMapping("/prenotazioni/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Prenotazione p = prenotazioneService.getPrenotazione(id);
        model.addAttribute("prenotazione", p);
        return mostraForm(model, p.getServizio().getId());
    }
    
    
    @PostMapping("/prenotazioni/{id}/edit")
    public String edit(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes, @Valid @ModelAttribute Prenotazione prenotazione, BindingResult bindingResult, Model model) {
        Long servizioId = prenotazioneService.getPrenotazione(id).getServizio().getId();
        if(bindingResult.hasErrors()){
            prenotazione.setId(id);
            return mostraForm(model, servizioId);
        }try {
            prenotazioneService.modifica(id, prenotazione.getData(), prenotazione.getOra(), utenteCorrente(userDetails));
        } catch (PrenotazioneNotFoundException | SlotNonDisponibileException | FuoriOrarioException | AccessoNegatoException e) {
            bindingResult.rejectValue("ora", "prenotazione.nonValida", e.getMessage());
            prenotazione.setId(id);
            return mostraForm(model, servizioId);
        }
        redirectAttributes.addFlashAttribute("messaggio", "Prenotazione modificata");
        return "redirect:/prenotazioni";
    }
    
}
