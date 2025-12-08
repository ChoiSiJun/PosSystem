import { useQuery, type QueryFunctionContext, type UseQueryResult } from '@tanstack/react-query';
import { useEffect } from 'react';

interface UseSimpleQueryOptions<TQueryKey extends readonly unknown[], TData, TSelected = TData> {
  queryKey: TQueryKey;
  queryFn: (context: QueryFunctionContext<TQueryKey>) => Promise<TData>;
  enabled?: boolean;
  select?: (data: TData) => TSelected;
  onSuccess?: (data: TSelected) => void;
  onError?: (error: unknown) => void;
}

export const useQueryWithLoading = <
  TQueryFnData = unknown,
  TQueryKey extends readonly unknown[] = readonly unknown[],
  TSelected = TQueryFnData,
>(
  options: UseSimpleQueryOptions<TQueryKey, TQueryFnData, TSelected>
): UseQueryResult<TSelected, unknown> => {
  const wrappedQueryFn = async (
    context: QueryFunctionContext<TQueryKey>
  ): Promise<TQueryFnData> => {
    // dispatch(lodingOn());
    try {
      return await options.queryFn(context);
    } finally {
      // dispatch(lodingOff());
    }
  };

  const queryOptions = {
    queryKey: options.queryKey,
    queryFn: wrappedQueryFn,
    enabled: options.enabled,
    select: options.select,
    staleTime: 1000 * 60 * 5,
    retry: 1,
    placeholderData: (previousData: TSelected | undefined) => previousData,
  };

  const queryResult = useQuery<TQueryFnData, unknown, TSelected, TQueryKey>(queryOptions as any);

  // useEffect에 options를 의존성으로 추가합니다.
  useEffect(() => {
    if (queryResult.isSuccess && options.onSuccess && queryResult.data) {
      options.onSuccess(queryResult.data);
    }
  }, [queryResult.isSuccess, queryResult.data, options]); // options 추가

  // useEffect에 options를 의존성으로 추가합니다.
  useEffect(() => {
    if (queryResult.isError && options.onError && queryResult.error) {
      options.onError(queryResult.error);
    }
  }, [queryResult.isError, queryResult.error, options]); // options 추가

  return queryResult;
};
