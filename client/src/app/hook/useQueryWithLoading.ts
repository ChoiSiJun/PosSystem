import { useQuery, UseQueryOptions, UseQueryResult } from 'react-query';
import { useAppDispatch } from 'store/ReduxHooks';
import { globalLoadingOn, globalLoadingOff } from '@store/slice/LoadingSlice';
import apiErrorHandler from '@config/handlers/apiErrorHandler';
import { useEffect } from 'react';
import { AxiosError } from 'axios';
import { useNavigate } from 'react-router-dom';

export const useQueryWithLoading = <
  TQueryFnData = unknown,
  TError = unknown,
  TData = TQueryFnData,
  TQueryKey extends readonly unknown[] = unknown[],
>(
  options: UseQueryOptions<TQueryFnData, TError, TData, TQueryKey>
): UseQueryResult<TData, TError> => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const queryResult = useQuery({
    ...options,

    retry: (failureCount, error) => {
      if (error instanceof AxiosError && [401, 403].includes(error.response?.status ?? 0)) {
        return false;
      }

      return failureCount < 3; // 기본 3회 재시도
    },

    onError: (error) => {
      if (error instanceof AxiosError && [403].includes(error.response?.status ?? 0)) {
        navigate('/error');
        return false;
      }

      if (error instanceof AxiosError && [401].includes(error.response?.status ?? 0)) {
        navigate('/');
        return false;
      }

      if (options.onError) {
        options.onError(error);
      } else {
        apiErrorHandler(error);
      }
    },

    onSuccess: (data) => {
      options.onSuccess?.(data);
    },

    onSettled: (data, error) => {
      options.onSettled?.(data, error);
      dispatch(globalLoadingOff());
    },
  });

  useEffect(() => {
    if (queryResult.isLoading) {
      dispatch(globalLoadingOn());
    }
  }, [queryResult.isLoading, dispatch]);

  return queryResult;
};
