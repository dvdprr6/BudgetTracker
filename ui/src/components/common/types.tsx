export type TColumn = {
  id: string,
  label: string,
  minWidth?: number,
  align?: 'right',
  format?: (value: number) => string
  formatString?: (value: string) => string
}