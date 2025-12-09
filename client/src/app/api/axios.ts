import store from '@store/ReduxStoreConfig';
import axios from 'axios';

const instance = axios.create({
  baseURL: import.meta.env.VITE_REST_API,
  timeout: 5000,
});

instance.interceptors.request.use(
  (config) => {
    const state = store.getState();
    const jwt = state.shop.jwtToken;

    if (jwt) {
      config.headers.Authorization = `Bearer ${jwt}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default instance;
