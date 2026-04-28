import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import testService from '../services/testService';

const AdminResults = () => {
  const navigate = useNavigate();
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchAllResults();
  }, []);

  const fetchAllResults = async () => {
    try {
      const response = await testService.getAllResults();
      setResults(response.data.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="spinner"></div>;

  return (
    <div className="container mt-3">
      <h1>All Test Results</h1>

      <button className="btn btn-secondary mb-3" onClick={() => navigate('/admin/dashboard')}>
        ← Back to Dashboard
      </button>

      <table>
        <thead>
          <tr>
            <th>Test Name</th>
            <th>Date</th>
            <th>Score</th>
            <th>Correct Answers</th>
          </tr>
        </thead>
        <tbody>
          {results.map((result) => (
            <tr key={result.id}>
              <td>{result.testName}</td>
              <td>{new Date(result.completedAt).toLocaleDateString()}</td>
              <td className="font-bold">{Math.round(result.scorePercentage)}%</td>
              <td>{result.correctAnswers}/{result.totalQuestions}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminResults;
