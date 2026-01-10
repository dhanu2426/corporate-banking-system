import { useState, useEffect, ChangeEvent, FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../../components/Layout';
import api from '../../config/axios';
import { Client } from '../../types';
import {
  Paper, TextField, Button, Grid, Alert, Box, FormControl, InputLabel, Select, MenuItem, SelectChangeEvent
} from '@mui/material';

function CreateCreditRequest() {
  const navigate = useNavigate();
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [clients, setClients] = useState<Client[]>([]);
  
  const [formData, setFormData] = useState({
    clientId: '',
    requestAmount: '',
    tenureMonths: '',
    purpose: ''
  });

  useEffect(() => {
    fetchClients();
  }, []);

  const fetchClients = async () => {
    try {
      const response = await api.get<Client[]>('/rm/clients');
      setClients(response.data);
    } catch (err) {
      setError('Failed to fetch clients');
    }
  };

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSelectChange = (e: SelectChangeEvent) => {
    setFormData({ ...formData, clientId: e.target.value });
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    if (!formData.clientId) {
      setError('Please select a client');
      setLoading(false);
      return;
    }

    if (!formData.requestAmount || parseFloat(formData.requestAmount) <= 0) {
      setError('Please enter valid request amount');
      setLoading(false);
      return;
    }

    if (!formData.tenureMonths || parseInt(formData.tenureMonths) <= 0) {
      setError('Please enter valid tenure');
      setLoading(false);
      return;
    }

    if (!formData.purpose) {
      setError('Please enter purpose');
      setLoading(false);
      return;
    }

    try {
      await api.post('/rm/credit-requests', {
        ...formData,
        requestAmount: parseFloat(formData.requestAmount),
        tenureMonths: parseInt(formData.tenureMonths)
      });
      navigate('/rm/credit-requests');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to create credit request');
      setLoading(false);
    }
  };

  return (
    <Layout title="Create Credit Request">
      {error && <Alert severity="error" sx={{ marginBottom: 2 }}>{error}</Alert>}

      {clients.length === 0 && (
        <Alert severity="warning" sx={{ marginBottom: 2 }}>
          You need to add clients first before creating credit requests.
          <Button onClick={() => navigate('/rm/clients/new')} sx={{ marginLeft: 2 }}>
            Add Client
          </Button>
        </Alert>
      )}

      <Paper sx={{ padding: 3 }}>
        <form onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            <Grid item xs={12}>
              <FormControl fullWidth required>
                <InputLabel>Select Client</InputLabel>
                <Select value={formData.clientId} label="Select Client" onChange={handleSelectChange}>
                  <MenuItem value=""><em>-- Select a Client --</em></MenuItem>
                  {clients.map((client) => (
                    <MenuItem key={client.id} value={client.id}>
                      {client.companyName} - {client.industry}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField fullWidth required label="Request Amount (in INR)" name="requestAmount"
                type="number" value={formData.requestAmount} onChange={handleChange}
                placeholder="e.g., 5000000" />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField fullWidth required label="Tenure (in Months)" name="tenureMonths"
                type="number" value={formData.tenureMonths} onChange={handleChange}
                placeholder="e.g., 24" />
            </Grid>

            <Grid item xs={12}>
              <TextField fullWidth required multiline rows={4} label="Purpose" name="purpose"
                value={formData.purpose} onChange={handleChange}
                placeholder="Describe the purpose of the credit request..." />
            </Grid>

            <Grid item xs={12}>
              <Box sx={{ display: 'flex', gap: 2 }}>
                <Button type="submit" variant="contained" color="primary" disabled={loading || clients.length === 0}>
                  {loading ? 'Submitting...' : 'Submit Request'}
                </Button>
                <Button variant="outlined" onClick={() => navigate('/rm/credit-requests')} disabled={loading}>
                  Cancel
                </Button>
              </Box>
            </Grid>
          </Grid>
        </form>
      </Paper>
    </Layout>
  );
}

export default CreateCreditRequest;