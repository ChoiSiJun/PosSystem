const productApiBase = '/products';

export const PRODUCT_API_ENDPOINTS = {
  // 상품 목록 조회
  list: {
    url: productApiBase,
    method: 'GET',
  },

  // 상품 상세 조회
  detail: {
    url: (id: number) => `${productApiBase}/${id}`,
    method: 'GET',
  },

  // 상품 등록
  create: {
    url: productApiBase,
    method: 'POST',
  },

  // 상품 수정
  update: {
    url: (id: number) => `${productApiBase}/${id}`,
    method: 'PUT',
  },

  // 상품 삭제
  delete: {
    url: (id: number) => `${productApiBase}/${id}`,
    method: 'DELETE',
  },
};

