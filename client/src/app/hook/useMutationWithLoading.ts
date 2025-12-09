// hooks/useMutationWithLoading.ts

import {
  useMutation,
  type UseMutationOptions,
  type UseMutationResult,
} from '@tanstack/react-query';
import { useAppDispatch } from '../store/ReduxHooks';
import { lodingOff, lodingOn } from '@/features/loading/LoadingSlice';

export const useMutationWithLoading = <TData = unknown, TError = unknown, TVariables = void>(
  options?: UseMutationOptions<TData, TError, TVariables>
): UseMutationResult<TData, TError, TVariables> => {
  const dispatch = useAppDispatch();

  return useMutation({
    ...options,

    onMutate: async (variables, context) => {
      dispatch(lodingOn());
      await options?.onMutate?.(variables, context);
    },

    onSettled: (data, error, variables, context, mutation) => {
      dispatch(lodingOff());
      options?.onSettled?.(data, error, variables, context, mutation);
    },

    // Add the 'mutation' argument to the onSuccess callback.
  });
};
