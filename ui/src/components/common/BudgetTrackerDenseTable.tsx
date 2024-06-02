import { FC } from 'react'
import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableContainer from '@mui/material/TableContainer'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'
import Paper from '@mui/material/Paper'
import Card from '@mui/material/Card'
import { TColumn } from './types'
import { TABLE_MAX_HEIGHT } from '@budgettracker-utils'

type TBudgetTrackerDenseTable = {
  columns: TColumn[],
  rows: any[]
}

const BudgetTrackerDenseTable: FC<TBudgetTrackerDenseTable> = (props) => {
  const { columns, rows } = props

  return(
    <Card variant={'outlined'} sx={{ height: '100%' }}>
      <Paper sx={{ width: '100%', overflow: 'hidden' }}>
        <TableContainer sx={{ maxHeight: TABLE_MAX_HEIGHT }}>
          <Table sx={{ minWidth: 650 }} size={'small'}>
            <TableHead>
              <TableRow>
                {columns.map(column => (
                  <TableCell
                    key={column.id}
                    align={column.align}
                    style={{ minWidth: column.minWidth }}
                  >
                    {column.label}
                  </TableCell>
                ))}
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map(row => (
                <TableRow
                  key={row.name}
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                >
                  {columns.map(column => (
                    <TableCell key={column.id} align={column.align}>
                      {row[column.id]}
                    </TableCell>
                  ))}
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
    </Card>
  )
}

export default BudgetTrackerDenseTable