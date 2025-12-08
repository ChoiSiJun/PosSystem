export interface Product {
  id?: number;
  code: string;
  name: string;
  description: string;
  price: number;
  stock: number;
  status: ProductStatus;
  imageUrl?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface CartItem {
  id: string;
  productId?: number;
  name: string;
  price: number;
  quantity: number;
  imageUrl?: string;
  isCustom?: boolean; // 계산기로 추가한 기타 상품인지 여부
}

export const ProductStatus = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
  OUT_OF_STOCK: 'OUT_OF_STOCK',
} as const;

export type ProductStatus = (typeof ProductStatus)[keyof typeof ProductStatus];

export interface ProductListResponse {
  products: Product[];
  total: number;
  page: number;
  pageSize: number;
}

export interface ProductDetailResponse extends Product {}
