import { useState, useEffect, ChangeEvent } from 'react';
import Layout from '../../components/Layout';
import api from '../../config/axios';
import { CreditRequest } from '../../types';
import {
  Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
  Button, Alert, Chip, Dialog, DialogTitle, DialogContent, DialogActions,
  TextField, FormControl, InputLabel, Select, MenuItem, SelectChangeEvent
} from '@mui/material';

function ReviewCreditRequests() {
  const [requests, setRequests] = useState<CreditRequest[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState<CreditRequest | null>(null);
  const [reviewData, setReviewData] = useState({
    status: 'Approved',
    remarks: ''
  });

  useEffect(() => {
    fetchRequests();
  }, []);

  const fetchRequests = async () => {
    setLoading(true);
    try {
      const response = await api.get<CreditRequest[]>('/analyst/credit-requests');
      setRequests(response.data);
      setError('');
    } catch (err) {
      setError('Failed to fetch credit requests');
    }
    setLoading(false);
  };

  const handleReviewClick = (request: CreditRequest) => {
    setSelectedRequest(request);
    setReviewData({
      status: request.status === 'Pending' ? 'Approved' : request.status,
      remarks: request.remarks || ''
    });
    setOpenDialog(true);
  };

  const handleReviewSubmit = async () => {
    if (!selectedRequest) return;
    
    try {
      await api.put(`/analyst/credit-requests/${selectedRequest.id}`, reviewData);
      setSuccess(`Request ${reviewData.status.toLowerCase()} successfully!`);
      setOpenDialog(false);
      fetchRequests();
      setTimeout(() => setSuccess(''), 3000);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to update request');
    }
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
    <Layout title="Review Credit Requests">
      {error && <Alert severity="error" sx={{ marginBottom: 2 }}>{error}</Alert>}
      {success && <Alert severity="success" sx={{ marginBottom: 2 }}>{success}</Alert>}

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell><strong>Request Amount</strong></TableCell>
              <TableCell><strong>Tenure</strong></TableCell>
              <TableCell><strong>Purpose</strong></TableCell>
              <TableCell><strong>Status</strong></TableCell>
              <TableCell><strong>Remarks</strong></TableCell>
              <TableCell><strong>Date</strong></TableCell>
              <TableCell><strong>Actions</strong></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {loading ? (
              <TableRow><TableCell colSpan={7} align="center">Loading...</TableCell></TableRow>
            ) : requests.length === 0 ? (
              <TableRow><TableCell colSpan={7} align="center">No credit requests found</TableCell></TableRow>
            ) : (
              requests.map((request) => (
                <TableRow key={request.id}>
                  <TableCell>{formatCurrency(request.requestAmount)}</TableCell>
                  <TableCell>{request.tenureMonths} months</TableCell>
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
                  <TableCell>
                    <Button size="small" variant="outlined" onClick={() => handleReviewClick(request)}>
                      Review
                    </Button>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Review Credit Request</DialogTitle>
        <DialogContent>
          {selectedRequest && (
            <>
              <TextField
                fullWidth disabled label="Request Amount" margin="normal"
                value={formatCurrency(selectedRequest.requestAmount)}
              />
              
              <TextField
                fullWidth disabled label="Tenure" margin="normal"
                value={`${selectedRequest.tenureMonths} months`}
              />
              
              <TextField
                fullWidth disabled multiline rows={2} label="Purpose" margin="normal"
                value={selectedRequest.purpose}
              />

              <FormControl fullWidth margin="normal">
                <InputLabel>Decision</InputLabel>
                <Select
                  value={reviewData.status}
                  label="Decision"
                  onChange={(e: SelectChangeEvent) => setReviewData({...reviewData, status: e.target.value})}
                >
                  <MenuItem value="Pending">Pending</MenuItem>
                  <MenuItem value="Approved">Approve</MenuItem>
                  <MenuItem value="Rejected">Reject</MenuItem>
                </Select>
              </FormControl>

              <TextField
                fullWidth multiline rows={3} label="Remarks" margin="normal"
                value={reviewData.remarks}
                onChange={(e: ChangeEvent<HTMLInputElement>) => setReviewData({...reviewData, remarks: e.target.value})}
                placeholder="Add your comments here..."
              />
            </>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
          <Button onClick={handleReviewSubmit} variant="contained" color="primary">
            Submit Review
          </Button>
        </DialogActions>
      </Dialog>
    </Layout>
  );
}

export default ReviewCreditRequests;