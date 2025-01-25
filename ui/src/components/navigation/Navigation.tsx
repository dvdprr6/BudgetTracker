import React, { FC, ReactNode, useState, useCallback } from 'react'
import MenuIcon from '@mui/icons-material/Menu'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import { AppBar, Box, Toolbar, IconButton, Typography, Menu, Container, Button, MenuItem } from '@mui/material'
import { styled } from '@mui/material/styles'
import {
  DASHBOARD_REDIRECT,
  CATEGORIES_REDIRECT,
  CASH_FLOW_REDIRECT,
  ITEM_REDIRECT
} from '@budgettracker-utils'
import { useNavigate } from 'react-router-dom'
import DateRangeIcon from '@mui/icons-material/DateRange'
import DateRangeDialog from './DateRangeDialog.tsx'

const DrawerHeader = styled('div')(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'flex-end',
  padding: theme.spacing(0, 1),
  // necessary for content to be below app bar
  ...theme.mixins.toolbar
}))

const Navigation: FC<{ component: ReactNode }> = (props) => {
  const { component: Component } = props
  const [anchorElNav, setAnchorElNav] = React.useState<null | HTMLElement>(null)
  const navigate = useNavigate()
  const [openDialog, setOpenDialog] = useState<boolean>(false)

  const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElNav(event.currentTarget)
  }

  const handleRedirect = (redirectUrl: string) => {
    navigate(redirectUrl)
    //setAnchorElNav(null)
  }

  const handleCloseNavMenu = () => {
    setAnchorElNav(null)
  }

  const handleOpenDialog = useCallback(() => { setOpenDialog(true) }, [openDialog])

  const handleCloseDialog = useCallback(() => { setOpenDialog(false) }, [openDialog])

  return (
    <Box sx={{ display: 'flex' }}>
      <AppBar>
        <Container maxWidth="xl">
          <Toolbar disableGutters>
            <TrendingUpIcon sx={{ display: { xs: 'none', md: 'flex' }, mr: 1 }} />
            <Typography
              variant="h6"
              noWrap
              component="a"
              href="#app-bar-with-responsive-menu"
              sx={{
                mr: 2,
                display: { xs: 'none', md: 'flex' },
                fontFamily: 'monospace',
                fontWeight: 700,
                letterSpacing: '.2rem',
                color: 'inherit',
                textDecoration: 'none',
              }}
            >
              Budget Tracker
            </Typography>
            <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
              <IconButton
                size="large"
                aria-label="account of current user"
                aria-controls="menu-appbar"
                aria-haspopup="true"
                onClick={handleOpenNavMenu}
                color="inherit"
              >
                <MenuIcon />
              </IconButton>
              <Menu
                id="menu-appbar"
                anchorEl={anchorElNav}
                anchorOrigin={{
                  vertical: 'bottom',
                  horizontal: 'left',
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'left',
                }}
                open={Boolean(anchorElNav)}
                onClose={handleCloseNavMenu}
                sx={{
                  display: { xs: 'block', md: 'none' },
                }}
              >
                <MenuItem onClick={() => handleRedirect(DASHBOARD_REDIRECT)}>
                  <Typography textAlign={'center'}>Dashboard</Typography>
                </MenuItem>
                <MenuItem onClick={() => handleRedirect(CATEGORIES_REDIRECT)}>
                  <Typography textAlign={'center'}>Categories</Typography>
                </MenuItem>
              </Menu>
            </Box>
            <TrendingUpIcon sx={{ display: { xs: 'flex', md: 'none' }, mr: 1 }} />
            <Typography
              variant="h5"
              noWrap
              component="a"
              href="#app-bar-with-responsive-menu"
              sx={{
                mr: 2,
                display: { xs: 'flex', md: 'none' },
                flexGrow: 1,
                fontFamily: 'monospace',
                fontWeight: 700,
                letterSpacing: '.2rem',
                color: 'inherit',
                textDecoration: 'none',
              }}
            >
              Budget Tracker
            </Typography>
            <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
              <Button onClick={() => handleRedirect(DASHBOARD_REDIRECT)} sx={{ my: 2, color: 'white', display: 'block' }}>
                Dashboard
              </Button>
              <Button onClick={() => handleRedirect(ITEM_REDIRECT)} sx={{ my: 2, color: 'white', display: 'block' }}>
                Items
              </Button>
              <Button onClick={() => handleRedirect(CATEGORIES_REDIRECT)} sx={{ my: 2, color: 'white', display: 'block' }}>
                Categories
              </Button>
              <Button onClick={() => handleRedirect(CASH_FLOW_REDIRECT)} sx={{ my: 2, color: 'white', display: 'block' }}>
                Cash Flow
              </Button>
            </Box>
            <IconButton onClick={handleOpenDialog}>
              <DateRangeIcon />
            </IconButton>
            <DateRangeDialog
              open={openDialog}
              handleOnClose={handleCloseDialog}
            />
          </Toolbar>
        </Container>
      </AppBar>
      <Box component='main' sx={{ flexGrow: 1, p: 3 }}>
        <DrawerHeader />
        <Container maxWidth={'xl'}>
          <div>{Component}</div>
        </Container>
      </Box>
    </Box>
  )
}

export default Navigation
