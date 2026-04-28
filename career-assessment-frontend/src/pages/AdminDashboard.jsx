import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/dashboard.css';

const AdminDashboard = () => {
  const navigate = useNavigate();

  return (
    <div className="dashboard-container">
      <div className="container">
        <h1>Admin Dashboard</h1>

        <div className="dashboard-grid">
          <div className="dashboard-card" onClick={() => navigate('/admin/questions')}>
            <div className="card-icon">❓</div>
            <h3>Manage Questions</h3>
            <p>Create, edit, and delete assessment questions.</p>
          </div>

          <div className="dashboard-card" onClick={() => navigate('/admin/users')}>
            <div className="card-icon">👥</div>
            <h3>Manage Users</h3>
            <p>View and manage student and admin accounts.</p>
          </div>

          <div className="dashboard-card" onClick={() => navigate('/admin/careers')}>
            <div className="card-icon">🎯</div>
            <h3>Manage Careers</h3>
            <p>Update career information and recommendations.</p>
          </div>

          <div className="dashboard-card" onClick={() => navigate('/admin/courses')}>
            <div className="card-icon">📚</div>
            <h3>Manage Courses</h3>
            <p>Add, edit, and manage course offerings.</p>
          </div>

          <div className="dashboard-card" onClick={() => navigate('/admin/programs')}>
            <div className="card-icon">🎓</div>
            <h3>Student Programs</h3>
            <p>Create and manage career development programs for students.</p>
          </div>

          <div className="dashboard-card" onClick={() => navigate('/admin/results')}>
            <div className="card-icon">📈</div>
            <h3>View Results</h3>
            <p>Review student assessment results and scores.</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
