// @hooks/apiErrorHandler.ts
import { AxiosError } from 'axios';
import { toast } from 'react-toastify';

const apiErrorHandler = (error: unknown) => {
  if (error instanceof AxiosError) {
    const message = error.response?.data?.message || error.message;

    if (error.response?.status === 401) {
      toast.error('로그인이 필요합니다.');
      return;
    }

    toast.error(`에러 발생: ${message}`);
  } else {
    toast.error('예상치 못한 오류가 발생했습니다.');
  }
};

export default apiErrorHandler;
