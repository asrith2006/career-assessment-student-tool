import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import courseService from '../services/courseService';
import '../styles/dashboard.css';

const CourseRegistration = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [courses, setCourses] = useState([]);
  const [registeredCourseIds, setRegisteredCourseIds] = useState([]);
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    const refreshCourses = () => setCourses(courseService.getAllCourses());
    refreshCourses();

    // Load user's registered courses
    loadUserRegistrations();

    const handleStorage = (event) => {
      if (event.key === 'career_assessment_courses') {
        refreshCourses();
      }
    };

    window.addEventListener('storage', handleStorage);
    return () => window.removeEventListener('storage', handleStorage);
  }, [user]);

  const loadUserRegistrations = () => {
    if (!user) return;

    const registrationsKey = `course_registrations_${user.id}`;
    const stored = localStorage.getItem(registrationsKey);
    if (stored) {
      try {
        const registrations = JSON.parse(stored);
        setRegisteredCourseIds(registrations.map(reg => reg.courseId));
      } catch (e) {
        console.error('Error loading course registrations:', e);
      }
    }
  };

  const saveUserRegistration = (courseId) => {
    if (!user) return;

    const registrationsKey = `course_registrations_${user.id}`;
    const stored = localStorage.getItem(registrationsKey);
    let registrations = [];

    if (stored) {
      try {
        registrations = JSON.parse(stored);
      } catch (e) {
        registrations = [];
      }
    }

    // Add new registration
    const newRegistration = {
      courseId: courseId,
      registeredAt: new Date().toISOString(),
      userId: user.id,
      userName: user.fullName,
      userEmail: user.email
    };

    registrations.push(newRegistration);
    localStorage.setItem(registrationsKey, JSON.stringify(registrations));
  };

  const handleRegister = (course) => {
    if (registeredCourseIds.includes(course.id)) {
      return;
    }

    // Save registration
    saveUserRegistration(course.id);

    setRegisteredCourseIds((prev) => [...prev, course.id]);
    setSuccessMessage(`Successfully registered for ${course.title}.`);
    setTimeout(() => setSuccessMessage(''), 3000);
  };

  return (
    <div className="dashboard-container">
      <div className="container">
        <div className="dashboard-header">
          <h1>Course Registration</h1>
          <p>Browse available courses and register for the ones that support your career goals.</p>
        </div>

        {successMessage && <div className="alert alert-success">{successMessage}</div>}

        <div className="course-list">
          {courses.map((course) => (
            <div key={course.id} className="course-card">
              <h3>{course.title}</h3>
              <p>{course.description}</p>
              <div className="course-meta">
                <span>{course.duration}</span>
                <button
                  className="btn btn-primary"
                  onClick={() => handleRegister(course)}
                  disabled={registeredCourseIds.includes(course.id)}
                >
                  {registeredCourseIds.includes(course.id) ? 'Registered' : 'Register'}
                </button>
              </div>
            </div>
          ))}
        </div>

        <button className="btn btn-secondary" onClick={() => navigate('/student/dashboard')}>
          Back to Dashboard
        </button>
      </div>
    </div>
  );
};

export default CourseRegistration;
