import React, { useState, useRef } from 'react';
import {
  Box,
  Paper,
  TextField,
  Button,
  Typography,
  MenuItem,
  FormControl,
  InputLabel,
  Select,
  Stack,
  Avatar,
  IconButton,
} from '@mui/material';
import { PhotoCamera, Delete as DeleteIcon } from '@mui/icons-material';
import { useFormik } from 'formik';
import * as yup from 'yup';
import type { Product } from '@/features/pos/types';
import { ProductStatus } from '@/features/pos/types';
import { useProductCreateMutation } from '@/features/pos/hook/useProductCreateMutation';
import { useProductUpdateMutation } from '@/features/pos/hook/useProductUpdateMutation';
import { useProductDetailQuery } from '@/features/pos/hook/useProductDetailQuery';
import { toast } from 'react-toastify';

interface ProductFormProps {
  productId?: number;
  onSuccess: () => void;
  onCancel: () => void;
}

const validationSchema = yup.object({
  code: yup.string().required('상품 코드를 입력해주세요'),
  name: yup.string().required('상품명을 입력해주세요'),
  description: yup.string().required('설명을 입력해주세요'),
  price: yup.number().required('가격을 입력해주세요').min(0, '가격은 0 이상이어야 합니다'),
  stock: yup
    .number()
    .required('재고를 입력해주세요')
    .integer('재고는 정수여야 합니다')
    .min(0, '재고는 0 이상이어야 합니다'),
  status: yup.string().required('상태를 선택해주세요'),
  image: yup.mixed().nullable(),
});

const ProductForm: React.FC<ProductFormProps> = ({ productId, onSuccess, onCancel }) => {
  const createMutation = useProductCreateMutation();
  const updateMutation = useProductUpdateMutation();
  const { data: product } = useProductDetailQuery(productId || null);
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [imagePreview, setImagePreview] = useState<string | null>(null);
  const [imageFile, setImageFile] = useState<File | null>(null);

  const isEditMode = !!productId;

  React.useEffect(() => {
    if (product?.imageUrl) {
      setImagePreview(product.imageUrl);
    }
  }, [product]);

  const formik = useFormik<
    Omit<Product, 'id' | 'createdAt' | 'updatedAt'> & { image?: File | null }
  >({
    enableReinitialize: true,
    initialValues: {
      code: product?.code || '',
      name: product?.name || '',
      description: product?.description || '',
      price: product?.price || 0,
      stock: product?.stock || 0,
      status: product?.status || ProductStatus.ACTIVE,
      imageUrl: product?.imageUrl || '',
      image: null,
    },
    validationSchema,
    onSubmit: async (values) => {
      try {
        // FormData로 통일
        const formData = new FormData();
        formData.append('code', values.code);
        formData.append('name', values.name);
        formData.append('description', values.description);
        formData.append('price', values.price.toString());
        formData.append('stock', values.stock.toString());
        formData.append('status', values.status);

        if (imageFile) {
          formData.append('image', imageFile);
        } else if (values.imageUrl && !imageFile) {
          // 수정 모드에서 이미지를 변경하지 않은 경우 기존 이미지 URL 전달
          formData.append('imageUrl', values.imageUrl);
        }

        if (isEditMode && productId) {
          await updateMutation.mutateAsync({
            id: productId,
            formData,
          });
          toast.success('상품이 수정되었습니다.');
        } else {
          await createMutation.mutateAsync(formData);
          toast.success('상품이 등록되었습니다.');
        }
        onSuccess();
      } catch (error) {
        // Error handling is done in mutation hook
      }
    },
  });

  const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
      if (file.size > 5 * 1024 * 1024) {
        toast.error('이미지 크기는 5MB 이하여야 합니다.');
        return;
      }
      if (!file.type.startsWith('image/')) {
        toast.error('이미지 파일만 업로드 가능합니다.');
        return;
      }
      setImageFile(file);
      const reader = new FileReader();
      reader.onloadend = () => {
        setImagePreview(reader.result as string);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleRemoveImage = () => {
    setImageFile(null);
    setImagePreview(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  return (
    <Box>
      <Typography variant="h5" component="h2" sx={{ mb: 3 }}>
        {isEditMode ? '상품 수정' : '상품 등록'}
      </Typography>

      <Paper sx={{ p: 3 }}>
        <form onSubmit={formik.handleSubmit}>
          <Stack spacing={3}>
            <Box sx={{ display: 'flex', gap: 2, flexDirection: { xs: 'column', md: 'row' } }}>
              <TextField
                fullWidth
                id="code"
                name="code"
                label="상품 코드"
                value={formik.values.code}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                error={formik.touched.code && Boolean(formik.errors.code)}
                helperText={formik.touched.code && formik.errors.code}
                disabled={isEditMode}
              />
              <TextField
                fullWidth
                id="name"
                name="name"
                label="상품명"
                value={formik.values.name}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                error={formik.touched.name && Boolean(formik.errors.name)}
                helperText={formik.touched.name && formik.errors.name}
              />
            </Box>

            <TextField
              fullWidth
              id="description"
              name="description"
              label="설명"
              value={formik.values.description}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={formik.touched.description && Boolean(formik.errors.description)}
              helperText={formik.touched.description && formik.errors.description}
              multiline
              rows={4}
            />

            {/* 이미지 업로드 */}
            <Box>
              <Typography variant="body2" sx={{ mb: 1 }}>
                상품 이미지
              </Typography>
              <Box sx={{ display: 'flex', gap: 2, alignItems: 'center', flexWrap: 'wrap' }}>
                {imagePreview && (
                  <Box sx={{ position: 'relative', display: 'inline-block' }}>
                    <Avatar
                      src={imagePreview}
                      alt="상품 이미지 미리보기"
                      sx={{ width: 120, height: 120 }}
                      variant="rounded"
                    />
                    <IconButton
                      onClick={handleRemoveImage}
                      sx={{
                        position: 'absolute',
                        top: -8,
                        right: -8,
                        bgcolor: 'error.main',
                        color: 'white',
                        '&:hover': { bgcolor: 'error.dark' },
                      }}
                      size="small"
                    >
                      <DeleteIcon fontSize="small" />
                    </IconButton>
                  </Box>
                )}
                <input
                  ref={fileInputRef}
                  accept="image/*"
                  style={{ display: 'none' }}
                  id="image-upload"
                  type="file"
                  onChange={handleImageChange}
                />
                <label htmlFor="image-upload">
                  <Button
                    variant="outlined"
                    component="span"
                    startIcon={<PhotoCamera />}
                    sx={{ flexShrink: 0 }}
                  >
                    {imagePreview ? '이미지 변경' : '이미지 선택'}
                  </Button>
                </label>
              </Box>
              <Typography variant="caption" color="text.secondary" sx={{ mt: 1, display: 'block' }}>
                이미지 크기는 5MB 이하여야 합니다. (권장: 500x500px 이상)
              </Typography>
            </Box>

            <Box sx={{ display: 'flex', gap: 2, flexDirection: { xs: 'column', md: 'row' } }}>
              <TextField
                fullWidth
                id="price"
                name="price"
                label="가격"
                type="number"
                value={formik.values.price}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                error={formik.touched.price && Boolean(formik.errors.price)}
                helperText={formik.touched.price && formik.errors.price}
                InputProps={{
                  endAdornment: <Typography variant="body2">원</Typography>,
                }}
              />
              <TextField
                fullWidth
                id="stock"
                name="stock"
                label="재고"
                type="number"
                value={formik.values.stock}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                error={formik.touched.stock && Boolean(formik.errors.stock)}
                helperText={formik.touched.stock && formik.errors.stock}
              />
            </Box>

            <Box sx={{ display: 'flex', gap: 2, flexDirection: { xs: 'column', md: 'row' } }}>
              <FormControl fullWidth>
                <InputLabel id="status-label">상태</InputLabel>
                <Select
                  labelId="status-label"
                  id="status"
                  name="status"
                  value={formik.values.status}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  label="상태"
                  error={formik.touched.status && Boolean(formik.errors.status)}
                >
                  <MenuItem value={ProductStatus.ACTIVE}>활성</MenuItem>
                  <MenuItem value={ProductStatus.INACTIVE}>비활성</MenuItem>
                  <MenuItem value={ProductStatus.OUT_OF_STOCK}>품절</MenuItem>
                </Select>
              </FormControl>
            </Box>

            <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
              <Button variant="outlined" onClick={onCancel}>
                취소
              </Button>
              <Button
                type="submit"
                variant="contained"
                disabled={createMutation.isPending || updateMutation.isPending}
              >
                {createMutation.isPending || updateMutation.isPending
                  ? '처리 중...'
                  : isEditMode
                    ? '수정'
                    : '등록'}
              </Button>
            </Box>
          </Stack>
        </form>
      </Paper>
    </Box>
  );
};

export default ProductForm;
