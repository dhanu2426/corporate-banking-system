import { useState, useEffect, FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Container, Paper, TextField, Button, Typography, Box, Alert } from '@mui/material';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  
  const { login, user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (user) {
      if (user.role === 'ADMIN') navigate('/admin');
      else if (user.role === 'RM') navigate('/rm');
      else if (user.role === 'ANALYST') navigate('/analyst');
    }
  }, [user, navigate]);

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    if (!username || !password) {
      setError('Please enter both username and password');
      setLoading(false);
      return;
    }

    const result = await login(username, password);
    
    if (result.success) {
      const savedUser = JSON.parse(localStorage.getItem('user') || '{}');
      if (savedUser.role === 'ADMIN') navigate('/admin');
      else if (savedUser.role === 'RM') navigate('/rm');
      else if (savedUser.role === 'ANALYST') navigate('/analyst');
    } else {
      setError(result.message || 'Login failed');
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="sm">
      <Box sx={{ marginTop: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Paper elevation={3} sx={{ padding: 4, width: '100%' }}>
          <Typography variant="h4" align="center" gutterBottom>
            Corporate Banking System
          </Typography>
          
          <Typography variant="h6" align="center" color="textSecondary" gutterBottom>
            Login
          </Typography>

          {error && <Alert severity="error" sx={{ marginBottom: 2 }}>{error}</Alert>}

          <form onSubmit={handleSubmit}>
            <TextField
              fullWidth
              label="Username"
              variant="outlined"
              margin="normal"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              disabled={loading}
            />

            <TextField
              fullWidth
              label="Password"
              type="password"
              variant="outlined"
              margin="normal"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              disabled={loading}
            />

            <Button
              fullWidth
              type="submit"
              variant="contained"
              color="primary"
              size="large"
              sx={{ marginTop: 2 }}
              disabled={loading}
            >
              {loading ? 'Logging in...' : 'Login'}
            </Button>
          </form>

          <Box sx={{ marginTop: 3 }}>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
}

export default Login;