import { AxiosError } from 'axios';
import { useQueryClient } from '@tanstack/react-query';

import axios from '@/app/api/axios';
import { useMutationWithLoading } from '@/app/hook/useMutationWithLoading';
import { PRODUCT_API_ENDPOINTS } from '@/features/pos/api/productApi';
import type { Product } from '@/features/pos/types';
import type { ApiResponse } from '@/app/type';

interface ProductUpdateParams {
  id: number;
  formData: FormData;
}

export const useProductUpdateMutation = () => {
  const queryClient = useQueryClient();
  const endpoint = PRODUCT_API_ENDPOINTS.update;

  return useMutationWithLoading<ApiResponse<Product>, AxiosError, ProductUpdateParams>({
    mutationFn: async (params: ProductUpdateParams) => {
      const { id, formData } = params;
      const response = await axios({
        url: endpoint.url(id),
        method: endpoint.method,
        data: formData,
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        timeout: 10000,
      });
      return response.data;
    },
    onSuccess: (data, variables) => {
      queryClient.invalidateQueries({ queryKey: ['products'] });
      if (variables.id) {
        queryClient.invalidateQueries({ queryKey: ['products', 'detail', variables.id] });
      }
    },
  });
};
