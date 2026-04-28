import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import courseService from '../services/courseService';
import '../styles/dashboard.css';

const ManageCourses = () => {
  const navigate = useNavigate();
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editCourseId, setEditCourseId] = useState(null);
  const [courseRegistrations, setCourseRegistrations] = useState({});
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    duration: '',
  });

  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchCourses = () => {
    setCourses(courseService.getAllCourses());
    loadAllCourseRegistrations();
    setLoading(false);
  };

  const loadAllCourseRegistrations = () => {
    const registrations = {};

    // Get all user IDs from localStorage (this is a simplified approach)
    // In a real app, you'd have a user service to get all users
    const userKeys = Object.keys(localStorage).filter(key => key.startsWith('course_registrations_'));

    userKeys.forEach(key => {
      const userId = key.replace('course_registrations_', '');
      const stored = localStorage.getItem(key);

      if (stored) {
        try {
          const userRegistrations = JSON.parse(stored);
          userRegistrations.forEach(reg => {
            if (!registrations[reg.courseId]) {
              registrations[reg.courseId] = [];
            }
            registrations[reg.courseId].push({
              userId: reg.userId,
              userName: reg.userName,
              userEmail: reg.userEmail,
              registeredAt: reg.registeredAt
            });
          });
        } catch (e) {
          console.error('Error loading registrations for user:', userId, e);
        }
      }
    });

    setCourseRegistrations(registrations);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (editCourseId) {
      courseService.updateCourse({ id: editCourseId, ...formData });
    } else {
      courseService.addCourse(formData);
    }

    fetchCourses();
    setFormData({ title: '', description: '', duration: '' });
    setEditCourseId(null);
    setShowForm(false);
  };

  const handleDelete = (id) => {
    if (window.confirm('Delete this course for all users?')) {
      courseService.deleteCourse(id);
      fetchCourses();
      if (editCourseId === id) {
        setEditCourseId(null);
        setFormData({ title: '', description: '', duration: '' });
        setShowForm(false);
      }
    }
  };

  const handleEdit = (course) => {
    setFormData({
      title: course.title,
      description: course.description,
      duration: course.duration,
    });
    setEditCourseId(course.id);
    setShowForm(true);
  };

  const handleCancel = () => {
    setEditCourseId(null);
    setFormData({ title: '', description: '', duration: '' });
    setShowForm(false);
  };

  return (
    <div className="dashboard-container">
      <div className="container">
        <div className="dashboard-header">
          <h1>Manage Courses</h1>
          <p>Add, edit, and manage course offerings for students.</p>
        </div>

        <button
          onClick={showForm ? handleCancel : () => setShowForm(true)}
          className="btn btn-primary mb-3"
        >
          {showForm ? 'Cancel' : editCourseId ? 'Edit Course' : 'Add Course'}
        </button>

        {showForm && (
          <form onSubmit={handleSubmit} className="card mb-3">
            <div className="form-group">
              <label>Course Title</label>
              <input
                type="text"
                value={formData.title}
                onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                required
              />
            </div>
            <div className="form-group">
              <label>Description</label>
              <textarea
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                required
              ></textarea>
            </div>
            <div className="form-group">
              <label>Duration</label>
              <input
                type="text"
                value={formData.duration}
                onChange={(e) => setFormData({ ...formData, duration: e.target.value })}
                required
              />
            </div>
            <button type="submit" className="btn btn-success">
              {editCourseId ? 'Update Course' : 'Save Course'}
            </button>
          </form>
        )}

        <div className="course-list">
          {loading ? (
            <div>Loading courses...</div>
          ) : courses.length ? (
            courses.map((course) => {
              const registeredStudents = courseRegistrations[course.id] || [];
              return (
                <div key={course.id} className="course-card">
                  <h3>{course.title}</h3>
                  <p>{course.description}</p>

                  <div className="registered-students-section">
                    <h4>Registered Students ({registeredStudents.length})</h4>
                    {registeredStudents.length > 0 ? (
                      <div className="registered-students-list">
                        {registeredStudents.map((student, index) => (
                          <div key={`${student.userId}-${index}`} className="registered-student-item">
                            <span>{student.userName} ({student.userEmail})</span>
                            <small>Registered: {new Date(student.registeredAt).toLocaleDateString()}</small>
                          </div>
                        ))}
                      </div>
                    ) : (
                      <p className="no-students">No students registered yet.</p>
                    )}
                  </div>

                  <div className="course-meta">
                    <span>{course.duration}</span>
                    <div className="course-actions">
                      <button onClick={() => handleEdit(course)} className="btn btn-secondary">
                        Edit
                      </button>
                      <button onClick={() => handleDelete(course.id)} className="btn btn-danger">
                        Delete
                      </button>
                    </div>
                  </div>
                </div>
              );
            })
          ) : (
            <div>No courses available.</div>
          )}
        </div>

        <button className="btn btn-secondary" onClick={() => navigate('/admin/dashboard')}>
          Back to Dashboard
        </button>
      </div>
    </div>
  );
};

export default ManageCourses;
