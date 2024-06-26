import { createTheme, Theme } from '@mui/material'
import { ThemeProvider } from '@mui/material/styles'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import { Home } from '@budgettracker-components-home'
import { DashboardPage } from '@budgettracker-components-dashboard'
import { CategoriesPage } from '@budgettracker-components-categories'
import { CashFlowPage } from '@budgettracker-components-cashFlow'
import { ItemsPage } from '@budgettracker-components-items'
import CssBaseline from '@mui/material/CssBaseline'
import { HOME_REDIRECT, DASHBOARD_REDIRECT, CATEGORIES_REDIRECT, CASH_FLOW_REDIRECT, ITEM_REDIRECT } from '@budgettracker-utils'
import { Provider } from 'react-redux'
import { store } from '@budgettracker-reducers'

const theme: Theme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#1d5bb9'
    },
    secondary: {
      main: '#1d5bb9'
    },
    background: {
      default: '#303030',
      paper: '#424242'
    },
  },
  typography: {
    button: {
      textTransform: 'none'
    }
  },
  components: {
    MuiAppBar: {
      styleOverrides: {
        root: {
          background: '#1d5bb9'
        }
      }
    },
    MuiButton: {
      styleOverrides: {
        text: {
          size: '10',
          textTransform: 'lowercase'
        }
      }
    },
    MuiRadio: {
      styleOverrides: {
        root:{
          color: '#1DB954',
          '&.Mui-checked': {
            color: '#1DB954'
          }
        }
      }
    },
    MuiCircularProgress: {
      styleOverrides: {
        root:{
          size: 20,
          color:'#1DB954'
        }
      }
    }
  }
})

const router = createBrowserRouter([
  {
    path: HOME_REDIRECT,
    element: <Home />
  },
  {
    path: DASHBOARD_REDIRECT,
    element: <DashboardPage />
  },
  {
    path: CATEGORIES_REDIRECT,
    element: <CategoriesPage />
  },
  {
    path: CASH_FLOW_REDIRECT,
    element: <CashFlowPage />
  },
  {
    path: ITEM_REDIRECT,
    element: <ItemsPage />
  }
])

const App = () => {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Provider store={store}>
        <RouterProvider router={router} />
      </Provider>
    </ThemeProvider>
  )
}

export default App
