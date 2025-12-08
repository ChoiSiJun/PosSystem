import type { Product, ProductListResponse, ProductDetailResponse } from '.';

export interface ProductListRequest {
  page?: number;
  pageSize?: number;
  search?: string;
  status?: string;
}

export interface ProductCreateRequest extends Omit<Product, 'id' | 'createdAt' | 'updatedAt'> {}

export interface ProductUpdateRequest extends Partial<Omit<Product, 'id' | 'createdAt' | 'updatedAt'>> {
  id: number;
}

export interface ProductDeleteRequest {
  id: number;
}

export interface ProductListApiResponse extends ProductListResponse {}

export interface ProductDetailApiResponse extends ProductDetailResponse {}

export interface ProductCreateApiResponse extends Product {}

export interface ProductUpdateApiResponse extends Product {}

export interface ProductDeleteApiResponse {
  success: boolean;
  message?: string;
}

