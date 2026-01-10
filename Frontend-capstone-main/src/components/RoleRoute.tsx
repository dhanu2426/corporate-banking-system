import { ReactNode } from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

interface RoleRouteProps {
  children: ReactNode;
  allowedRole: 'ADMIN' | 'RM' | 'ANALYST';
}

function RoleRoute({ children, allowedRole }: RoleRouteProps) {
  const { user } = useAuth();

  if (user && user.role !== allowedRole) {
    if (user.role === 'ADMIN') {
      return <Navigate to="/admin" />;
    } else if (user.role === 'RM') {
      return <Navigate to="/rm" />;
    } else if (user.role === 'ANALYST') {
      return <Navigate to="/analyst" />;
    }
  }

  return <>{children}</>;
}

export default RoleRoute;