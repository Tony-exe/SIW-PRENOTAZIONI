package it.uniroma3.siw.siw_prenotazioni.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
public class Servizio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column (unique = true, nullable = false)
    private String nome;

    @Size (max = 3000)
    @Column (length = 3000)
    private String descrizione; /* CAMPO FACOLTATIVO */

    @NotNull 
    @Min(1)
    private Integer durata;

    @NotNull
    @PositiveOrZero
    private Float prezzo;

    public Servizio() {}
    
    public Servizio(String nome, String descrizione, Integer durata, Float prezzo) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.durata = durata;
        this.prezzo = prezzo;
    }
    
    /*
    *
    * GETTER 
    * 
    */
    
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Integer getDurata() {
        return durata;
    }

    public Float getPrezzo() {
        return prezzo;
    }

    /*
    *
    * SETTER 
    * 
    */
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setDurata(Integer durata) {
        this.durata = durata;
    }

    public void setPrezzo(Float prezzo) {
        this.prezzo = prezzo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
        Servizio other = (Servizio) obj;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        return true;
    }

}
