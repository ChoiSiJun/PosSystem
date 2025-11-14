import { useRoutes } from 'react-router-dom';
import Pos from '@/page/pos';
export default function AppRoutes() {
  const routes = [{ path: '/', element: <Pos /> }];

  return useRoutes(routes);
}
