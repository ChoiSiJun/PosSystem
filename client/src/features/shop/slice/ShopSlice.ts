import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import type { ShopAuthState, ShopLoginResponsePayload } from '@features/shop/types';
import { jwtDecode } from 'jwt-decode';

const initialState: ShopAuthState = {
  shopCode: '',
  shopName: '',
  jwtToken: '',
};

const shopSlice = createSlice({
  name: 'shop',
  initialState,
  reducers: {
    shopLogin: (state, action: PayloadAction<string>) => {
      const decodedPayload = jwtDecode<ShopLoginResponsePayload>(action.payload);

      state.jwtToken = action.payload;
      state.shopCode = decodedPayload.shopCode;
      state.shopName = decodedPayload.shopName;
    },
    shopLogout: (state) => {
      state.jwtToken = '';
      state.shopCode = '';
      state.shopName = '';
    },
  },
});

export const { shopLogin, shopLogout } = shopSlice.actions;
export default shopSlice.reducer;
