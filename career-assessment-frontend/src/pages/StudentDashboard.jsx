import React, { useEffect, useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import courseService from '../services/courseService';
import '../styles/dashboard.css';

const StudentDashboard = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [courses, setCourses] = useState([]);

  useEffect(() => {
    const refreshCourses = () => setCourses(courseService.getAllCourses());
    refreshCourses();

    const handleStorage = (event) => {
      if (event.key === 'career_assessment_courses') {
        refreshCourses();
      }
    };

    window.addEventListener('storage', handleStorage);
    return () => window.removeEventListener('storage', handleStorage);
  }, []);

  return (
    <div className="dashboard-container">
      <div className="container">
        <div className="dashboard-header">
          <h1>Welcome, {user?.fullName}!</h1>
          <p>Ready to explore your career path?</p>
        </div>

        <div className="dashboard-grid">
          <div className="dashboard-card">
            <div className="card-icon">📝</div>
            <h3>Take Assessment Test</h3>
            <p>Answer a series of career-focused questions to get personalized recommendations.</p>
            <Link to="/student/take-test" className="btn btn-primary btn-block">
              Start Test
            </Link>
          </div>

          <div className="dashboard-card">
            <div className="card-icon">📊</div>
            <h3>View Results</h3>
            <p>Check your previous test results and career recommendations.</p>
            <Link to="/student/results" className="btn btn-primary btn-block">
              View Results
            </Link>
          </div>

          <div className="dashboard-card">
            <div className="card-icon">🎯</div>
            <h3>Explore Careers</h3>
            <p>Learn about different career paths and what skills they require.</p>
            <Link to="/careers" className="btn btn-primary btn-block">
              Explore
            </Link>
          </div>

          <div className="dashboard-card">
            <div className="card-icon">🎓</div>
            <h3>Course Registration</h3>
            <p>Find and register for courses that match your career goals.</p>
            <Link to="/courses" className="btn btn-primary btn-block">
              Register Now
            </Link>
          </div>

          <div className="dashboard-card">
            <div className="card-icon">👤</div>
            <h3>My Profile</h3>
            <p>View and manage your account information and preferences.</p>
            <button onClick={() => navigate('/profile')} className="btn btn-primary btn-block">
              Profile
            </button>
          </div>
        </div>

        <div className="info-section">
          <h2>Available Courses</h2>
          <div className="course-preview">
            {courses.length ? (
              courses.slice(0, 3).map((course) => (
                <div key={course.id} className="course-item">
                  <h4>{course.title}</h4>
                  <p>{course.description}</p>
                  <span>{course.duration}</span>
                </div>
              ))
            ) : (
              <div className="course-item">
                <p>No courses available at the moment.</p>
              </div>
            )}
          </div>
          <Link to="/courses" className="btn btn-primary">View All Courses</Link>
        </div>

        <div className="info-section">
          <h2>Getting Started</h2>
          <div className="info-content">
            <div className="info-item">
              <h4>1. Take the Assessment</h4>
              <p>Complete our career assessment test which includes questions about your interests, strengths, and work preferences.</p>
            </div>
            <div className="info-item">
              <h4>2. Get Recommendations</h4>
              <p>Receive personalized career recommendations based on your answers and overall assessment score.</p>
            </div>
            <div className="info-item">
              <h4>3. Explore Careers</h4>
              <p>Learn more about the recommended careers, including skills needed, salary ranges, and job outlook.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default StudentDashboard;

