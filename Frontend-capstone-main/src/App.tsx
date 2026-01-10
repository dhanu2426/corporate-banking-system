import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import PrivateRoute from './components/PrivateRoute';
import RoleRoute from './components/RoleRoute';

import Login from './pages/Login';
import AdminDashboard from './pages/admin/AdminDashBorad';
import RMDashboard from './pages/rm/RMDashboard';
import ClientList from './pages/rm/ClientList';
import CreateClient from './pages/rm/CreateClient';
import EditClient from './pages/rm/EditClient';
import CreditRequestList from './pages/rm/CreditRequestList';
import CreateCreditRequest from './pages/rm/CreateCreditRequest';
import AnalystDashboard from './pages/analyst/AnalystDashboard';
import ReviewCreditRequests from './pages/analyst/ReviewCreditRequests';

function App() {
  return (
    <Router>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<Login />} />
          
          <Route path="/admin" element={
            <PrivateRoute>
              <RoleRoute allowedRole="ADMIN">
                <AdminDashboard />
              </RoleRoute>
            </PrivateRoute>
          } />
          
          <Route path="/rm" element={
            <PrivateRoute>
              <RoleRoute allowedRole="RM">
                <RMDashboard />
              </RoleRoute>
            </PrivateRoute>
          } />
          
          <Route path="/rm/clients" element={
            <PrivateRoute>
              <RoleRoute allowedRole="RM">
                <ClientList />
              </RoleRoute>
            </PrivateRoute>
          } />
          
          <Route path="/rm/clients/new" element={
            <PrivateRoute>
              <RoleRoute allowedRole="RM">
                <CreateClient />
              </RoleRoute>
            </PrivateRoute>
          } />
          
          <Route path="/rm/clients/edit/:id" element={
            <PrivateRoute>
              <RoleRoute allowedRole="RM">
                <EditClient />
              </RoleRoute>
            </PrivateRoute>
          } />
          
          <Route path="/rm/credit-requests" element={
            <PrivateRoute>
              <RoleRoute allowedRole="RM">
                <CreditRequestList />
              </RoleRoute>
            </PrivateRoute>
          } />
          
          <Route path="/rm/credit-requests/new" element={
            <PrivateRoute>
              <RoleRoute allowedRole="RM">
                <CreateCreditRequest />
              </RoleRoute>
            </PrivateRoute>
          } />
          
          <Route path="/analyst" element={
            <PrivateRoute>
              <RoleRoute allowedRole="ANALYST">
                <AnalystDashboard />
              </RoleRoute>
            </PrivateRoute>
          } />
          
          <Route path="/analyst/credit-requests" element={
            <PrivateRoute>
              <RoleRoute allowedRole="ANALYST">
                <ReviewCreditRequests />
              </RoleRoute>
            </PrivateRoute>
          } />
          
          <Route path="/" element={<Navigate to="/login" />} />
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;