import { useState, useEffect, ChangeEvent, FormEvent } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Layout from '../../components/Layout';
import api from '../../config/axios';
import {
  Paper, TextField, Button, Grid, Alert, FormControlLabel, Checkbox, Box, CircularProgress
} from '@mui/material';

function EditClient() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [fetchLoading, setFetchLoading] = useState(true);
  
  const [formData, setFormData] = useState({
    companyName: '',
    industry: '',
    address: '',
    primaryContact: { name: '', email: '', phone: '' },
    annualTurnover: '',
    documentsSubmitted: false
  });

  useEffect(() => {
    fetchClient();
  }, [id]);

  const fetchClient = async () => {
    try {
      const response = await api.get(`/rm/clients/${id}`);
      const client = response.data;
      setFormData({
        companyName: client.companyName,
        industry: client.industry,
        address: client.address,
        primaryContact: client.primaryContact,
        annualTurnover: client.annualTurnover.toString(),
        documentsSubmitted: client.documentsSubmitted
      });
      setFetchLoading(false);
    } catch (err) {
      setError('Failed to fetch client details');
      setFetchLoading(false);
    }
  };

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleContactChange = (e: ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      primaryContact: { ...formData.primaryContact, [e.target.name]: e.target.value }
    });
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await api.put(`/rm/clients/${id}`, {
        ...formData,
        annualTurnover: parseFloat(formData.annualTurnover)
      });
      navigate('/rm/clients');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to update client');
      setLoading(false);
    }
  };

  if (fetchLoading) {
    return (
      <Layout title="Edit Client">
        <Box sx={{ display: 'flex', justifyContent: 'center', padding: 4 }}>
          <CircularProgress />
        </Box>
      </Layout>
    );
  }

  return (
    <Layout title="Edit Client">
      {error && <Alert severity="error" sx={{ marginBottom: 2 }}>{error}</Alert>}

      <Paper sx={{ padding: 3 }}>
        <form onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            <Grid item xs={12}>
              <TextField fullWidth required label="Company Name" name="companyName"
                value={formData.companyName} onChange={handleChange} />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField fullWidth required label="Industry" name="industry"
                value={formData.industry} onChange={handleChange} />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField fullWidth required label="Annual Turnover (in INR)" name="annualTurnover"
                type="number" value={formData.annualTurnover} onChange={handleChange} />
            </Grid>

            <Grid item xs={12}>
              <TextField fullWidth required multiline rows={2} label="Address" name="address"
                value={formData.address} onChange={handleChange} />
            </Grid>

            <Grid item xs={12}>
              <Box sx={{ marginTop: 2, marginBottom: 1 }}>
                <strong>Primary Contact Details</strong>
              </Box>
            </Grid>

            <Grid item xs={12} md={4}>
              <TextField fullWidth required label="Contact Name" name="name"
                value={formData.primaryContact.name} onChange={handleContactChange} />
            </Grid>

            <Grid item xs={12} md={4}>
              <TextField fullWidth required label="Contact Email" name="email" type="email"
                value={formData.primaryContact.email} onChange={handleContactChange} />
            </Grid>

            <Grid item xs={12} md={4}>
              <TextField fullWidth required label="Contact Phone" name="phone"
                value={formData.primaryContact.phone} onChange={handleContactChange} />
            </Grid>

            <Grid item xs={12}>
              <FormControlLabel
                control={
                  <Checkbox
                    checked={formData.documentsSubmitted}
                    onChange={(e) => setFormData({...formData, documentsSubmitted: e.target.checked})}
                  />
                }
                label="Documents Submitted"
              />
            </Grid>

            <Grid item xs={12}>
              <Box sx={{ display: 'flex', gap: 2 }}>
                <Button type="submit" variant="contained" color="primary" disabled={loading}>
                  {loading ? 'Updating...' : 'Update Client'}
                </Button>
                <Button variant="outlined" onClick={() => navigate('/rm/clients')} disabled={loading}>
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

export default EditClient;