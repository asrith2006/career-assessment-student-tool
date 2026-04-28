import React, { useState, useEffect } from 'react';
import careerService from '../services/careerService';

const ManageCareers = () => {
  const [careers, setCareers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    careerName: '',
    description: '',
    salaryRange: '',
  });

  useEffect(() => {
    fetchCareers();
  }, []);

  const fetchCareers = async () => {
    try {
      const response = await careerService.getAllCareers();
      setCareers(response.data.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await careerService.createCareer(formData);
      setShowForm(false);
      setFormData({ careerName: '', description: '', salaryRange: '' });
      fetchCareers();
    } catch (err) {
      console.error(err);
    }
  };

  if (loading) return <div className="spinner"></div>;

  return (
    <div className="container mt-3">
      <h1>Manage Careers</h1>

      <button onClick={() => setShowForm(!showForm)} className="btn btn-primary mb-2">
        {showForm ? 'Cancel' : 'Add Career'}
      </button>

      {showForm && (
        <form onSubmit={handleSubmit} className="card mb-3">
          <div className="form-group">
            <label>Career Name</label>
            <input
              type="text"
              value={formData.careerName}
              onChange={(e) => setFormData({ ...formData, careerName: e.target.value })}
              required
            />
          </div>
          <div className="form-group">
            <label>Description</label>
            <textarea
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            ></textarea>
          </div>
          <div className="form-group">
            <label>Salary Range</label>
            <input
              type="text"
              value={formData.salaryRange}
              onChange={(e) => setFormData({ ...formData, salaryRange: e.target.value })}
            />
          </div>
          <button type="submit" className="btn btn-success">Save Career</button>
        </form>
      )}

      <table>
        <thead>
          <tr>
            <th>Career Name</th>
            <th>Description</th>
            <th>Salary</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {careers.map((c) => (
            <tr key={c.id}>
              <td>{c.careerName}</td>
              <td>{c.description?.substring(0, 50)}...</td>
              <td>{c.salaryRange}</td>
              <td>
                <button onClick={() => careerService.deleteCareer(c.id)} className="btn btn-danger">
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ManageCareers;
