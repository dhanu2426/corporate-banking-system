import { useState, useEffect, ChangeEvent } from 'react';
import Layout from '../../components/Layout';
import api from '../../config/axios';
import { User } from '../../types';
import {
  Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
  Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField,
  Select, MenuItem, FormControl, InputLabel, Alert, Chip, Box, SelectChangeEvent
} from '@mui/material';

function AdminDashboard() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [openDialog, setOpenDialog] = useState(false);
  const [newUser, setNewUser] = useState({
    username: '',
    email: '',
    password: '',
    role: 'RM'
  });

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    setLoading(true);
    try {
      const response = await api.get<User[]>('/admin/users');
      setUsers(response.data);
      setError('');
    } catch (err) {
      setError('Failed to fetch users');
    }
    setLoading(false);
  };

  const handleCreateUser = async () => {
    try {
      await api.post('/auth/register', newUser);
      setSuccess('User created successfully!');
      setOpenDialog(false);
      setNewUser({ username: '', email: '', password: '', role: 'RM' });
      fetchUsers();
      setTimeout(() => setSuccess(''), 3000);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to create user');
    }
  };

  const handleToggleStatus = async (userId: string, currentStatus: boolean) => {
    try {
      await api.put(`/admin/users/${userId}/status`, { active: !currentStatus });
      setSuccess(`User ${!currentStatus ? 'activated' : 'deactivated'} successfully!`);
      fetchUsers();
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError('Failed to update user status');
    }
  };

  return (
    <Layout title="Admin Dashboard - User Management">
      {error && <Alert severity="error" sx={{ marginBottom: 2 }}>{error}</Alert>}
      {success && <Alert severity="success" sx={{ marginBottom: 2 }}>{success}</Alert>}

      <Box sx={{ marginBottom: 2 }}>
        <Button variant="contained" color="primary" onClick={() => setOpenDialog(true)}>
          Create New User
        </Button>
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell><strong>Username</strong></TableCell>
              <TableCell><strong>Email</strong></TableCell>
              <TableCell><strong>Role</strong></TableCell>
              <TableCell><strong>Status</strong></TableCell>
              <TableCell><strong>Actions</strong></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {loading ? (
              <TableRow><TableCell colSpan={5} align="center">Loading...</TableCell></TableRow>
            ) : users.length === 0 ? (
              <TableRow><TableCell colSpan={5} align="center">No users found</TableCell></TableRow>
            ) : (
              users.map((user) => (
                <TableRow key={user.id}>
                  <TableCell>{user.username}</TableCell>
                  <TableCell>{user.email}</TableCell>
                  <TableCell>
                    <Chip 
                      label={user.role} 
                      color={user.role === 'ADMIN' ? 'error' : user.role === 'RM' ? 'primary' : 'success'}
                      size="small"
                    />
                  </TableCell>
                  <TableCell>
                    <Chip 
                      label={user.active ? 'Active' : 'Inactive'}
                      color={user.active ? 'success' : 'default'}
                      size="small"
                    />
                  </TableCell>
                  <TableCell>
                    <Button
                      size="small"
                      variant="outlined"
                      color={user.active ? 'error' : 'success'}
                      onClick={() => handleToggleStatus(user.id, user.active)}
                    >
                      {user.active ? 'Deactivate' : 'Activate'}
                    </Button>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
        <DialogTitle>Create New User</DialogTitle>
        <DialogContent>
          <TextField
            fullWidth label="Username" name="username" margin="normal"
            value={newUser.username}
            onChange={(e: ChangeEvent<HTMLInputElement>) => setNewUser({...newUser, username: e.target.value})}
          />
          <TextField
            fullWidth label="Email" name="email" type="email" margin="normal"
            value={newUser.email}
            onChange={(e: ChangeEvent<HTMLInputElement>) => setNewUser({...newUser, email: e.target.value})}
          />
          <TextField
            fullWidth label="Password" name="password" type="password" margin="normal"
            value={newUser.password}
            onChange={(e: ChangeEvent<HTMLInputElement>) => setNewUser({...newUser, password: e.target.value})}
          />
          <FormControl fullWidth margin="normal">
            <InputLabel>Role</InputLabel>
            <Select
              value={newUser.role} label="Role"
              onChange={(e: SelectChangeEvent) => setNewUser({...newUser, role: e.target.value})}
            >
              <MenuItem value="ADMIN">Admin</MenuItem>
              <MenuItem value="RM">Relationship Manager</MenuItem>
              <MenuItem value="ANALYST">Analyst</MenuItem>
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
          <Button onClick={handleCreateUser} variant="contained" color="primary">Create</Button>
        </DialogActions>
      </Dialog>
    </Layout>
  );
}

export default AdminDashboard;