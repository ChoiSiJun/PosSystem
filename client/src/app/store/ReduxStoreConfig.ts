import { combineReducers, configureStore } from '@reduxjs/toolkit';

import { persistReducer } from 'redux-persist';
import sessionStorage from 'redux-persist/lib/storage/session';
import LoadingSlice from '@/features/loading/LoadingSlice';
import ShopSlice from '@/features/shop/slice/ShopSlice';

const reducers = combineReducers({
  loading: LoadingSlice,
  shop: ShopSlice,
});

const persistConfig = {
  key: 'root',
  storage: sessionStorage,
  whitelist: ['auth'],
};

const persistedReducer = persistReducer(persistConfig, reducers);
export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) => getDefaultMiddleware({ serializableCheck: false }),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
