import React, { useEffect, useState } from 'react';
import careerService from '../services/careerService';
import '../styles/results.css';

const CareerCatalog = () => {
  const [careers, setCareers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState('');
  const [appliedCareerIds, setAppliedCareerIds] = useState([]);

  useEffect(() => {
    fetchCareers();
  }, []);

  const fetchCareers = async () => {
    try {
      const response = await careerService.getAllCareers();
      setCareers(response.data.data);
    } catch (err) {
      setError('Failed to load careers');
    } finally {
      setLoading(false);
    }
  };

  const handleApply = (careerId, careerName) => {
    setAppliedCareerIds((prev) => [...prev, careerId]);
    setSuccessMessage(`Apply done for ${careerName}.`);
    setTimeout(() => setSuccessMessage(''), 3000);
  };

  if (loading) return <div className="container mt-3"><div className="spinner"></div></div>;

  return (
    <div className="results-container">
      <div className="container">
        <h1>Career Catalog</h1>
        
        {error && <div className="alert alert-danger">{error}</div>}
        {successMessage && <div className="alert alert-success">{successMessage}</div>}

        <div className="grid grid-3">
          {careers.map((career) => (
            <div key={career.id} className="card">
              <h3>{career.careerName}</h3>
              <p>{career.description}</p>
              <div className="career-details">
                <p><strong>Salary Range:</strong> {career.salaryRange}</p>
                <p><strong>Job Outlook:</strong> {career.jobOutlook}</p>
              </div>
              <button
                className="btn btn-primary career-apply-btn"
                onClick={() => handleApply(career.id, career.careerName)}
                disabled={appliedCareerIds.includes(career.id)}
              >
                {appliedCareerIds.includes(career.id) ? 'Applied' : 'Apply'}
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default CareerCatalog;
