import { FC } from 'react'
import Button from '@mui/material/Button'
import Dialog from '@mui/material/Dialog'
import DialogTitle from '@mui/material/DialogTitle'
import DialogContent from '@mui/material/DialogContent'
import DialogActions from '@mui/material/DialogActions'
import Typography from '@mui/material/Typography'

type TCategoriesDialog = {
  open: boolean
  handleOnClose: () => void
}

const CategoriesDialog: FC<TCategoriesDialog> = (props) => {
  const { open , handleOnClose } = props

  return(
    <Dialog
      open={open}
      onClick={handleOnClose}
      fullWidth={true}
      maxWidth={'xl'}
      scroll={'paper'}
    >
      <DialogTitle sx={{ m: 0, p: 2}}>
        Category
      </DialogTitle>
      <DialogContent dividers>
        <Typography gutterBottom>
          Cras mattis consectetur purus sit amet fermentum. Cras justo odio,
          dapibus ac facilisis in, egestas eget quam. Morbi leo risus, porta ac
          consectetur ac, vestibulum at eros.
        </Typography>
      </DialogContent>
      <DialogActions>
        <Button autoFocus onClick={handleOnClose}>
          Save Changes
        </Button>
      </DialogActions>
    </Dialog>
  )
}

export default CategoriesDialog