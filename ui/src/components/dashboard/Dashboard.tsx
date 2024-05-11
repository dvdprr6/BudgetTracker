import { Grid, Box, Card } from '@mui/material'
import { BudgetTrackerTable, TColumn, BudgetTrackerPieChart } from '@budgettracker-components-common'

const columns: TColumn[] = [
  { id: 'name', label: 'Name', minWidth: 170 },
  { id: 'code', label: 'ISO\u00a0Code', minWidth: 100 },
  {
    id: 'population',
    label: 'Population',
    minWidth: 170,
    align: 'right',
    format: (value: number) => value.toLocaleString('en-US'),
  },
  {
    id: 'size',
    label: 'Size\u00a0(km\u00b2)',
    minWidth: 170,
    align: 'right',
    format: (value: number) => value.toLocaleString('en-US'),
  },
  {
    id: 'density',
    label: 'Density',
    minWidth: 170,
    align: 'right',
    format: (value: number) => value.toFixed(2),
  },
]

function createData(
  name: string,
  code: string,
  population: number,
  size: number,
) {
  const density = population / size;
  return { name, code, population, size, density };
}

const rows = [
  createData('Expense', 'IN', 1324171354, 3287263),
  createData('Revenue', 'CN', 1403500365, 9596961),
  // createData('Italy', 'IT', 60483973, 301340),
  // createData('United States', 'US', 327167434, 9833520),
  // createData('Canada', 'CA', 37602103, 9984670),
  // createData('Australia', 'AU', 25475400, 7692024),
  // createData('Germany', 'DE', 83019200, 357578),
  // createData('Ireland', 'IE', 4857000, 70273),
  // createData('Mexico', 'MX', 126577691, 1972550),
  // createData('Japan', 'JP', 126317000, 377973),
  // createData('France', 'FR', 67022000, 640679),
  //
  // createData('Japan', 'JP', 126317000, 377973),
  // createData('France', 'FR', 67022000, 640679),
  // createData('Japan', 'JP', 126317000, 377973),
  // createData('France', 'FR', 67022000, 640679)
];

const Dashboard = () => {

  return (
    <Grid container spacing={2}>
      <Grid item xs={6}>
        <BudgetTrackerPieChart data={rows} width={600} height={400} />
      </Grid>
      <Grid item xs={6}>
        <Card variant={'outlined'} sx={{ height: '100%'}}>
          <Box alignItems={'center'} display={'flex'} p={2}>
            This Box uses MUI System props for quick customization.
          </Box>
          <Box alignItems={'center'} display={'flex'} p={2}>
            This Box uses MUI System props for quick customization.
          </Box>
          <Box alignItems={'center'} display={'flex'} p={2}>
            This Box uses MUI System props for quick customization.
          </Box>
          <Box alignItems={'center'} display={'flex'} p={2}>
            This Box uses MUI System props for quick customization.
          </Box>
        </Card>
      </Grid>
      <Grid item xs={12}>
        <BudgetTrackerTable columns={columns} rows={rows} />
      </Grid>
    </Grid>
  )
}

export default Dashboard