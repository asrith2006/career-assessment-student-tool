import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/dashboard.css';

const ManageStudentPrograms = () => {
  const navigate = useNavigate();
  const [programs, setPrograms] = useState([]);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [showAssignModal, setShowAssignModal] = useState(false);
  const [selectedProgram, setSelectedProgram] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    duration: '',
    requirements: '',
    benefits: '',
  });

  useEffect(() => {
    fetchPrograms();
    fetchUsers();
  }, []);

  const fetchPrograms = () => {
    // For now, using localStorage to store programs
    // In a real app, this would be an API call
    const storedPrograms = localStorage.getItem('student_programs');
    if (storedPrograms) {
      const parsedPrograms = JSON.parse(storedPrograms);
      // Ensure each program has an enrolledStudents array
      const programsWithStudents = parsedPrograms.map(program => ({
        ...program,
        enrolledStudents: program.enrolledStudents || []
      }));
      setPrograms(programsWithStudents);
    } else {
      // Default programs
      const defaultPrograms = [
        {
          id: 1,
          name: 'Career Development Program',
          description: 'Comprehensive program to help students develop their career skills and find suitable job opportunities.',
          duration: '6 months',
          requirements: 'Completed assessment test with minimum 60% score',
          benefits: 'Personalized career counseling, resume building, interview preparation',
          enrolledStudents: []
        },
        {
          id: 2,
          name: 'Skill Enhancement Program',
          description: 'Focused program to improve specific technical and soft skills required for various careers.',
          duration: '3 months',
          requirements: 'Basic assessment completed',
          benefits: 'Skill workshops, certification preparation, mentorship',
          enrolledStudents: []
        },
      ];
      setPrograms(defaultPrograms);
      localStorage.setItem('student_programs', JSON.stringify(defaultPrograms));
    }
  };

  const fetchUsers = async () => {
    try {
      // For demo purposes, using mock data since we might not have backend running
      // In production, uncomment the line below:
      // const response = await userService.getAllUsers();
      // setUsers(response.data.data);

      // Mock users data
      const mockUsers = [
        { id: 1, fullName: 'John Doe', email: 'john@example.com', role: 'STUDENT' },
        { id: 2, fullName: 'Jane Smith', email: 'jane@example.com', role: 'STUDENT' },
        { id: 3, fullName: 'Bob Johnson', email: 'bob@example.com', role: 'STUDENT' },
        { id: 4, fullName: 'Alice Brown', email: 'alice@example.com', role: 'STUDENT' },
        { id: 5, fullName: 'Charlie Wilson', email: 'charlie@example.com', role: 'STUDENT' },
      ];
      setUsers(mockUsers.filter(user => user.role === 'STUDENT'));
    } catch (err) {
      console.error('Error fetching users:', err);
      // Fallback to empty array
      setUsers([]);
    } finally {
      setLoading(false);
    }
  };

  const handleAssignStudent = (programId, studentId) => {
    const updatedPrograms = programs.map(program => {
      if (program.id === programId) {
        const isAlreadyEnrolled = program.enrolledStudents.some(student => student.id === studentId);
        if (!isAlreadyEnrolled) {
          const student = users.find(user => user.id === studentId);
          return {
            ...program,
            enrolledStudents: [...program.enrolledStudents, student]
          };
        }
      }
      return program;
    });
    setPrograms(updatedPrograms);
    localStorage.setItem('student_programs', JSON.stringify(updatedPrograms));
  };

  const handleRemoveStudent = (programId, studentId) => {
    const updatedPrograms = programs.map(program => {
      if (program.id === programId) {
        return {
          ...program,
          enrolledStudents: program.enrolledStudents.filter(student => student.id !== studentId)
        };
      }
      return program;
    });
    setPrograms(updatedPrograms);
    localStorage.setItem('student_programs', JSON.stringify(updatedPrograms));
  };

  const openAssignModal = (program) => {
    setSelectedProgram(program);
    setShowAssignModal(true);
  };

  const closeAssignModal = () => {
    setSelectedProgram(null);
    setShowAssignModal(false);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (editingId) {
      // Update existing program
      const updatedPrograms = programs.map(program =>
        program.id === editingId ? { ...program, ...formData } : program
      );
      setPrograms(updatedPrograms);
      localStorage.setItem('student_programs', JSON.stringify(updatedPrograms));
    } else {
      // Add new program
      const newProgram = {
        id: Date.now(),
        ...formData,
      };
      const updatedPrograms = [...programs, newProgram];
      setPrograms(updatedPrograms);
      localStorage.setItem('student_programs', JSON.stringify(updatedPrograms));
    }

    setFormData({
      name: '',
      description: '',
      duration: '',
      requirements: '',
      benefits: '',
    });
    setEditingId(null);
    setShowForm(false);
  };

  const handleEdit = (program) => {
    setFormData({
      name: program.name,
      description: program.description,
      duration: program.duration,
      requirements: program.requirements,
      benefits: program.benefits,
    });
    setEditingId(program.id);
    setShowForm(true);
  };

  const handleDelete = (id) => {
    if (window.confirm('Are you sure you want to delete this program?')) {
      const updatedPrograms = programs.filter(program => program.id !== id);
      setPrograms(updatedPrograms);
      localStorage.setItem('student_programs', JSON.stringify(updatedPrograms));
    }
  };

  const handleCancel = () => {
    setEditingId(null);
    setFormData({
      name: '',
      description: '',
      duration: '',
      requirements: '',
      benefits: '',
    });
    setShowForm(false);
  };

  if (loading) return <div className="spinner"></div>;

  return (
    <div className="dashboard-container">
      <div className="container">
        <div className="dashboard-header">
          <h1>Manage Student Programs</h1>
          <p>Create and manage programs that students can participate in for career development.</p>
        </div>

        <button
          onClick={showForm ? handleCancel : () => setShowForm(true)}
          className="btn btn-primary mb-3"
        >
          {showForm ? (editingId ? 'Cancel Editing' : 'Cancel') : 'Add Program'}
        </button>

        {showForm && (
          <form onSubmit={handleSubmit} className="card mb-3">
            <div className="form-group">
              <label>Program Name</label>
              <input
                type="text"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
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
            <div className="form-group">
              <label>Requirements</label>
              <textarea
                value={formData.requirements}
                onChange={(e) => setFormData({ ...formData, requirements: e.target.value })}
                required
              ></textarea>
            </div>
            <div className="form-group">
              <label>Benefits</label>
              <textarea
                value={formData.benefits}
                onChange={(e) => setFormData({ ...formData, benefits: e.target.value })}
                required
              ></textarea>
            </div>
            <button type="submit" className="btn btn-success">
              {editingId ? 'Update Program' : 'Save Program'}
            </button>
          </form>
        )}

        <div className="program-list">
          {programs.length ? (
            programs.map((program) => (
              <div key={program.id} className="program-card">
                <h3>{program.name}</h3>
                <p>{program.description}</p>
                <div className="program-details">
                  <div className="detail-item">
                    <strong>Duration:</strong> {program.duration}
                  </div>
                  <div className="detail-item">
                    <strong>Requirements:</strong> {program.requirements}
                  </div>
                  <div className="detail-item">
                    <strong>Benefits:</strong> {program.benefits}
                  </div>
                </div>

                <div className="enrolled-students-section">
                  <h4>Enrolled Students ({program.enrolledStudents.length})</h4>
                  {program.enrolledStudents.length > 0 ? (
                    <div className="enrolled-students-list">
                      {program.enrolledStudents.map((student) => (
                        <div key={student.id} className="enrolled-student-item">
                          <span>{student.fullName} ({student.email})</span>
                          <button
                            onClick={() => handleRemoveStudent(program.id, student.id)}
                            className="btn btn-sm btn-danger"
                          >
                            Remove
                          </button>
                        </div>
                      ))}
                    </div>
                  ) : (
                    <p className="no-students">No students enrolled yet.</p>
                  )}
                </div>

                <div className="program-actions">
                  <button onClick={() => openAssignModal(program)} className="btn btn-primary">
                    Assign Students
                  </button>
                  <button onClick={() => handleEdit(program)} className="btn btn-secondary">
                    Edit
                  </button>
                  <button onClick={() => handleDelete(program.id)} className="btn btn-danger">
                    Delete
                  </button>
                </div>
              </div>
            ))
          ) : (
            <div>No programs available.</div>
          )}
        </div>

        <button className="btn btn-secondary" onClick={() => navigate('/admin/dashboard')}>
          Back to Dashboard
        </button>
      </div>

      {/* Assign Students Modal */}
      {showAssignModal && selectedProgram && (
        <div className="modal-overlay" onClick={closeAssignModal}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>Assign Students to {selectedProgram.name}</h3>
              <button onClick={closeAssignModal} className="close-btn">&times;</button>
            </div>
            <div className="modal-body">
              <div className="available-students">
                <h4>Available Students</h4>
                {users.length > 0 ? (
                  <div className="students-list">
                    {users
                      .filter(student => !selectedProgram.enrolledStudents.some(enrolled => enrolled.id === student.id))
                      .map((student) => (
                        <div key={student.id} className="student-item">
                          <div className="student-info">
                            <span className="student-name">{student.fullName}</span>
                            <span className="student-email">{student.email}</span>
                          </div>
                          <button
                            onClick={() => handleAssignStudent(selectedProgram.id, student.id)}
                            className="btn btn-sm btn-success"
                          >
                            Assign
                          </button>
                        </div>
                      ))}
                  </div>
                ) : (
                  <p>No students available.</p>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ManageStudentPrograms;