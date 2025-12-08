import React, { useState } from 'react';
import {
  Box,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination,
  TextField,
  Button,
  Chip,
  IconButton,
  Typography,
  InputAdornment,
  MenuItem,
  Select,
  FormControl,
  InputLabel,
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Visibility as VisibilityIcon,
  Search as SearchIcon,
} from '@mui/icons-material';
import { useProductListQuery } from '@/features/pos/hook/useProductListQuery';
import { useProductDeleteMutation } from '@/features/pos/hook/useProductDeleteMutation';
import { type Product, ProductStatus } from '@/features/pos/types';
import { toast } from 'react-toastify';

interface ProductListProps {
  onAdd: () => void;
  onEdit: (id: number) => void;
  onView: (id: number) => void;
}

const ProductList: React.FC<ProductListProps> = ({ onAdd, onEdit, onView }) => {
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('');

  const { data, isLoading, refetch } = useProductListQuery({
    page: page + 1,
    pageSize,
    search,
    status: statusFilter || undefined,
  });

  const deleteMutation = useProductDeleteMutation();

  const handleDelete = async (id: number) => {
    if (window.confirm('정말 삭제하시겠습니까?')) {
      try {
        await deleteMutation.mutateAsync({ id });
        toast.success('상품이 삭제되었습니다.');
        refetch();
      } catch (error) {
        // Error handling is done in mutation hook
      }
    }
  };

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearch(e.target.value);
    setPage(0);
  };

  const handleStatusFilterChange = (e: any) => {
    setStatusFilter(e.target.value);
    setPage(0);
  };

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

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4" component="h1">
          상품 관리
        </Typography>
        <Button variant="contained" startIcon={<AddIcon />} onClick={onAdd} sx={{ minWidth: 120 }}>
          상품 등록
        </Button>
      </Box>

      <Paper sx={{ p: 2, mb: 2 }}>
        <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap' }}>
          <TextField
            placeholder="상품명 또는 코드로 검색"
            value={search}
            onChange={handleSearch}
            size="small"
            sx={{ flexGrow: 1, minWidth: 300 }}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon />
                </InputAdornment>
              ),
            }}
          />
          <FormControl size="small" sx={{ minWidth: 150 }}>
            <InputLabel>상태</InputLabel>
            <Select value={statusFilter} label="상태" onChange={handleStatusFilterChange}>
              <MenuItem value="">전체</MenuItem>
              <MenuItem value={ProductStatus.ACTIVE}>활성</MenuItem>
              <MenuItem value={ProductStatus.INACTIVE}>비활성</MenuItem>
              <MenuItem value={ProductStatus.OUT_OF_STOCK}>품절</MenuItem>
            </Select>
          </FormControl>
        </Box>
      </Paper>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>코드</TableCell>
              <TableCell>상품명</TableCell>
              <TableCell>설명</TableCell>
              <TableCell align="right">가격</TableCell>
              <TableCell align="right">재고</TableCell>
              <TableCell align="center">상태</TableCell>
              <TableCell align="center">작업</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {isLoading ? (
              <TableRow>
                <TableCell colSpan={7} align="center">
                  로딩 중...
                </TableCell>
              </TableRow>
            ) : data?.products && data.products.length > 0 ? (
              data.products.map((product: Product) => (
                <TableRow key={product.id} hover>
                  <TableCell>{product.code}</TableCell>
                  <TableCell>{product.name}</TableCell>
                  <TableCell
                    sx={{
                      maxWidth: 300,
                      overflow: 'hidden',
                      textOverflow: 'ellipsis',
                      whiteSpace: 'nowrap',
                    }}
                  >
                    {product.description}
                  </TableCell>
                  <TableCell align="right">{product.price.toLocaleString()}원</TableCell>
                  <TableCell align="right">{product.stock}</TableCell>
                  <TableCell align="center">
                    <Chip
                      label={getStatusLabel(product.status)}
                      color={getStatusColor(product.status) as any}
                      size="small"
                    />
                  </TableCell>
                  <TableCell align="center">
                    <IconButton size="small" onClick={() => onView(product.id!)} color="primary">
                      <VisibilityIcon />
                    </IconButton>
                    <IconButton size="small" onClick={() => onEdit(product.id!)} color="primary">
                      <EditIcon />
                    </IconButton>
                    <IconButton
                      size="small"
                      onClick={() => handleDelete(product.id!)}
                      color="error"
                    >
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={7} align="center">
                  등록된 상품이 없습니다.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
        {data && (
          <TablePagination
            component="div"
            count={data.total}
            page={page}
            onPageChange={(_, newPage) => setPage(newPage)}
            rowsPerPage={pageSize}
            onRowsPerPageChange={(e) => {
              setPageSize(parseInt(e.target.value, 10));
              setPage(0);
            }}
            rowsPerPageOptions={[10, 25, 50]}
            labelRowsPerPage="페이지당 행 수:"
            labelDisplayedRows={({ from, to, count }) => `${from}-${to} / 총 ${count}개`}
          />
        )}
      </TableContainer>
    </Box>
  );
};

export default ProductList;
