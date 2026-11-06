export interface Insegnante {
    id: number
    nome: string
    cognome: string
    email: string
}

export interface Servizio {
    id: number
    nome: string
    descrizione: string | null
    durata: number
    prezzo: number
    insegnante: Insegnante
}