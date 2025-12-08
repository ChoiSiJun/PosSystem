import axios from '@/app/api/axios';
import { useQueryWithLoading } from '@/app/hook/useQueryWithLoading';
import { PRODUCT_API_ENDPOINTS } from '@/features/pos/api/productApi';
import type { ProductDetailApiResponse } from '@/features/pos/types/ApiType';

export const useProductDetailQuery = (id: number | null) => {
  const endpoint = PRODUCT_API_ENDPOINTS.detail;

  return useQueryWithLoading<ProductDetailApiResponse>({
    queryKey: ['products', 'detail', id],
    queryFn: async () => {
      if (!id) {
        throw new Error('Product ID is required');
      }
      const response = await axios({
        url: endpoint.url(id),
        method: endpoint.method,
        timeout: 5000,
      });
      return response.data;
    },
    enabled: !!id,
  });
};
