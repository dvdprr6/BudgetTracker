import { FC, useEffect } from 'react'
import {Dialog, DialogTitle, DialogContent, Stack, Button} from '@mui/material'
import { CloseDialogIcon } from '@budgettracker-components-common'
import { Controller, useForm } from 'react-hook-form'
import { DateRangePicker } from '@mui/x-date-pickers-pro/DateRangePicker'
import { TDateRangePicker } from '@budgettracker-utils'
import {
  TPropsFromRedux,
  connector,
  TAppDispatch,
  setGlobalDateRange,
  resetGlobalDateRange
} from '@budgettracker-reducers'
import { useDispatch } from 'react-redux'

type TDateRangeDialog = {
  open: boolean
  handleOnClose: () => void
}

type TDateRangePickerForm = TDateRangePicker

const DateRangeDialog: FC<TDateRangeDialog & TPropsFromRedux> = (props) => {
  const { open, handleOnClose, globalDateRange: { value: gDateRange } } = props
  const { control, handleSubmit, setValue } = useForm<TDateRangePickerForm>()
  const dispatch: TAppDispatch = useDispatch()

  useEffect(() => {
    setValue('dateRange', gDateRange.dateRange)
  }, [gDateRange.dateRange])

  const handleDateRangePickerReset = () => {
    function resetDateRange(): Promise<void>{
      dispatch(resetGlobalDateRange())
      return Promise.resolve()
    }
    resetDateRange().then()
  }

  const handleDateRangeSubmit = (form: TDateRangePickerForm) => {
    function setDateRange(): Promise<void>{
      dispatch(setGlobalDateRange(form))
      return Promise.resolve()
    }
    setDateRange().then(() => handleOnClose())
  }

  return (
    <Dialog
      open={open}
      fullWidth={false}
      maxWidth={'xl'}
      scroll={'paper'}
    >
      <DialogTitle sx={{ m: 0, p: 2}}>
        Date Range
      </DialogTitle>
      <CloseDialogIcon handleOnClose={handleOnClose} />
      <DialogContent dividers>
        <Controller
          name={'dateRange'}
          control={control}
          render={({ field: { value, onChange }}) => (
            <Stack spacing={2} direction={'row'}>
              <DateRangePicker
                slotProps={{ textField: { size: 'small', color: 'secondary' } }}
                localeText={{ start: 'From Date', end: 'To Date' }}
                value={value}
                onChange={dateRange => onChange(dateRange)}
              />
              <Button variant={'contained'} size={'small'} onClick={handleDateRangePickerReset}>Reset</Button>
              <Button variant={'contained'} size={'small'} onClick={handleSubmit(handleDateRangeSubmit)}>Submit</Button>
            </Stack>
          )}
        />
      </DialogContent>
    </Dialog>
  )
}

export default connector(DateRangeDialog)