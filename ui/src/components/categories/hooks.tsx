import { TCategoryGroupByWithTotalsDto, TItemDto } from '@budgettracker-utils'
import moment from 'moment'

type TUseCategories = {
  pieChartData: { id: string, value: number, label: string }[]
}

export function useCategories(categoriesGroupByWithTotalsDto: TCategoryGroupByWithTotalsDto[]): TUseCategories{
  const categoriesGroupByWithTotalsCopy = [...categoriesGroupByWithTotalsDto]

  const sortedCategoriesGroupByWithTotalsDto = categoriesGroupByWithTotalsCopy.sort((a, b) => moment(a.createDate).diff(moment(b.createDate)))

  const pieChartData = sortedCategoriesGroupByWithTotalsDto.map(item => ({ id: item.id, value: item.total, label: item.categoryName }))

  return { pieChartData }
}

export function useCategoriesDialog(itemDto: TItemDto[]): TUseCategories{
  const itemDtoCopy = [...itemDto]

  const pieChartData = itemDtoCopy.map(record => ({ id: record.id, value: record.amount, label: record.itemName }))
  
  return { pieChartData }
}