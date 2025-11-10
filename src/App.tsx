import ReactDOM from 'react-dom/client';

import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { persistStore } from 'redux-persist';
import { PersistGate } from 'redux-persist/integration/react';
import { CssBaseline } from '@mui/material';

import { QueryClientProvider, QueryClient, QueryCache } from '@tanstack/react-query';

import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import ReactToast from '@components/feedback/ReactToast';
import store from '@store/ReduxStoreConfig';
import apiErrorHandler from '@api/apiErrorHandler';
import Loading from '@components/feedback/Loading';
import { ToastProvider } from '@contexts/ToastContext';
import DnniteRouter from '@/routes';
const persistor = persistStore(store);
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnMount: 'always',
      refetchOnWindowFocus: false,
      refetchOnReconnect: false,
    },
    mutations: { onError: apiErrorHandler },
  },
  queryCache: new QueryCache({ onError: apiErrorHandler }),
});

ReactDOM.createRoot(document.getElementById('root')!).render(
  <>
    <CssBaseline />
    <Provider store={store}>
      <QueryClientProvider client={queryClient}>
        <PersistGate loading={null} persistor={persistor}>
          <ToastProvider>
            <BrowserRouter>
              <DnniteRouter />
            </BrowserRouter>
          </ToastProvider>
          <ReactToast />
          <Loading />
        </PersistGate>
        <ReactQueryDevtools initialIsOpen={false} />
      </QueryClientProvider>
    </Provider>
  </>
);
