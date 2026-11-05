package it.uniroma3.siw.siw_prenotazioni.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.siw_prenotazioni.exception.FuoriOrarioException;
import it.uniroma3.siw.siw_prenotazioni.exception.PrenotazioneNotFoundException;
import it.uniroma3.siw.siw_prenotazioni.exception.SlotNonDisponibileException;
import it.uniroma3.siw.siw_prenotazioni.model.Prenotazione;
import it.uniroma3.siw.siw_prenotazioni.model.Servizio;
import it.uniroma3.siw.siw_prenotazioni.service.PrenotazioneService;
import jakarta.validation.Valid;

// Tutti gli URL iniziano con /admin/ -> accessibili solo all'ADMIN (vedi AuthConfig)
@Controller
public class AdminPrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    public AdminPrenotazioneController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }

    // Mette nel model la materia e le fasce orarie, poi restituisce la form
    private String mostraForm(Model model, Servizio servizio) {
        model.addAttribute("servizio", servizio);
        model.addAttribute("orari", prenotazioneService.getOrariInizio(servizio.getDurata()));
        return "admin/prenotazioni/form";
    }

    @GetMapping("/admin/prenotazioni")
    public String elenco(Model model) {
        model.addAttribute("prenotazioni", prenotazioneService.getTutte());
        return "admin/prenotazioni/list";
    }

    @GetMapping("/admin/prenotazioni/{id}/edit")
    public String modificaForm(@PathVariable("id") Long id, Model model) {
        Prenotazione prenotazione = prenotazioneService.getPrenotazione(id);
        model.addAttribute("prenotazione", prenotazione);
        return mostraForm(model, prenotazione.getServizio());
    }

    @PostMapping("/admin/prenotazioni/{id}/edit")
    public String modifica(@PathVariable("id") Long id,
            @Valid @ModelAttribute("prenotazione") Prenotazione prenotazione, BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes) {
        // la prenotazione arrivata dalla form contiene solo data e ora: il resto lo prendo dal database
        Prenotazione esistente = prenotazioneService.getPrenotazione(id);
        if (bindingResult.hasErrors()) {
            prenotazione.setId(id);
            return mostraForm(model, esistente.getServizio());
        }
        try {
            prenotazioneService.modificaComeAdmin(id, prenotazione.getData(), prenotazione.getOra());
        } catch (SlotNonDisponibileException | FuoriOrarioException e) {
            bindingResult.rejectValue("ora", "prenotazione.nonValida", e.getMessage());
            prenotazione.setId(id);
            return mostraForm(model, esistente.getServizio());
        }
        redirectAttributes.addFlashAttribute("messaggio", "Prenotazione modificata");
        return "redirect:/admin/prenotazioni";
    }

    @PostMapping("/admin/prenotazioni/{id}/delete")
    public String annulla(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            prenotazioneService.annullaComeAdmin(id);
            redirectAttributes.addFlashAttribute("messaggio", "Prenotazione annullata");
        } catch (PrenotazioneNotFoundException e) {
            redirectAttributes.addFlashAttribute("errore", e.getMessage());
        }
        return "redirect:/admin/prenotazioni";
    }
}
