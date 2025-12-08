import { AxiosError } from 'axios';
import { useQueryClient } from '@tanstack/react-query';

import axios from '@/app/api/axios';
import { useMutationWithLoading } from '@/app/hook/useMutationWithLoading';
import { PRODUCT_API_ENDPOINTS } from '@/features/pos/api/productApi';
import type { ProductCreateRequest, ProductCreateApiResponse } from '@/features/pos/types/ApiType';

export const useProductCreateMutation = () => {
  const queryClient = useQueryClient();
  const endpoint = PRODUCT_API_ENDPOINTS.create;

  return useMutationWithLoading<ProductCreateApiResponse, AxiosError, ProductCreateRequest>({
    mutationFn: async (params: ProductCreateRequest) => {
      const response = await axios({
        url: endpoint.url,
        method: endpoint.method,
        data: params,
        timeout: 5000,
      });
      return response.data;
    },
    onSuccess: (
      _data: ProductCreateApiResponse,
      _variables: ProductCreateRequest,
      _context: unknown
    ) => {
      queryClient.invalidateQueries({ queryKey: ['products'] });
    },
  });
};
