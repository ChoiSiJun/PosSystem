import React from 'react';
import {
  Box,
  Paper,
  Typography,
  List,
  ListItem,
  ListItemText,
  IconButton,
  Button,
  Divider,
  Stack,
} from '@mui/material';
import { Delete as DeleteIcon } from '@mui/icons-material';
import { type CartItem } from '@/features/pos/types';

interface CartProps {
  items: CartItem[];
  onRemoveItem: (id: string) => void;
  onQuantityChange: (id: string, quantity: number) => void;
  onCheckout: () => void;
}

const Cart: React.FC<CartProps> = ({ items, onRemoveItem, onQuantityChange, onCheckout }) => {
  const totalAmount = items.reduce((sum, item) => sum + item.price * item.quantity, 0);

  return (
    <Paper sx={{ p: 2, height: '100%', display: 'flex', flexDirection: 'column' }}>
      <Typography variant="h5" sx={{ mb: 2, fontWeight: 'bold' }}>
        결제 목록
      </Typography>

      <Box sx={{ flexGrow: 1, overflow: 'auto', mb: 2 }}>
        {items.length === 0 ? (
          <Box sx={{ textAlign: 'center', py: 4 }}>
            <Typography color="text.secondary">장바구니가 비어있습니다</Typography>
          </Box>
        ) : (
          <List>
            {items.map((item) => (
              <React.Fragment key={item.id}>
                <ListItem
                  sx={{
                    px: 0,
                    py: 1.5,
                  }}
                >
                  {item.imageUrl && (
                    <Box
                      component="img"
                      src={item.imageUrl}
                      alt={item.name}
                      sx={{
                        width: 60,
                        height: 60,
                        objectFit: 'cover',
                        borderRadius: 1,
                        mr: 2,
                      }}
                    />
                  )}
                  <ListItemText
                    primary={item.name}
                    secondary={
                      <Box>
                        <Typography variant="body2" color="text.secondary">
                          {item.price.toLocaleString()}원 × {item.quantity}
                        </Typography>
                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mt: 0.5 }}>
                          <Button
                            size="small"
                            variant="outlined"
                            onClick={() =>
                              onQuantityChange(item.id, Math.max(1, item.quantity - 1))
                            }
                            sx={{ minWidth: 30, p: 0.5 }}
                          >
                            -
                          </Button>
                          <Typography variant="body2" sx={{ minWidth: 30, textAlign: 'center' }}>
                            {item.quantity}
                          </Typography>
                          <Button
                            size="small"
                            variant="outlined"
                            onClick={() => onQuantityChange(item.id, item.quantity + 1)}
                            sx={{ minWidth: 30, p: 0.5 }}
                          >
                            +
                          </Button>
                        </Box>
                      </Box>
                    }
                  />
                  <Box
                    sx={{
                      display: 'flex',
                      flexDirection: 'column',
                      alignItems: 'flex-end',
                      gap: 1,
                    }}
                  >
                    <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                      {(item.price * item.quantity).toLocaleString()}원
                    </Typography>
                    <IconButton size="small" color="error" onClick={() => onRemoveItem(item.id)}>
                      <DeleteIcon />
                    </IconButton>
                  </Box>
                </ListItem>
                <Divider />
              </React.Fragment>
            ))}
          </List>
        )}
      </Box>

      <Box sx={{ mt: 'auto' }}>
        <Divider sx={{ mb: 2 }} />
        <Stack spacing={2}>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <Typography variant="h6">총 금액</Typography>
            <Typography variant="h4" color="primary" sx={{ fontWeight: 'bold' }}>
              {totalAmount.toLocaleString()}원
            </Typography>
          </Box>
          <Button
            variant="contained"
            color="primary"
            fullWidth
            size="large"
            onClick={onCheckout}
            disabled={items.length === 0}
            sx={{ py: 1.5, fontSize: '1.2rem', fontWeight: 'bold' }}
          >
            결제하기
          </Button>
        </Stack>
      </Box>
    </Paper>
  );
};

export default Cart;

