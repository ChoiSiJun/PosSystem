import * as React from 'react';
import { useEffect } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { toast } from 'react-toastify';

// Material-UI 컴포넌트
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import { createTheme, ThemeProvider } from '@mui/material/styles';

// 커스텀 컴포넌트 및 API

import { useAppDispatch } from '@/app/store/ReduxHooks';
import type { RootState } from '@/app/store/ReduxStoreConfig';
import { authDelete } from '@/features/auth/slice/AuthSlice';

// 기본 테마 설정 (데모 목적)
const defaultTheme = createTheme();

/**
 * @description 로그인 및 회원가입 페이지 컴포넌트
 * 로그인 폼과 회원가입 드로어를 관리하며, JWT 상태에 따라 페이지를 제어합니다.
 */
export default function Auth() {
  // Redux 상태 관리
  const status = useSelector((state: RootState) => state.auth.status);
  const exp = useSelector((state: RootState) => state.auth.exp);

  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  // 회원가입 드로어 상태 관리
  const [joinOpen, setJoinOpen] = React.useState(false);

  // JWT 토큰 상태에 따른 페이지 리다이렉션 및 만료 처리
  useEffect(() => {
    // Redux의 status가 'success'이고
    if (status === 'success') {
      const now = Date.now() / 1000; // 현재 시간 (초 단위)
      // 토큰이 만료되었다면 (exp가 현재 시간보다 작다면)
      if (exp && exp < now) {
        dispatch(authDelete()); // 토큰 삭제
      }
      // 유효한 토큰이 있다면 홈 페이지로 리다이렉트
      navigate('/forjun');
    }
  }, [dispatch, exp, navigate, status]);

  // 로그인 API 호출을 위한 뮤테이션 훅
  const loginMutation = useLoginMutation();

  // 회원가입 드로어 핸들러
  const handleJoin = () => {
    setJoinOpen(!joinOpen);
  };

  // Formik을 이용한 로그인 폼 상태 관리 및 유효성 검사
  const loginForm = useFormik({
    initialValues: {
      userId: '',
      password: '',
    },
    validationSchema: Yup.object({
      userId: Yup.string().required('아이디는 필수 항목입니다.'),
      password: Yup.string()
        .required('비밀번호는 필수 항목입니다.')
        .matches(
          /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\\[\]{};':"\\|,.<>\\/?]).{8,}$/,
          '비밀번호는 최소 8자 이상이며, 영문, 숫자, 특수문자를 모두 포함해야 합니다.'
        ),
    }),
    onSubmit: async (values) => {
      // loginMutation 훅의 mutate 함수를 호출하고 성공 시 콜백을 전달
      loginMutation.mutate(values, {
        onSuccess: (token) => {
          // API 성공 후 Redux 상태를 업데이트하고 토스트 메시지를 표시
          dispatch(authInsert(token));
          toast.success('로그인 되었습니다.');
        },
      });
    },
  });

  return (
    <ThemeProvider theme={defaultTheme}>
      <Grid container component="main" sx={{ height: '100vh' }}>
        <CssBaseline />
        <Grid
          item
          xs={false}
          sm={4}
          md={7}
          sx={{
            backgroundImage: 'url(/asset/loginbanner.jpg)',
            backgroundRepeat: 'no-repeat',
            backgroundColor: (t) =>
              t.palette.mode === 'light' ? t.palette.grey[50] : t.palette.grey[900],
            backgroundSize: 'cover',
            backgroundPosition: 'center',
          }}
        />
        <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
          <Box
            sx={{
              my: 8,
              mx: 4,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Sign in
            </Typography>
            <Box
              component="form"
              noValidate
              onSubmit={loginForm.handleSubmit}
              sx={{
                mt: 1,
                width: '320px',
              }}
            >
              {/* 아이디 입력 필드 */}
              <Grid item xs={12} sm={12} md={12} margin={1}>
                <SjTextField
                  label="ID"
                  name="userId"
                  size="medium"
                  onChange={loginForm.handleChange}
                  onBlur={loginForm.handleBlur}
                  error={!!loginForm.errors.userId && !!loginForm.touched.userId}
                  helperText={
                    loginForm.touched.userId && loginForm.errors.userId
                      ? loginForm.errors.userId
                      : undefined
                  }
                />
              </Grid>

              {/* 비밀번호 입력 필드 */}
              <Grid item xs={12} sm={12} md={12} margin={1}>
                <SjTextField
                  name="password"
                  label="Password"
                  type="password"
                  onChange={loginForm.handleChange}
                  onBlur={loginForm.handleBlur}
                  error={!!loginForm.errors.password && !!loginForm.touched.password}
                  helperText={
                    loginForm.touched.password && loginForm.errors.password
                      ? loginForm.errors.password
                      : undefined
                  }
                />
              </Grid>

              <FormControlLabel
                control={<Checkbox value="remember" color="primary" />}
                label="Remember me"
              />
              <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2 }}>
                Sign In
              </Button>
              <Grid container justifyContent="center">
                <Grid item>
                  <Link href="#" onClick={handleJoin} variant="body2">
                    {'Sign Up'}
                  </Link>
                </Grid>
              </Grid>
            </Box>
          </Box>
        </Grid>

        <Join joinOpen={joinOpen} handleJoin={handleJoin}></Join>
      </Grid>
    </ThemeProvider>
  );
}
