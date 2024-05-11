import { FC } from 'react'
import { PieChart } from '@mui/x-charts'
import { Box, Card } from '@mui/material'

type TBudgetTrackerPieChart = {
  data: any[],
  width: number,
  height: number
}

const BudgetTrackerPieChart: FC<TBudgetTrackerPieChart> = (props) => {
  const { data, width, height } = props

  return (
    <Card variant={'outlined'} sx={{ height: '100%'}}>
      <Box alignItems={'center'} display={'flex'} p={2}>
        <PieChart
          series={[
            {
              data: data.map(((item, index) => ({ id: index, value: item.population, label: item.name }))),
            },
          ]}
          width={width}
          height={height}
        />
      </Box>
    </Card>
  )
}

export default BudgetTrackerPieChart