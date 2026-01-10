import { ReactNode } from 'react';
import { useNavigate } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button, Container, Box } from '@mui/material';
import { useAuth } from '../context/AuthContext';

interface LayoutProps {
  children: ReactNode;
  title: string;
}

function Layout({ children, title }: LayoutProps) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const handleDashboard = () => {
    if (user?.role === 'ADMIN') {
      navigate('/admin');
    } else if (user?.role === 'RM') {
      navigate('/rm');
    } else if (user?.role === 'ANALYST') {
      navigate('/analyst');
    }
  };

  return (
    <Box>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1, cursor: 'pointer' }} onClick={handleDashboard}>
            Corporate Banking System
          </Typography>
          
          <Typography variant="body1" sx={{ marginRight: 2 }}>
            Welcome, {user?.username} ({user?.role})
          </Typography>
          
          <Button color="inherit" onClick={handleLogout}>
            Logout
          </Button>
        </Toolbar>
      </AppBar>

      <Container maxWidth="lg" sx={{ marginTop: 4, marginBottom: 4 }}>
        <Typography variant="h4" gutterBottom>
          {title}
        </Typography>
        {children}
      </Container>
    </Box>
  );
}

export default Layout;