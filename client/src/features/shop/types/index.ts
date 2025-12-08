import type { JwtPayload } from 'jwt-decode';

export interface Shop {
  shopCode: string;
  shopName: string;
  password: string;
  adminPassword: string;
}

/** 상태 타입 **/

export interface ShopAuthState extends Pick<Shop, 'shopCode' | 'shopName'> {
  jwtToken: string;
}

/** API 요청/응답 타입 **/

/** 로그인 요청 **/
export interface ShopLoginRequest extends Pick<Shop, 'shopCode' | 'password'> {}

/** 로그인 응답 **/
export interface ShopLoginResponse {
  jwtToken: string;
}

export interface ShopLoginResponsePayload extends JwtPayload {
  shopCode: string;
  shopName: string;
}

/** 매장 등록 요청 **/
export interface ShopRegisterRequest extends Shop {}
