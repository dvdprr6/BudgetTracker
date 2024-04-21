import { createTheme, Theme } from '@mui/material'
import { ThemeProvider } from '@mui/material/styles'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import { Home } from '@budgettracker-components-home'
import { DashboardPage } from '@budgettracker-components-dashboard'
import { CategoriesPage } from '@budgettracker-components-categories'
import CssBaseline from '@mui/material/CssBaseline'
import { HOME_REDIRECT, DASHBOARD_REDIRECT, CATEGORIES_REDIRECT } from '@budgettracker-utils'

const theme: Theme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#1DB954'
    },
    secondary: {
      main: '#1DB954'
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
  }
])

const App = () => {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <RouterProvider router={router} />
    </ThemeProvider>
  )
}

export default App
