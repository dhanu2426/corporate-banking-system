import { useNavigate } from 'react-router-dom';
import Layout from '../../components/Layout';
import { Paper, Grid, Typography, Button, Box } from '@mui/material';
import { Assignment } from '@mui/icons-material';

function AnalystDashboard() {
  const navigate = useNavigate();

  return (
    <Layout title="Analyst Dashboard">
      <Grid container spacing={3} justifyContent="center">
        <Grid item xs={12} md={6}>
          <Paper elevation={3} sx={{ padding: 3, textAlign: 'center' }}>
            <Assignment sx={{ fontSize: 60, color: 'primary.main', marginBottom: 2 }} />
            <Typography variant="h5" gutterBottom>
              Credit Request Review
            </Typography>
            <Typography variant="body2" color="textSecondary" paragraph>
              Review and approve/reject credit requests submitted by Relationship Managers
            </Typography>
            <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center' }}>
              <Button 
                variant="contained" 
                color="primary"
                onClick={() => navigate('/analyst/credit-requests')}
              >
                View All Requests
              </Button>
            </Box>
          </Paper>
        </Grid>
      </Grid>
    </Layout>
  );
}

export default AnalystDashboard;