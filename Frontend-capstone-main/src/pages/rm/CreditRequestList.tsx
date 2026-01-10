import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../../components/Layout';
import api from '../../config/axios';
import { CreditRequest } from '../../types';
import {
  Paper, Table, TableBody, TableCell, TableContainer, TableHead,
  TableRow, Button, Box, Alert, Chip
} from '@mui/material';

function CreditRequestList() {
  const [requests, setRequests] = useState<CreditRequest[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchRequests();
  }, []);

  const fetchRequests = async () => {
    setLoading(true);
    try {
      const response = await api.get<CreditRequest[]>('/rm/credit-requests');
      setRequests(response.data);
      setError('');
    } catch (err) {
      setError('Failed to fetch credit requests');
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

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-IN');
  };

  const getStatusColor = (status: string) => {
    if (status === 'Approved') return 'success';
    if (status === 'Rejected') return 'error';
    return 'warning';
  };

  return (
    <Layout title="My Credit Requests">
      {error && <Alert severity="error" sx={{ marginBottom: 2 }}>{error}</Alert>}

      <Box sx={{ marginBottom: 2 }}>
        <Button variant="contained" color="primary" onClick={() => navigate('/rm/credit-requests/new')}>
          Create New Request
        </Button>
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell><strong>Request Amount</strong></TableCell>
              <TableCell><strong>Tenure (Months)</strong></TableCell>
              <TableCell><strong>Purpose</strong></TableCell>
              <TableCell><strong>Status</strong></TableCell>
              <TableCell><strong>Remarks</strong></TableCell>
              <TableCell><strong>Created Date</strong></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {loading ? (
              <TableRow><TableCell colSpan={6} align="center">Loading...</TableCell></TableRow>
            ) : requests.length === 0 ? (
              <TableRow><TableCell colSpan={6} align="center">No credit requests found. Create your first request!</TableCell></TableRow>
            ) : (
              requests.map((request) => (
                <TableRow key={request.id}>
                  <TableCell>{formatCurrency(request.requestAmount)}</TableCell>
                  <TableCell>{request.tenureMonths}</TableCell>
                  <TableCell>{request.purpose}</TableCell>
                  <TableCell>
                    <Chip 
                      label={request.status}
                      color={getStatusColor(request.status) as any}
                      size="small"
                    />
                  </TableCell>
                  <TableCell>{request.remarks || '-'}</TableCell>
                  <TableCell>{formatDate(request.createdAt)}</TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Layout>
  );
}

export default CreditRequestList;