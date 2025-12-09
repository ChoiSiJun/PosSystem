import { AxiosError } from 'axios';
import { useQueryClient } from '@tanstack/react-query';

import axios from '@/app/api/axios';
import { useMutationWithLoading } from '@/app/hook/useMutationWithLoading';
import { PRODUCT_API_ENDPOINTS } from '@/features/pos/api/productApi';
import type { Product } from '@/features/pos/types';
import type { ApiResponse } from '@/app/type';

export const useProductCreateMutation = () => {
  const queryClient = useQueryClient();
  const endpoint = PRODUCT_API_ENDPOINTS.create;

  return useMutationWithLoading<ApiResponse<Product>, AxiosError, FormData>({
    mutationFn: async (formData: FormData) => {
      const response = await axios({
        url: endpoint.url,
        method: endpoint.method,
        data: formData,
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        timeout: 10000,
      });
      return response.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['products'] });
    },
  });
};
