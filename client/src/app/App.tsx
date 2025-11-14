import ReactDOM from 'react-dom/client';

import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { persistStore } from 'redux-persist';
import { PersistGate } from 'redux-persist/integration/react';
import { CssBaseline } from '@mui/material';

import { QueryClientProvider, QueryClient, QueryCache } from '@tanstack/react-query';

import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import ReactToast from '@/app/components/feedback/ReactToast';
import store from '@/app/store/ReduxStoreConfig';
import apiErrorHandler from '@/app/api/apiErrorHandler';
import Loading from '@/app/components/feedback/Loading';
import { ToastProvider } from '@/app/contexts/ToastContext';
import DnniteRouter from '@/app/routes';
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
