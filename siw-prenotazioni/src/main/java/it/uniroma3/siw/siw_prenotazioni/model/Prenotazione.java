package it.uniroma3.siw.siw_prenotazioni.model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"servizio_id", "data", "ora"}))
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @FutureOrPresent
    @DateTimeFormat (iso = DateTimeFormat.ISO.DATE)
    private LocalDate data;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime ora;

    @ManyToOne
    @JoinColumn (nullable = false)
    private User user;

    @ManyToOne ()
    @JoinColumn (name = "servizio_id", nullable = false)
    private Servizio servizio;

    public Prenotazione() {}

    public Prenotazione(LocalDate data, LocalTime ora){
        this.data = data;
        this.ora = ora;
    }

    /*
    *
    * GETTER 
    * 
    */

    public Long getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getOra() {
        return ora;
    }

    public User getUser() {
        return user;
    }

    public Servizio getServizio() {
        return servizio;
    }

    /*
    *
    * SETTER 
    * 
    */

    public void setId(Long id) {
        this.id = id;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setServizio(Servizio servizio) {
        this.servizio = servizio;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((ora == null) ? 0 : ora.hashCode());
        result = prime * result + ((servizio == null) ? 0 : servizio.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Prenotazione other = (Prenotazione) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        if (ora == null) {
            if (other.ora != null)
                return false;
        } else if (!ora.equals(other.ora))
            return false;
        if (servizio == null) {
            if (other.servizio != null)
                return false;
        } else if (!servizio.equals(other.servizio))
            return false;
        return true;
    }

}
