import { AxiosError } from 'axios';
import { AUTH_API_ENDPOINTS } from '@/features/auth/api/authApi';
import { useMutationWithLoading } from '@/app/hook/useMutationWithLoading';
import axios from '@/app/api/axios';
import type { RegisterRequest } from '@/features/auth/types/ApiType';

export const useRegisterMutation = () => {
  const endpoint = AUTH_API_ENDPOINTS.register;

  return useMutationWithLoading<void, AxiosError, RegisterRequest>({
    mutationFn: async (params: RegisterRequest) => {
      await axios({
        method: endpoint.method,
        url: endpoint.url,
        data: params,
        timeout: 5000,
      });
    },
  });
};
