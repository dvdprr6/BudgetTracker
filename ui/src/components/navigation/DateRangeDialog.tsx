import { FC, useCallback, useState, useEffect } from 'react'
import {Dialog, DialogTitle, DialogContent, Stack, Button} from '@mui/material'
import { CloseDialogIcon } from '@budgettracker-components-common'
import { DateRange } from '@mui/x-date-pickers-pro'
import moment from 'moment/moment'
import { Controller, useForm } from 'react-hook-form'
import { DateRangePicker } from '@mui/x-date-pickers-pro/DateRangePicker'
import { TDateRangePicker } from '@budgettracker-utils'

type TDateRangeDialog = {
  open: boolean
  handleOnClose: () => void
}

type TDateRangePickerForm = TDateRangePicker

const DateRangeDialog: FC<TDateRangeDialog> = (props) => {
  const { open, handleOnClose } = props
  const [currentDateRange, setCurrentDateRange] = useState<DateRange<moment.Moment>>([moment().startOf('month'), moment().endOf('month')])
  const { control, handleSubmit, setValue } = useForm<TDateRangePickerForm>()

  useEffect(() => {
    setValue('dateRange', currentDateRange)
  }, [currentDateRange])

  const handleDateRangePickerReset = useCallback(() => {
    setCurrentDateRange([moment().startOf('month'), moment().endOf('month')])
  }, [currentDateRange])

  const handleDateRangeSubmit = (form: TDateRangePickerForm) => {
    setCurrentDateRange([form.dateRange[0], form.dateRange[1]])
    console.log(form.dateRange[0])
    console.log(form.dateRange[1])
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



export default DateRangeDialog