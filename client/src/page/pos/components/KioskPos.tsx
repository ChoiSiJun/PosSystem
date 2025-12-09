import React, { useState, useCallback } from 'react';
import {
  Box,
  TextField,
  InputAdornment,
  Typography,
  Paper,
  Button,
  Dialog,
  useMediaQuery,
  useTheme,
} from '@mui/material';
import { Search as SearchIcon, Add as AddIcon } from '@mui/icons-material';
import { useProductListQuery } from '@/features/pos/hook/useProductListQuery';
import type { Product, CartItem } from '@/features/pos/types';
import ProductCard from './ProductCard';
import Cart from './Cart';
import Calculator from './Calculator';
import ProductForm from './ProductForm';
import { toast } from 'react-toastify';

const KioskPos: React.FC = () => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
  const [search, setSearch] = useState('');
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const [isProductFormOpen, setIsProductFormOpen] = useState(false);

  const { data, isLoading } = useProductListQuery({
    page: 1,
    pageSize: 100,
    search,
    status: 'ACTIVE',
  });

  const handleProductClick = useCallback(
    (product: Product) => {
      if (product.status === 'OUT_OF_STOCK' || product.stock === 0) {
        toast.error('품절된 상품입니다.');
        return;
      }

      const existingItem = cartItems.find((item) => item.productId === product.id);

      if (existingItem) {
        setCartItems((prev) =>
          prev.map((item) =>
            item.id === existingItem.id ? { ...item, quantity: item.quantity + 1 } : item
          )
        );
      } else {
        const newItem: CartItem = {
          id: `product-${product.id}-${Date.now()}`,
          productId: product.id,
          name: product.name,
          price: product.price,
          quantity: 1,
          imageUrl: product.imageUrl,
          isCustom: false,
        };
        setCartItems((prev) => [...prev, newItem]);
      }

      toast.success(`${product.name}이(가) 장바구니에 추가되었습니다.`);
    },
    [cartItems]
  );

  const handleRemoveItem = useCallback((id: string) => {
    setCartItems((prev) => prev.filter((item) => item.id !== id));
  }, []);

  const handleQuantityChange = useCallback((id: string, quantity: number) => {
    setCartItems((prev) => prev.map((item) => (item.id === id ? { ...item, quantity } : item)));
  }, []);

  const handleAddCustomItem = useCallback((amount: number, label: string) => {
    const newItem: CartItem = {
      id: `custom-${Date.now()}`,
      name: label,
      price: amount,
      quantity: 1,
      isCustom: true,
    };
    setCartItems((prev) => [...prev, newItem]);
    toast.success(`${label}이(가) 추가되었습니다.`);
  }, []);

  const handleCheckout = useCallback(() => {
    const totalAmount = cartItems.reduce((sum, item) => sum + item.price * item.quantity, 0);
    if (window.confirm(`총 ${totalAmount.toLocaleString()}원을 결제하시겠습니까?`)) {
      toast.success('결제가 완료되었습니다.');
      setCartItems([]);
    }
  }, [cartItems]);

  const filteredProducts =
    data?.products?.filter((product) => product.status === 'ACTIVE' && product.stock > 0) || [];

  const handleProductFormSuccess = useCallback(() => {
    setIsProductFormOpen(false);
  }, []);

  const handleProductFormCancel = useCallback(() => {
    setIsProductFormOpen(false);
  }, []);

  return (
    <Box sx={{ height: '100vh', display: 'flex', flexDirection: 'column', p: 2 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4" sx={{ fontWeight: 'bold' }}>
          POS 시스템
        </Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => setIsProductFormOpen(true)}
          sx={{ flexShrink: 0 }}
        >
          상품 등록
        </Button>
      </Box>

      <Box
        sx={{
          display: 'flex',
          gap: 2,
          flexGrow: 1,
          overflow: 'hidden',
          flexDirection: { xs: 'column', md: 'row' },
        }}
      >
        {/* 왼쪽: 상품 목록 */}
        <Box
          sx={{
            flex: { xs: '1 1 auto', md: '0 0 66.666%' },
            display: 'flex',
            flexDirection: 'column',
            overflow: 'hidden',
            minHeight: 0,
          }}
        >
          <Paper sx={{ p: 2, mb: 2 }}>
            <TextField
              fullWidth
              placeholder="상품명 또는 코드로 검색"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <SearchIcon />
                  </InputAdornment>
                ),
              }}
            />
          </Paper>

          <Box sx={{ flexGrow: 1, overflow: 'auto', pr: 1 }}>
            {isLoading ? (
              <Box sx={{ textAlign: 'center', py: 4 }}>
                <Typography>로딩 중...</Typography>
              </Box>
            ) : filteredProducts.length === 0 ? (
              <Box sx={{ textAlign: 'center', py: 4 }}>
                <Typography color="text.secondary">등록된 상품이 없습니다.</Typography>
              </Box>
            ) : (
              <Box
                sx={{
                  display: 'grid',
                  gridTemplateColumns: {
                    xs: 'repeat(2, 1fr)',
                    sm: 'repeat(3, 1fr)',
                    md: 'repeat(4, 1fr)',
                  },
                  gap: 2,
                }}
              >
                {filteredProducts.map((product) => (
                  <ProductCard
                    key={product.id}
                    product={product}
                    onClick={() => handleProductClick(product)}
                  />
                ))}
              </Box>
            )}
          </Box>
        </Box>

        {/* 오른쪽: 결제 영역 */}
        <Box
          sx={{
            flex: { xs: '1 1 auto', md: '0 0 33.333%' },
            display: 'flex',
            flexDirection: 'column',
            gap: 2,
            overflow: 'hidden',
            minHeight: 0,
          }}
        >
          {/* 장바구니 */}
          <Box sx={{ flex: '1 1 60%', minHeight: 0 }}>
            <Cart
              items={cartItems}
              onRemoveItem={handleRemoveItem}
              onQuantityChange={handleQuantityChange}
              onCheckout={handleCheckout}
            />
          </Box>

          {/* 계산기 */}
          <Box sx={{ flex: '1 1 40%', minHeight: 0 }}>
            <Calculator onAddCustomItem={handleAddCustomItem} />
          </Box>
        </Box>
      </Box>

      {/* 상품 등록 다이얼로그 */}
      <Dialog
        open={isProductFormOpen}
        onClose={handleProductFormCancel}
        maxWidth="md"
        fullWidth
        fullScreen={isMobile}
      >
        <Box sx={{ p: { xs: 2, sm: 3 } }}>
          <ProductForm onSuccess={handleProductFormSuccess} onCancel={handleProductFormCancel} />
        </Box>
      </Dialog>
    </Box>
  );
};

export default KioskPos;
