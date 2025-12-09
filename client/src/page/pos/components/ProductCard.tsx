import React from 'react';
import { Card, CardContent, CardMedia, Typography, Box, Chip } from '@mui/material';
import { type Product, ProductStatus } from '@/features/pos/types';

interface ProductCardProps {
  product: Product;
  onClick: () => void;
}

const ProductCard: React.FC<ProductCardProps> = ({ product, onClick }) => {
  const isOutOfStock = product.status === ProductStatus.OUT_OF_STOCK || product.stock === 0;

  return (
    <Card
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        cursor: isOutOfStock ? 'not-allowed' : 'pointer',
        opacity: isOutOfStock ? 0.6 : 1,
        transition: 'transform 0.2s, box-shadow 0.2s',
        '&:hover': {
          transform: isOutOfStock ? 'none' : 'translateY(-4px)',
          boxShadow: isOutOfStock ? 2 : 6,
        },
      }}
      onClick={isOutOfStock ? undefined : onClick}
    >
      <CardMedia
        component="img"
        height="200"
        image={product.imageUrl || '/placeholder-image.png'}
        alt={product.name}
        sx={{
          objectFit: 'cover',
          backgroundColor: '#f5f5f5',
        }}
      />
      <CardContent sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column' }}>
        <Typography variant="h6" component="h3" sx={{ mb: 1, fontWeight: 'bold' }}>
          {product.name}
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 1, flexGrow: 1 }}>
          {product.description}
        </Typography>
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            mt: 'auto',
          }}
        >
          <Typography variant="h6" color="primary" sx={{ fontWeight: 'bold' }}>
            {product.price.toLocaleString()}원
          </Typography>
          {isOutOfStock && <Chip label="품절" color="error" size="small" />}
        </Box>
      </CardContent>
    </Card>
  );
};

export default ProductCard;

