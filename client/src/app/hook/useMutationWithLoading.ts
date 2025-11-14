// hooks/useMutationWithLoading.ts
import { useMutation, UseMutationOptions, UseMutationResult } from 'react-query';
import { useAppDispatch } from 'store/ReduxHooks';
import { mutationLoadingOn, mutationLoadingOff } from '@store/slice/LoadingSlice';
import apiErrorHandler from '@config/handlers/apiErrorHandler';
import { AxiosError } from 'axios';

export const useMutationWithLoading = <TData = unknown, TError = unknown, TVariables = void>(
  options?: UseMutationOptions<TData, TError, TVariables> // options를 optional로
): UseMutationResult<TData, TError, TVariables> => {
  const dispatch = useAppDispatch();
  return useMutation({
    ...(options ?? {}), // options가 없으면 빈 객체를 대입
    onMutate: async (variables) => {
      dispatch(mutationLoadingOn());
      if (options?.onMutate) {
        await options.onMutate(variables);
      }
    },
    retry: (failureCount, error) => {
      if (error instanceof AxiosError && [401, 403, 404].includes(error.response?.status ?? 0)) {
        return false;
      }

      return failureCount < 3; // 기본 3회 재시도
    },
    onSuccess: (data, variables, context) => {
      dispatch(mutationLoadingOff());
      if (options?.onSuccess) {
        options.onSuccess(data, variables, context);
      }
    },
    onError: (error, variables, context) => {
      dispatch(mutationLoadingOff());
      if (options?.onError) {
        options.onError(error, variables, context);
      } else if (error) {
        apiErrorHandler(error);
      }
    },
    onSettled: (data, error, variables, context) => {
      dispatch(mutationLoadingOff());
      if (options?.onSettled) {
        options.onSettled(data, error, variables, context);
      }
    },
  });
};
