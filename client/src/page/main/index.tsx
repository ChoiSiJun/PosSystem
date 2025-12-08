import React, { useState } from 'react';
import { Box, Paper, TextField, Button, Typography, Container, Avatar, Stack } from '@mui/material';
import { Store as StoreIcon } from '@mui/icons-material';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { useShopLoginMutation } from '@/features/shop/hook/useShopLoginMutation';
import ShopRegister from './components/ShopRegister';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';
import { useAppDispatch } from '@/app/store/ReduxHooks';
import { shopLogin } from '@/features/shop/slice/ShopSlice';

const validationSchema = yup.object({
  shopCode: yup.string().required('매장 코드를 입력해주세요'),
  password: yup.string().required('비밀번호를 입력해주세요'),
});

const Main: React.FC = () => {
  const [registerOpen, setRegisterOpen] = useState(false);
  const navigate = useNavigate();
  const loginMutation = useShopLoginMutation();

  const dispatch = useAppDispatch();

  const formik = useFormik({
    initialValues: {
      shopCode: '',
      password: '',
    },
    validationSchema,
    onSubmit: async (values) => {
      try {
        const response = await loginMutation.mutateAsync(values);

        // TODO: 토큰 저장 및 상태 관리
        dispatch(shopLogin(response));

        toast.success('로그인되었습니다.');

        // 로그인 성공 후 POS 페이지로 이동
        navigate('/pos');
      } catch (error) {
        toast.error('로그인에 실패했습니다.');
      }
    },
  });

  return (
    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Avatar sx={{ m: 1, bgcolor: 'primary.main', width: 64, height: 64 }}>
          <StoreIcon sx={{ fontSize: 40 }} />
        </Avatar>
        <Typography component="h1" variant="h4" sx={{ mb: 3, fontWeight: 'bold' }}>
          매장 로그인
        </Typography>

        <Paper sx={{ p: 4, width: '100%' }} elevation={3}>
          <form onSubmit={formik.handleSubmit}>
            <Stack spacing={3}>
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
                id="password"
                name="password"
                label="비밀번호"
                type="password"
                value={formik.values.password}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                error={formik.touched.password && Boolean(formik.errors.password)}
                helperText={formik.touched.password && formik.errors.password}
              />

              <Button
                type="submit"
                fullWidth
                variant="contained"
                size="large"
                disabled={loginMutation.isPending}
                sx={{ mt: 2, py: 1.5 }}
              >
                {loginMutation.isPending ? '로그인 중...' : '로그인'}
              </Button>

              <Button
                fullWidth
                variant="outlined"
                size="large"
                onClick={() => setRegisterOpen(true)}
                sx={{ py: 1.5 }}
              >
                매장 등록
              </Button>
            </Stack>
          </form>
        </Paper>
      </Box>

      <ShopRegister open={registerOpen} onClose={() => setRegisterOpen(false)} />
    </Container>
  );
};

export default Main;
