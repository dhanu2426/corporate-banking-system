import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../../components/Layout';
import api from '../../config/axios';
import { Client } from '../../types';
import {
  Paper, Table, TableBody, TableCell, TableContainer, TableHead,
  TableRow, Button, Box, Alert, Chip
} from '@mui/material';

function ClientList() {
  const [clients, setClients] = useState<Client[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchClients();
  }, []);

  const fetchClients = async () => {
    setLoading(true);
    try {
      const response = await api.get<Client[]>('/rm/clients');
      setClients(response.data);
      setError('');
    } catch (err) {
      setError('Failed to fetch clients');
    }
    setLoading(false);
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR',
      maximumFractionDigits: 0
    }).format(amount);
  };

  return (
    <Layout title="My Clients">
      {error && <Alert severity="error" sx={{ marginBottom: 2 }}>{error}</Alert>}

      <Box sx={{ marginBottom: 2 }}>
        <Button variant="contained" color="primary" onClick={() => navigate('/rm/clients/new')}>
          Add New Client
        </Button>
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell><strong>Company Name</strong></TableCell>
              <TableCell><strong>Industry</strong></TableCell>
              <TableCell><strong>Contact Person</strong></TableCell>
              <TableCell><strong>Contact Email</strong></TableCell>
              <TableCell><strong>Annual Turnover</strong></TableCell>
              <TableCell><strong>Documents</strong></TableCell>
              <TableCell><strong>Actions</strong></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {loading ? (
              <TableRow><TableCell colSpan={7} align="center">Loading...</TableCell></TableRow>
            ) : clients.length === 0 ? (
              <TableRow><TableCell colSpan={7} align="center">No clients found. Add your first client!</TableCell></TableRow>
            ) : (
              clients.map((client) => (
                <TableRow key={client.id}>
                  <TableCell>{client.companyName}</TableCell>
                  <TableCell>{client.industry}</TableCell>
                  <TableCell>{client.primaryContact.name}</TableCell>
                  <TableCell>{client.primaryContact.email}</TableCell>
                  <TableCell>{formatCurrency(client.annualTurnover)}</TableCell>
                  <TableCell>
                    <Chip 
                      label={client.documentsSubmitted ? 'Submitted' : 'Pending'}
                      color={client.documentsSubmitted ? 'success' : 'warning'}
                      size="small"
                    />
                  </TableCell>
                  <TableCell>
                    <Button size="small" variant="outlined" onClick={() => navigate(`/rm/clients/edit/${client.id}`)}>
                      Edit
                    </Button>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Layout>
  );
}

export default ClientList;