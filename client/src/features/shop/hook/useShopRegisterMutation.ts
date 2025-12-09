import { AxiosError } from 'axios';

import axios from '@/app/api/axios';
import { useMutationWithLoading } from '@/app/hook/useMutationWithLoading';
import { SHOP_API_ENDPOINTS } from '@/features/shop/api/shopApi';
import type { ShopRegisterRequest } from '@/features/shop/types';
import type { ApiResponse } from '@/app/type';

export const useShopRegisterMutation = () => {
  const endpoint = SHOP_API_ENDPOINTS.register;

  return useMutationWithLoading<ApiResponse<void>, AxiosError, ShopRegisterRequest>({
    mutationFn: async (params: ShopRegisterRequest) => {
      const response = await axios({
        url: endpoint.url,
        method: endpoint.method,
        data: params,
        timeout: 5000,
      });

      const apiResponse = response.data;
      return apiResponse.data;
    },
  });
};

