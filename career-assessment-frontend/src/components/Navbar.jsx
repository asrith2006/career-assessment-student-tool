import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import '../styles/navbar.css';

const Navbar = () => {
  const { user, logout, isAuthenticated, isAdmin, isStudent } = useAuth();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="container">
        <div className="navbar-content">
          <Link to="/" className="navbar-brand">
            Career Assessment
          </Link>

          <div className={`navbar-menu ${menuOpen ? 'open' : ''}`}>
            {isAuthenticated ? (
              <>
                {isStudent && (
                  <div className="navbar-links">
                    <Link to="/student/dashboard" className="nav-link">Dashboard</Link>
                    <Link to="/student/take-test" className="nav-link">Take Test</Link>
                    <Link to="/student/results" className="nav-link">My Results</Link>
                    <Link to="/careers" className="nav-link">Careers</Link>
                    <Link to="/profile" className="nav-link">Profile</Link>
                  </div>
                )}

                {isAdmin && (
                  <div className="navbar-links">
                    <Link to="/admin/dashboard" className="nav-link">Dashboard</Link>
                    <Link to="/admin/questions" className="nav-link">Questions</Link>
                    <Link to="/admin/users" className="nav-link">Users</Link>
                    <Link to="/admin/careers" className="nav-link">Careers</Link>
                    <Link to="/admin/results" className="nav-link">Results</Link>
                  </div>
                )}

                <div className="navbar-user">
                  <span className="user-name">{user?.fullName}</span>
                  <button onClick={handleLogout} className="btn btn-secondary">
                    Logout
                  </button>
                </div>
              </>
            ) : (
              <div className="navbar-links">
                <Link to="/login" className="nav-link">Login</Link>
                <Link to="/register" className="nav-link">Register</Link>
              </div>
            )}
          </div>

          <button className="hamburger" onClick={() => setMenuOpen(!menuOpen)}>
            <span></span>
            <span></span>
            <span></span>
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
