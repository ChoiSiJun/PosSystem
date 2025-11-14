const authApiBase = '/auth';

export const AUTH_API_ENDPOINTS = {
  //ID 중복 체크
  duplicateCheckId: {
    url: authApiBase + '/duplicate',
    method: 'GET',
  },

  //등록
  register: {
    url: authApiBase,
    method: 'POST',
  },

  //로그인
  login: {
    url: authApiBase + '/login',
    method: 'POST',
  },
};
