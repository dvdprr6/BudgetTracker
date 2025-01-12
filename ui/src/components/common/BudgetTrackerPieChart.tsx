import { FC } from 'react'
import { PieChart } from '@mui/x-charts'
import { Box, Card, CardContent } from '@mui/material'

type TBudgetTrackerPieChart = {
  data: { id: string, value: number, label: string }[],
  width: number,
  height: number
}

const BudgetTrackerPieChart: FC<TBudgetTrackerPieChart> = (props) => {
  const { data, width, height } = props

  return (
    <Card variant={'outlined'} sx={{ height: '100%'}}>
      <CardContent>
        <Box alignItems={'center'} display={'flex'}>
          <PieChart
            series={[
              {
                data: data,
                highlightScope: { faded: 'global', highlighted: 'item' },
                faded: { innerRadius: 30, additionalRadius: -30, color: 'gray' },
                valueFormatter: v => new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD'}).format(v.value)
              }
            ]}
            width={width}
            height={height}
          />
        </Box>
      </CardContent>
    </Card>
  )
}

export default BudgetTrackerPieChart