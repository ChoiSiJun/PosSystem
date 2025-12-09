import React from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Box,
  Typography,
  IconButton,
} from '@mui/material';
import { Close as CloseIcon } from '@mui/icons-material';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { useShopRegisterMutation } from '@/features/shop/hook/useShopRegisterMutation';
import { toast } from 'react-toastify';

interface ShopRegisterProps {
  open: boolean;
  onClose: () => void;
  onSuccess?: () => void;
}

const validationSchema = yup.object({
  shopCode: yup.string().required('매장 코드를 입력해주세요'),
  shopName: yup.string().required('매장 이름을 입력해주세요'),
  password: yup
    .string()
    .required('비밀번호를 입력해주세요')
    .min(4, '비밀번호는 최소 4자 이상이어야 합니다'),
  adminPassword: yup
    .string()
    .required('관리자 비밀번호를 입력해주세요')
    .min(4, '관리자 비밀번호는 최소 4자 이상이어야 합니다'),
});

const ShopRegister: React.FC<ShopRegisterProps> = ({ open, onClose }) => {
  const registerMutation = useShopRegisterMutation();

  const formik = useFormik({
    initialValues: {
      shopCode: '',
      shopName: '',
      password: '',
      adminPassword: '',
    },
    validationSchema,
    onSubmit: async (values) => {
      try {
        await registerMutation.mutateAsync(values);
        toast.success('매장이 등록되었습니다.');
        formik.resetForm();
        onClose();
      } catch (error) {}
    },
  });

  const handleClose = () => {
    formik.resetForm();
    onClose();
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
      <DialogTitle>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="h6">매장 등록</Typography>
          <IconButton onClick={handleClose} size="small">
            <CloseIcon />
          </IconButton>
        </Box>
      </DialogTitle>
      <form onSubmit={formik.handleSubmit}>
        <DialogContent>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 1 }}>
            <TextField
              fullWidth
              id="shopCode"
              name="shopCode"
              label="매장 코드"
              value={formik.values.shopCode}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={formik.touched.shopCode && Boolean(formik.errors.shopCode)}
              helperText={formik.touched.shopCode && formik.errors.shopCode}
              autoFocus
            />

            <TextField
              fullWidth
              id="shopName"
              name="shopName"
              label="매장 이름"
              value={formik.values.shopName}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={formik.touched.shopName && Boolean(formik.errors.shopName)}
              helperText={formik.touched.shopName && formik.errors.shopName}
            />

            <TextField
              fullWidth
              id="password"
              name="password"
              label="매장 비밀번호"
              type="password"
              value={formik.values.password}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={formik.touched.password && Boolean(formik.errors.password)}
              helperText={formik.touched.password && formik.errors.password}
            />

            <TextField
              fullWidth
              id="adminPassword"
              name="adminPassword"
              label="관리자 비밀번호"
              type="password"
              value={formik.values.adminPassword}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={formik.touched.adminPassword && Boolean(formik.errors.adminPassword)}
              helperText={formik.touched.adminPassword && formik.errors.adminPassword}
            />
          </Box>
        </DialogContent>
        <DialogActions sx={{ px: 3, pb: 2 }}>
          <Button onClick={handleClose} variant="outlined">
            취소
          </Button>
          <Button type="submit" variant="contained" disabled={registerMutation.isPending}>
            등록
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
};

export default ShopRegister;

