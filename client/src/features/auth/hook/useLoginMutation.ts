import { AxiosError } from 'axios';

import axios from '@/app/api/axios';
import { useMutationWithLoading } from '@/app/hook/useMutationWithLoading';
import { AUTH_API_ENDPOINTS } from '@/features/auth/api/authApi';
import type { LoginRequest } from '@/features/auth/types/ApiType';

export const useLoginMutation = () => {
  const endpoint = AUTH_API_ENDPOINTS.login;
  return useMutationWithLoading<string, AxiosError, LoginRequest>({
    mutationFn: async (params: LoginRequest) => {
      const response = await axios({
        url: endpoint.url,
        method: endpoint.method,
        data: params,
        timeout: 5000,
      });
      return response.data;
    },
  });
};
