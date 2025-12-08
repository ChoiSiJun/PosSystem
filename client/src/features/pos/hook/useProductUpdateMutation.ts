import { AxiosError } from 'axios';
import { useQueryClient } from '@tanstack/react-query';

import axios from '@/app/api/axios';
import { useMutationWithLoading } from '@/app/hook/useMutationWithLoading';
import { PRODUCT_API_ENDPOINTS } from '@/features/pos/api/productApi';
import type { ProductUpdateRequest, ProductUpdateApiResponse } from '@/features/pos/types/ApiType';

export const useProductUpdateMutation = () => {
  const queryClient = useQueryClient();
  const endpoint = PRODUCT_API_ENDPOINTS.update;

  return useMutationWithLoading<ProductUpdateApiResponse, AxiosError, ProductUpdateRequest>({
    mutationFn: async (params: ProductUpdateRequest) => {
      const { id, ...data } = params;
      const response = await axios({
        url: endpoint.url(id),
        method: endpoint.method,
        data,
        timeout: 5000,
      });
      return response.data;
    },
    onSuccess: (
      data: ProductUpdateApiResponse,
      _variables: ProductUpdateRequest,
      _context: unknown
    ) => {
      queryClient.invalidateQueries({ queryKey: ['products'] });
      if (data.id) {
        queryClient.invalidateQueries({ queryKey: ['products', 'detail', data.id] });
      }
    },
  });
};
