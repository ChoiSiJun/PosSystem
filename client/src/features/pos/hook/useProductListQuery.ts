import axios from '@/app/api/axios';
import { useQueryWithLoading } from '@/app/hook/useQueryWithLoading';
import { PRODUCT_API_ENDPOINTS } from '@/features/pos/api/productApi';
import type { ProductListRequest, ProductListApiResponse } from '@/features/pos/types/ApiType';

export const useProductListQuery = (params: ProductListRequest = {}) => {
  const endpoint = PRODUCT_API_ENDPOINTS.list;

  return useQueryWithLoading<ProductListApiResponse>({
    queryKey: ['products', 'list', params],
    queryFn: async () => {
      const response = await axios({
        url: endpoint.url,
        method: endpoint.method,
        params: {
          page: params.page || 1,
          pageSize: params.pageSize || 10,
          search: params.search || '',
          status: params.status || '',
        },
        timeout: 5000,
      });
      return response.data;
    },
  });
};
