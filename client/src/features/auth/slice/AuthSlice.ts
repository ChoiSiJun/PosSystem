import { createSlice } from '@reduxjs/toolkit';
import { jwtDecode } from 'jwt-decode';
import type { AuthState } from '@/features/auth/types/StateType';

const initialState: AuthState = {
  jwtToken: '',
  userName: 'GUEST',
  role: 'GUEST',
  status: 'idle',
  exp: null,
};

export const AuthSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    //인증값 저장
    authInsert: (state, action) => {
      const jwtToken = action.payload;
      state.jwtToken = jwtToken;

      const decodeJwt: {
        sub: string;
        userName: string;
        authority: string;
        exp: number;
      } = jwtDecode(jwtToken);

      const userName = decodeJwt.sub;
      const name = decodeJwt.userName;
      const role = decodeJwt.authority;
      const exp = decodeJwt.exp;

      if (userName == undefined) {
        state.status = 'fail';
        return;
      }

      state.userName = userName;
      state.role = role;
      state.exp = exp;
      state.status = 'success';
    },

    //인증값 삭제
    authDelete: (state) => {
      state.userName = initialState.userName;
      state.jwtToken = initialState.jwtToken;
      state.role = initialState.role;
      state.status = 'idle';
    },
  },
});

export const { authInsert, authDelete } = AuthSlice.actions;
export default AuthSlice.reducer;
