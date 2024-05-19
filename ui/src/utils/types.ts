export type TCashFlowDto = {
  id: string,
  amount: number,
  delta: number,
  createDate: string,
  modifiedDate: string
}

export type TCategoryDto = {
  id: string,
  categoryName: string,
  creatDate: string,
  modifiedDate: string
}

export type TUseCashFlow = {
  amounts: number[],
  months: string[]
}