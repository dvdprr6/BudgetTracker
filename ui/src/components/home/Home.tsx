import { Navigate } from 'react-router-dom'
import { DASHBOARD_REDIRECT } from '@budgettracker-utils'

const Home = () => {
  return <Navigate to={DASHBOARD_REDIRECT} replace />
}

export default Home