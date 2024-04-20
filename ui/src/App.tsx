import { createTheme, Theme } from '@mui/material'
import { ThemeProvider } from '@mui/material/styles'
import {createBrowserRouter, RouterProvider} from 'react-router-dom'
import {Navigation} from "./components/navigation";
import CssBaseline from '@mui/material/CssBaseline';

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
    path: '/BudgetTracker',
    element: <Navigation />
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
