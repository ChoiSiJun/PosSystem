import { AxiosError } from 'axios';
import { useQueryClient } from '@tanstack/react-query';

import axios from '@/app/api/axios';
import { useMutationWithLoading } from '@/app/hook/useMutationWithLoading';
import { PRODUCT_API_ENDPOINTS } from '@/features/pos/api/productApi';
import type { ProductDeleteRequest, ProductDeleteApiResponse } from '@/features/pos/types/ApiType';

export const useProductDeleteMutation = () => {
  const queryClient = useQueryClient();
  const endpoint = PRODUCT_API_ENDPOINTS.delete;

  return useMutationWithLoading<ProductDeleteApiResponse, AxiosError, ProductDeleteRequest>({
    mutationFn: async (params: ProductDeleteRequest) => {
      const response = await axios({
        url: endpoint.url(params.id),
        method: endpoint.method,
        timeout: 5000,
      });
      return response.data;
    },
    onSuccess: (
      _data: ProductDeleteApiResponse,
      _variables: ProductDeleteRequest,
      _context: unknown
    ) => {
      queryClient.invalidateQueries({ queryKey: ['products'] });
    },
  });
};
