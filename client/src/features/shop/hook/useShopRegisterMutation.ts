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

      if (!apiResponse.success) {
        // success가 false라면, 서버에서 정의된 message를 사용하여 에러 발생
        throw new Error(apiResponse.message || '등록에 실패했습니다.');
      }

      return apiResponse.data;
    },
  });
};
