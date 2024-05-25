import { TCategoryGroupByWithTotalsDto, TUseCategoryGroupByWithTotals } from '@budgettracker-utils'
import moment from 'moment'

export function useCategories(categoriesGroupByWithTotalsDto: TCategoryGroupByWithTotalsDto[]): TUseCategoryGroupByWithTotals{
  const categoriesGroupByWithTotalsCopy = [...categoriesGroupByWithTotalsDto]

  const sortedCategoriesGroupByWithTotalsDto = categoriesGroupByWithTotalsCopy.sort((a, b) => moment(a.createDate).diff(moment(b.createDate)))

  const pieChartData = sortedCategoriesGroupByWithTotalsDto.map(item => ({ id: item.id, value: item.total, label: item.categoryName }))

  return { pieChartData }
}