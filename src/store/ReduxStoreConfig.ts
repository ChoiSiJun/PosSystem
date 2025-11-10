import { combineReducers, configureStore } from '@reduxjs/toolkit';

import { persistReducer } from 'redux-persist';
import sessionStorage from 'redux-persist/lib/storage/session';
import loadingSlice from './slice/loadingSlice';

const reducers = combineReducers({ loading: loadingSlice });

const persistConfig = {
  key: 'root',
  storage: sessionStorage,
  whitelist: ['Menu'],
};

const persistedReducer = persistReducer(persistConfig, reducers);
export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) => getDefaultMiddleware({ serializableCheck: false }),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
