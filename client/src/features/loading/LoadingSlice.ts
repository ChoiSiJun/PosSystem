import { createSlice } from '@reduxjs/toolkit';

export interface LoadingState {
  isLoading: boolean;
}

const initialState: LoadingState = {
  isLoading: false, // 기본값은 로딩 없음
};

const loadingSlice = createSlice({
  name: 'loading',
  initialState,
  reducers: {
    lodingOn(state) {
      state.isLoading = true;
    },
    lodingOff(state) {
      state.isLoading = false;
    },
  },
});

export const { lodingOn, lodingOff } = loadingSlice.actions;
export default loadingSlice.reducer;
