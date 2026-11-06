import type { Servizio } from "../types"
import api from "./api"

export async function getServizi(): Promise<Servizio[]> {
    try {
        const { data } = await api.get<Servizio[]>('/servizi')
        return data
    } catch (error) {
        throw new Error('Impossibile caricare le materie', { cause: error })
    }
}