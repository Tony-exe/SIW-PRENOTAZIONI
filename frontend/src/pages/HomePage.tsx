import { Container, Typography } from '@mui/material'
import ServiziFilterGrid from '../components/ServiziFilterGrid'

function HomePage() {
  return (
    <Container sx={{ py: 4 }}>
      <Typography variant="h4" gutterBottom>Lezioni private</Typography>
      <Typography color="text.secondary" gutterBottom>
        Scegli una materia, filtra per insegnante o prezzo e prenota la tua lezione.
      </Typography>
      <ServiziFilterGrid />
    </Container>
  )
}

export default HomePage