const shopApiBase = 'api/shop';

export const SHOP_API_ENDPOINTS = {
  // 매장 등록
  register: {
    url: shopApiBase,
    method: 'POST',
  },

  // 매장 로그인
  login: {
    url: shopApiBase + '/login',
    method: 'POST',
  },
};
