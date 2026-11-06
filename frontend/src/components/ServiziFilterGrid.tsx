import { useEffect, useState } from 'react'
import {
  Alert, Box, Button, Card, CardActions, CardContent,
  Chip,
  CircularProgress, FormControl, Grid, InputLabel, MenuItem, Select, Slider, Stack, Typography,
} from '@mui/material'
import type { Servizio } from '../types'
import { getServizi } from '../services/servizioService'

function ServiziFilterGrid() {
  // --- STATO: i dati che, quando cambiano, fanno ridisegnare il componente ---
  const [servizi, setServizi] = useState<Servizio[]>([])   // le materie (all'inizio nessuna)
  const [loading, setLoading] = useState(true)              // sto ancora caricando?
  const [errore, setErrore] = useState<string | null>(null) // messaggio di errore, se c'è
  const [insegnanteSel, setInsegnanteSel] = useState< number | null>(null)
  const [rangePrezzo, setRangePrezzo] = useState<number[]>([0,0])
  const [ordinamento, setOrdinamento] = useState('nome-asc')

  // --- CARICAMENTO: eseguito UNA volta, quando il componente compare ([] = nessuna dipendenza) ---
  useEffect(() => {
    // useEffect non accetta una funzione async, quindi la definisco qui dentro...
    async function carica() {
      try {       
        const dati = await getServizi()
        setServizi(dati)
        if(dati.length > 0){
            const prezzi = dati.map((s) => s.prezzo)
            setRangePrezzo([Math.min(...prezzi), Math.max(...prezzi)])
        }
      } catch (e) {
        setErrore((e as Error).message)       // il messaggio creato nel service
      } finally {
        setLoading(false)                     // in ogni caso ho finito di caricare
      }
    }
    carica()                                  // ...e poi la chiamo
  }, [])

  // --- COSA MOSTRARE: la funzione restituisce una cosa diversa a seconda dello stato ---
  if (loading) return <CircularProgress />
  if (errore) return <Alert severity="error">{errore}</Alert>

  const prezzi = servizi.map((s) => s.prezzo)
  const prezzoMin = Math.min(...prezzi)
  const prezzoMax = Math.max(...prezzi)

  const insegnanti = servizi.map((s) => s.insegnante).filter((ins, i, arr) => arr.findIndex((x) => x.id === ins.id) === i)

  const filtrati = servizi
  .filter((s) => insegnanteSel === null || s.insegnante.id === insegnanteSel)
  .filter((s) => s.prezzo >= rangePrezzo [0] && s.prezzo <= rangePrezzo[1])
  .sort((a, b) => {
    switch (ordinamento) {
        case 'nome-asc': return a.nome.localeCompare(b.nome)
        case 'nome-desc': return b.nome.localeCompare(a.nome)
        case 'prezzo-asc': return a.prezzo - b.prezzo
        default: return b.prezzo - a.prezzo //prezzo - desc
    }
  })

  return (
    <Stack spacing={3}>
    {/* CHIP INSEGNANTI: esclusivi, ricliccare quello attivo torna a "Tutti" */}
    <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
      <Chip label="Tutti"
        color={insegnanteSel === null ? 'primary' : 'default'}
        onClick={() => setInsegnanteSel(null)} />
      {insegnanti.map((ins) => (
        <Chip key={ins.id} label={`${ins.nome} ${ins.cognome}`}
          color={insegnanteSel === ins.id ? 'primary' : 'default'}
          onClick={() => setInsegnanteSel(insegnanteSel === ins.id ? null : ins.id)} />
      ))}
    </Stack>

    {/* SLIDER RANGE PREZZO */}
    <Box sx ={{px: 2}}>
      <Typography>Prezzo: {rangePrezzo[0]} € - {rangePrezzo[1]} €</Typography>
      <Slider value={rangePrezzo} min={prezzoMin} max={prezzoMax}
        onChange={(_, valore) => setRangePrezzo(valore as number[])}
        valueLabelDisplay="auto" />
    </Box>

    {/* SELECT ORDINAMENTO */}
    <FormControl sx={{ maxWidth: 250 }}>
      <InputLabel id="ordina">Ordina per</InputLabel>
      <Select labelId="ordina" label="Ordina per" value={ordinamento}
        onChange={(e) => setOrdinamento(e.target.value)}>
        <MenuItem value="nome-asc">Nome A → Z</MenuItem>
        <MenuItem value="nome-desc">Nome Z → A</MenuItem>
        <MenuItem value="prezzo-asc">Prezzo crescente</MenuItem>
        <MenuItem value="prezzo-desc">Prezzo decrescente</MenuItem>
      </Select>
    </FormControl>

    {filtrati.length === 0 && <Typography>Nessuna materia corrisponde ai filtri</Typography>}
    <Grid container spacing={2}>
      {filtrati.map((s) => (
        // key: React la usa per riconoscere ogni elemento della lista (come un id)
        <Grid key={s.id} size={{ xs: 12, sm: 6, md: 4 }}>
          <Card>
            <CardContent>
              <Typography variant="h6">{s.nome}</Typography>
              <Typography color="text.secondary">
                {s.insegnante.nome} {s.insegnante.cognome}
              </Typography>
              <Typography>{s.durata} minuti - {s.prezzo} €</Typography>
              {/* && = "mostra solo se c'è": se descrizione è null non viene mostrato niente */}
              {s.descrizione && <Typography variant="body2">{s.descrizione}</Typography>}
            </CardContent>
            <CardActions>
              <Button href={`http://localhost:8080/prenotazioni/new/${s.id}`}>Prenota</Button>
            </CardActions>
          </Card>
        </Grid>
      ))}
    </Grid>
    </Stack>
  )
}

export default ServiziFilterGrid