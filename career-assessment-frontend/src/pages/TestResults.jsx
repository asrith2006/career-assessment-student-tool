import React, { useEffect, useState } from 'react';
import { useLocation, useParams, useNavigate } from 'react-router-dom';
import testService from '../services/testService';
import '../styles/results.css';

const TestResults = () => {
  const { id } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const [result, setResult] = useState(null);
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState(location.state?.successMessage || '');

  useEffect(() => {
    if (id) {
      fetchTestResult();
    } else {
      fetchAllResults();
    }
  }, [id]);

  useEffect(() => {
    if (location.state?.successMessage) {
      setSuccessMessage(location.state.successMessage);
    }
  }, [location.state]);

  const fetchTestResult = async () => {
    try {
      const response = await testService.getTestResult(id);
      setResult(response.data.data);
      setError(null);
    } catch (err) {
      setError('Failed to load result');
    } finally {
      setLoading(false);
    }
  };

  const fetchAllResults = async () => {
    try {
      const response = await testService.getMyResults();
      setResults(response.data.data);
      setError(null);
    } catch (err) {
      setError('Failed to load results');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="container mt-3"><div className="spinner"></div></div>;

  if (error) {
    return <div className="container mt-3"><div className="alert alert-danger">{error}</div></div>;
  }

  if (id && result) {
    return (
      <div className="results-container">
        <div className="container">
          <h1>Test Result</h1>
          <button className="btn btn-secondary mb-3" onClick={() => navigate('/student/results')}>
            ← Back to My Test Results
          </button>
          
          <div className="result-card">
            <div className="result-header">
              <h2>{result.testName}</h2>
              <p className="text-muted">{new Date(result.completedAt).toLocaleDateString()}</p>
            </div>

            <div className="result-score">
              <div className="score-display">
                <div className="score-circle">
                  {Math.round(result.scorePercentage)}%
                </div>
                <p>{result.correctAnswers} of {result.totalQuestions} correct</p>
              </div>
            </div>

            {result.recommendedCareers && result.recommendedCareers.length > 0 && (
              <div className="recommendations">
                <h3>Recommended Careers</h3>
                <div className="career-list">
                  {result.recommendedCareers.map((career) => (
                    <div key={career.id} className="career-item">
                      <h4>{career.careerName}</h4>
                      <p className="match-score">Match: {Math.round(career.matchPercentage)}%</p>
                      <p>{career.description}</p>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="results-container">
      <div className="container">
        <h1>My Test Results</h1>
        <button className="btn btn-secondary mb-3" onClick={() => navigate('/student/dashboard')}>
          ← Back to Dashboard
        </button>
        {successMessage && (
          <div className="alert alert-success">
            {successMessage}
          </div>
        )}
        
        {results.length === 0 ? (
          <div className="alert alert-info">
            No test results yet. <a href="/student/take-test">Take a test</a>
          </div>
        ) : (
          <div className="results-table">
            <table>
              <thead>
                <tr>
                  <th>Test Name</th>
                  <th>Date</th>
                  <th>Score</th>
                  <th>Correct Answers</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {results.map((result) => (
                  <tr key={result.id}>
                    <td>{result.testName}</td>
                    <td>{new Date(result.completedAt).toLocaleDateString()}</td>
                    <td className="font-bold">{Math.round(result.scorePercentage)}%</td>
                    <td>{result.correctAnswers}/{result.totalQuestions}</td>
                    <td>
                      <a href={`/student/results/${result.id}`} className="btn btn-primary">
                        View
                      </a>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default TestResults;
