import { AxiosError } from 'axios';

import axios from '@/app/api/axios';
import { useMutationWithLoading } from '@/app/hook/useMutationWithLoading';
import { SHOP_API_ENDPOINTS } from '@/features/shop/api/shopApi';
import type { ShopLoginRequest } from '@/features/shop/types';
import type { ApiResponse } from '@/app/type';

export const useShopLoginMutation = () => {
  const endpoint = SHOP_API_ENDPOINTS.login;

  return useMutationWithLoading<ApiResponse<string>, AxiosError, ShopLoginRequest>({
    mutationFn: async (params: ShopLoginRequest) => {
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
