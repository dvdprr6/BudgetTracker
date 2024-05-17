export const HOME_REDIRECT: string = '/BudgetTracker'
export const DASHBOARD_REDIRECT: string = '/BudgetTracker/dashboard'
export const CATEGORIES_REDIRECT: string = '/BudgetTracker/categories'
export const CASH_FLOW_REDIRECT: string = '/BudgetTracker/cashFlow'

export const TABLE_MAX_HEIGHT: number = 370

/* REDUCER CONSTANTS */
export const CASH_FLOW_SLICE: string = 'CASH_FLOW_SLICE'

/* THUNK CONSTANTS */
export const GET_CASH_FLOW_RECORDS: string = 'get/cashFlow'

/* API URLS */
const HTTP_URL: string = 'http://localhost:8080/BudgetTracker/'
export const CASH_FLOW_API_URL: string = HTTP_URL + 'api/cashFlow'