export type TCashFlowDto = {
  id: string
  amount: number
  delta: number
  createDate: string
  modifiedDate: string
}

export type TCategoryDto = {
  id: string
  categoryName: string
  creatDate: string
  modifiedDate: string
}

export type TCategoryGroupByWithTotalsDto = {
  id: string
  categoryName: string
  total: number
  createDate: string
  modifiedDate: string
}

export type TItemDto = {
  id: string
  itemName: string
  amount: number
  itemType: string
  categoryId: string
  createDate: string
  modifiedDate: string
}

export type TUseCashFlow = {
  amounts: number[]
  months: string[]
}
