import { useRoutes } from 'react-router-dom';
import Pos from '@/page/pos';
import Main from '@/page/main';

export default function AppRoutes() {
  const routes = [
    { path: '/', element: <Main /> },
    { path: '/pos', element: <Pos /> },
  ];

  return useRoutes(routes);
}
