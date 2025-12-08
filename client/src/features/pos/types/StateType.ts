import type { Product } from '.';

export interface ProductState {
  selectedProduct: Product | null;
  filters: {
    search: string;
    status: string;
    page: number;
    pageSize: number;
  };
}

