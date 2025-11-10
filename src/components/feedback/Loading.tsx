import { styled, keyframes } from '@mui/system';
import { useAppSelector } from '../../store/ReduxHooks';

const blink = keyframes`
  0% { opacity: 0.2; }
  50% { opacity: 1; }
  100% { opacity: 0.2; }
`;

const Overlay = styled('div')({
  position: 'fixed',
  top: 0,
  left: 0,
  width: '100vw',
  height: '100vh',
  backgroundColor: 'rgba(0, 0, 0, 0.5)', // 반투명 배경
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  zIndex: 9999, // 가장 위로
});

const LoaderText = styled('span')({
  color: '#fff',
  fontSize: '48px',
  fontFamily: 'Arial, Helvetica, sans-serif',
  animation: `${blink} 1.5s infinite ease-in-out`,
});

const Loading = () => {
  const isLoading = useAppSelector((state) => state.loading.isLoading);

  if (!isLoading) return null;

  return (
    <Overlay>
      <LoaderText>Loading...</LoaderText>
    </Overlay>
  );
};

export default Loading;
