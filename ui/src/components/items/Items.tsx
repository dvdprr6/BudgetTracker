import {
  Grid, Box, Card, Typography,
  Accordion, AccordionSummary, AccordionDetails,
} from '@mui/material'
import { DatePicker } from '@mui/x-date-pickers/DatePicker'
import ExpandMoreIcon from '@mui/icons-material/ExpandMore'

const Items = () => {
  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <Accordion>
          <AccordionSummary expandIcon={<ExpandMoreIcon />}>
            <DatePicker />
            <Typography>-</Typography>
            <DatePicker />
          </AccordionSummary>
        </Accordion>
        {/*<Card variant={'outlined'} sx={{ height: '100%' }}>*/}
        {/*  <Box alignItems={'center'} display={'flex'} p={2}>*/}
        {/*    <DatePicker />*/}
        {/*    <DatePicker />*/}
        {/*  </Box>*/}
        {/*</Card>*/}
      </Grid>
    </Grid>
  )
}

export default Items