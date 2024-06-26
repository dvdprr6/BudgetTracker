export const HOME_REDIRECT: string = '/BudgetTracker'
export const DASHBOARD_REDIRECT: string = '/BudgetTracker/dashboard'
export const CATEGORIES_REDIRECT: string = '/BudgetTracker/categories'
export const CASH_FLOW_REDIRECT: string = '/BudgetTracker/cashFlow'
export const ITEM_REDIRECT: string = '/BudgetTracker/item'

export const TABLE_MAX_HEIGHT: number = 370

/* REDUCER CONSTANTS */
export const CASH_FLOW_SLICE: string = 'CASH_FLOW_SLICE'
export const CATEGORY_SLICE: string = 'CATEGORY_SLICE'
export const CATEGORY_GROUP_BY_WITH_TOTALS_SLICE: string = 'CATEGORY_GROUP_BY_WITH_TOTALS_SLICE'
export const ITEM_SLICE: string = 'ITEM_SLICE'
export const ITEM_BY_CATEGORY_SLICE: string = 'ITEM_BY_CATEGORY_SLICE'

/* THUNK CONSTANTS */
export const GET_CASH_FLOW_RECORDS: string = 'get/cashFlow'
export const GET_CATEGORY_RECORDS: string = 'get/category'
export const GET_CATEGORY_GROUP_BY_WITH_TOTALS_RECORDS: string = 'get/categoryGroupByWithTotalsRecords'
export const GET_ITEM_RECORDS: string = 'get/item'
export const GET_ITEM_BY_CATEGORY_RECORDS: string = 'get/itemByCategoryRecords'

/* API URLS */
const HTTP_URL: string = 'http://localhost:8080/BudgetTracker/'
export const CASH_FLOW_API_URL: string = HTTP_URL + 'api/cashFlow'
export const CATEGORY_API_URL: string = HTTP_URL + 'api/category'
export const CATEGORY_GROUP_BY_WITH_TOTALS_API_URL: string = HTTP_URL + 'api/category/categoryGroupByWithTotals'
export const ITEM_API_URL: string = HTTP_URL + 'api/item'
export const ITEM_BY_CATEGORY_API_URL: string = HTTP_URL + 'api/item/itemByCategory'

export const EXPENSE = 'Expense'
export const REVENUE = 'Revenue'