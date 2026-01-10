import { useNavigate } from 'react-router-dom';
import Layout from '../../components/Layout';
import { Paper, Grid, Typography, Button, Box } from '@mui/material';
import { People, RequestPage } from '@mui/icons-material';

function RMDashboard() {
  const navigate = useNavigate();

  return (
    <Layout title="Relationship Manager Dashboard">
      <Grid container spacing={3}>
        <Grid item xs={12} md={6}>
          <Paper elevation={3} sx={{ padding: 3, textAlign: 'center' }}>
            <People sx={{ fontSize: 60, color: 'primary.main', marginBottom: 2 }} />
            <Typography variant="h5" gutterBottom>Client Management</Typography>
            <Typography variant="body2" color="textSecondary" paragraph>
              Onboard new clients and manage existing client information
            </Typography>
            <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center' }}>
              <Button variant="contained" color="primary" onClick={() => navigate('/rm/clients')}>
                View Clients
              </Button>
              <Button variant="outlined" color="primary" onClick={() => navigate('/rm/clients/new')}>
                Add New Client
              </Button>
            </Box>
          </Paper>
        </Grid>

        <Grid item xs={12} md={6}>
          <Paper elevation={3} sx={{ padding: 3, textAlign: 'center' }}>
            <RequestPage sx={{ fontSize: 60, color: 'success.main', marginBottom: 2 }} />
            <Typography variant="h5" gutterBottom>Credit Requests</Typography>
            <Typography variant="body2" color="textSecondary" paragraph>
              Submit and track credit requests for your clients
            </Typography>
            <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center' }}>
              <Button variant="contained" color="success" onClick={() => navigate('/rm/credit-requests')}>
                View Requests
              </Button>
              <Button variant="outlined" color="success" onClick={() => navigate('/rm/credit-requests/new')}>
                New Request
              </Button>
            </Box>
          </Paper>
        </Grid>
      </Grid>
    </Layout>
  );
}

export default RMDashboard;