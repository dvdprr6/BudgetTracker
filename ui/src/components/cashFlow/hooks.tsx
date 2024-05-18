import { TCashFlowDto, TUseCashFlow } from '@budgettracker-utils'
import moment from 'moment'

export function useCashFlow(cashFlowDto: TCashFlowDto[]): TUseCashFlow {
  const cashFlowCopy = [...cashFlowDto]

  const sortedCashFlows = cashFlowCopy.sort((a, b) => moment(a.createDate).diff(moment(b.createDate)))

  const amounts = sortedCashFlows.map(record => record.amount)

  const months = sortedCashFlows.map(record => moment(record.createDate).format('MMMM'))

  return { amounts, months }
}