import type { Account } from '.';

export interface LoginRequest extends Pick<Account, 'userId' | 'password'> {}

export interface LoginResponse extends Pick<Account, 'userId' | 'password'> {}

export interface RegisterRequest extends Account {}
