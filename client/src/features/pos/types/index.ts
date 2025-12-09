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

/** API 요청/응답 타입 **/

// 상품 목록 조회 요청
export interface ProductListRequest {
  page?: number;
  pageSize?: number;
  search?: string;
  status?: string;
}

// 상품 등록 요청
export interface ProductCreateRequest extends Omit<Product, 'id'> {}

// 상품 수정 요청
export interface ProductUpdateRequest extends Partial<Omit<Product, 'id'>> {
  id: number;
}

// 상품 삭제 요청
export interface ProductDeleteRequest {
  id: number;
}

// API 응답 타입

// 상품 목록 조회 응답
export interface ProductListApiResponse extends ProductListResponse {}

// 상품 상세 조회 응답
export interface ProductDetailApiResponse extends ProductDetailResponse {}

// 상품 수정 응답
export interface ProductUpdateApiResponse extends Product {}

// 상품 삭제 응답
export interface ProductDeleteApiResponse {
  success: boolean;
  message?: string;
}

/** 상태 타입 **/
export const ProductStatus = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
  OUT_OF_STOCK: 'OUT_OF_STOCK',
} as const;

export type ProductStatus = (typeof ProductStatus)[keyof typeof ProductStatus];

// 상품 목록 응답
export interface ProductListResponse {
  products: Product[];
  total: number;
  page: number;
  pageSize: number;
}
1;

// 상품 상세 응답
export interface ProductDetailResponse extends Product {}
