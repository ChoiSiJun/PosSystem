export interface Account {
  userName: string;
  password: string;
  email: string;
}

export interface Auth {
  jwtToken: string;
  userName: string;
  role: string;
  status: string;
  exp: number | null;
}
