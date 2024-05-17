import { useEffect } from 'react'
import { Navigate } from 'react-router-dom'
import { DASHBOARD_REDIRECT } from '@budgettracker-utils'
import { useDispatch } from 'react-redux'
import { getCashFlowRecords, TAppDispatch } from '@budgettracker-reducers'

const Home = () => {
  const distpact: TAppDispatch = useDispatch()

  useEffect(() => {
    function homeDispatch(): Promise<void>{
      distpact(getCashFlowRecords())

      return Promise.resolve()
    }

    homeDispatch().then()
  }, []);

  return <Navigate to={DASHBOARD_REDIRECT} replace />
}

export default Home