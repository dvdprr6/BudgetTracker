import { Grid } from '@mui/material'
import { BudgetTrackerTable, TColumn, BudgetTrackerPieChart } from '@budgettracker-components-common'

const columns: TColumn[] = [
  { id: 'name', label: 'Name', minWidth: 170 },
  { id: 'code', label: 'ISO\u00a0Code', minWidth: 100 },
  {
    id: 'population',
    label: 'Population',
    minWidth: 170,
    align: 'right',
    format: (value: number) => value.toLocaleString('en-US')
  },
  {
    id: 'size',
    label: 'Size\u00a0(km\u00b2)',
    minWidth: 170,
    align: 'right',
    format: (value: number) => value.toLocaleString('en-US')
  },
  {
    id: 'density',
    label: 'Density',
    minWidth: 170,
    align: 'right',
    format: (value: number) => value.toFixed(2)
  }
]

function createData(
  name: string,
  code: string,
  population: number,
  size: number
) {
  const density = population / size
  return { name, code, population, size, density }
}

const data = [
  createData('India', 'IN', 1324171354, 3287263),
  createData('China', 'CN', 1403500365, 9596961),
  createData('Italy', 'IT', 60483973, 301340),
  createData('United States', 'US', 327167434, 9833520),
  createData('Canada', 'CA', 37602103, 9984670),
  createData('Australia', 'AU', 25475400, 7692024),
  createData('Germany', 'DE', 83019200, 357578),
  createData('Ireland', 'IE', 4857000, 70273),
  createData('Mexico', 'MX', 126577691, 1972550),
  createData('Japan', 'JP', 126317000, 377973),
  createData('France', 'FR', 67022000, 640679)
]

const Categories = () => {
  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <BudgetTrackerPieChart data={data} width={800} height={400} />
      </Grid>
      <Grid item xs={12}>
        <BudgetTrackerTable columns={columns} rows={data} />
      </Grid>
    </Grid>
  )
}

export default Categories