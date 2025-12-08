import React from 'react';
import { Box, Paper, Typography, Button, Chip, Divider, Stack } from '@mui/material';
import { ArrowBack as ArrowBackIcon, Edit as EditIcon } from '@mui/icons-material';
import { useProductDetailQuery } from '@/features/pos/hook/useProductDetailQuery';
import { ProductStatus } from '@/features/pos/types';

interface ProductDetailProps {
  productId: number;
  onBack: () => void;
  onEdit: () => void;
}

const ProductDetail: React.FC<ProductDetailProps> = ({ productId, onBack, onEdit }) => {
  const { data: product, isLoading } = useProductDetailQuery(productId);

  const getStatusColor = (status: ProductStatus) => {
    switch (status) {
      case ProductStatus.ACTIVE:
        return 'success';
      case ProductStatus.INACTIVE:
        return 'default';
      case ProductStatus.OUT_OF_STOCK:
        return 'error';
      default:
        return 'default';
    }
  };

  const getStatusLabel = (status: ProductStatus) => {
    switch (status) {
      case ProductStatus.ACTIVE:
        return '활성';
      case ProductStatus.INACTIVE:
        return '비활성';
      case ProductStatus.OUT_OF_STOCK:
        return '품절';
      default:
        return status;
    }
  };

  if (isLoading) {
    return (
      <Box>
        <Typography>로딩 중...</Typography>
      </Box>
    );
  }

  if (!product) {
    return (
      <Box>
        <Typography>상품을 찾을 수 없습니다.</Typography>
        <Button onClick={onBack} sx={{ mt: 2 }}>
          목록으로 돌아가기
        </Button>
      </Box>
    );
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4" component="h1">
          상품 상세
        </Typography>
        <Box sx={{ display: 'flex', gap: 2 }}>
          <Button variant="outlined" startIcon={<ArrowBackIcon />} onClick={onBack}>
            목록
          </Button>
          <Button variant="contained" startIcon={<EditIcon />} onClick={onEdit}>
            수정
          </Button>
        </Box>
      </Box>

      <Paper sx={{ p: 3 }}>
        <Stack spacing={3}>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <Typography variant="h5" component="h2">
              {product.name}
            </Typography>
            <Chip
              label={getStatusLabel(product.status)}
              color={getStatusColor(product.status) as any}
              size="medium"
            />
          </Box>

          <Divider />

          <Box sx={{ display: 'flex', gap: 2, flexDirection: { xs: 'column', md: 'row' } }}>
            <Box sx={{ flex: 1 }}>
              <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                상품 코드
              </Typography>
              <Typography variant="body1">{product.code}</Typography>
            </Box>
            <Box sx={{ flex: 1 }}>
              <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                상태
              </Typography>
              <Chip
                label={getStatusLabel(product.status)}
                color={getStatusColor(product.status) as any}
                size="small"
              />
            </Box>
          </Box>

          <Box>
            <Typography variant="subtitle2" color="text.secondary" gutterBottom>
              설명
            </Typography>
            <Typography variant="body1" sx={{ whiteSpace: 'pre-wrap' }}>
              {product.description}
            </Typography>
          </Box>

          <Box sx={{ display: 'flex', gap: 2, flexDirection: { xs: 'column', md: 'row' } }}>
            <Box sx={{ flex: 1 }}>
              <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                가격
              </Typography>
              <Typography variant="h6" color="primary">
                {product.price.toLocaleString()}원
              </Typography>
            </Box>
            <Box sx={{ flex: 1 }}>
              <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                재고
              </Typography>
              <Typography variant="h6">{product.stock}개</Typography>
            </Box>
          </Box>

          {(product.createdAt || product.updatedAt) && (
            <Box sx={{ display: 'flex', gap: 2, flexDirection: { xs: 'column', md: 'row' } }}>
              {product.createdAt && (
                <Box sx={{ flex: 1 }}>
                  <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                    등록일
                  </Typography>
                  <Typography variant="body2">
                    {new Date(product.createdAt).toLocaleString('ko-KR')}
                  </Typography>
                </Box>
              )}
              {product.updatedAt && (
                <Box sx={{ flex: 1 }}>
                  <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                    수정일
                  </Typography>
                  <Typography variant="body2">
                    {new Date(product.updatedAt).toLocaleString('ko-KR')}
                  </Typography>
                </Box>
              )}
            </Box>
          )}
        </Stack>
      </Paper>
    </Box>
  );
};

export default ProductDetail;
