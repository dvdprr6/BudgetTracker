import { FC } from 'react'
import { LineChart } from '@mui/x-charts'
import { Box, Card } from '@mui/material'

type TBudgetTrackerLineChart = {
  data: number[],
  xLabels: string[],
  width: number,
  height: number,
  title: string
}

export const BudgetTrackerLineChart: FC<TBudgetTrackerLineChart> = (props) => {
  const { data, xLabels, width, height, title } = props

  return (
    <Card variant={'outlined'} sx={{ height: '100%'}}>
      <Box alignItems={'center'} display={'flex'} p={2}>
        <LineChart
          width={width}
          height={height}
          series={[{ data: data, label: title }]}
          xAxis={[{ scaleType: 'point', data: xLabels, min: 0 }]}
        />
      </Box>
    </Card>
  )
}

export default BudgetTrackerLineChart