import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import '../styles/dashboard.css';

const Profile = () => {
  const { user } = useAuth();
  const navigate = useNavigate();

  return (
    <div className="dashboard-container">
      <div className="container">
        <div className="dashboard-header">
          <h1>My Profile</h1>
          <p>Review your account information and role details.</p>
        </div>

        <div className="profile-card">
          <h2>{user?.fullName}</h2>
          <p className="profile-role">{user?.role}</p>

          <div className="profile-details">
            <div className="profile-field">
              <span>Email</span>
              <strong>{user?.email}</strong>
            </div>
            <div className="profile-field">
              <span>Role</span>
              <strong>{user?.role}</strong>
            </div>
            <div className="profile-field">
              <span>User ID</span>
              <strong>{user?.id}</strong>
            </div>
          </div>

          <button className="btn btn-primary" onClick={() => navigate('/student/dashboard')}>
            Back to Dashboard
          </button>
        </div>
      </div>
    </div>
  );
};

export default Profile;
