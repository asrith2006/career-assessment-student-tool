import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import questionService from '../services/questionService';
import testService from '../services/testService';
import '../styles/test.css';

const TakeTest = () => {
  const [questions, setQuestions] = useState([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [responses, setResponses] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [testName, setTestName] = useState('Career Assessment Test');
  const [submitted, setSubmitted] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    fetchQuestions();
  }, []);

  const fetchQuestions = async () => {
    try {
      setLoading(true);
      const response = await questionService.getRandomQuestions(10);
      setQuestions(response.data.data);
      setError(null);
    } catch (err) {
      setError('Failed to load questions. Please try again.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleAnswerSelect = (answerId) => {
    setResponses(prev => ({
      ...prev,
      [currentQuestionIndex]: answerId,
    }));
  };

  const handleNext = () => {
    if (currentQuestionIndex < questions.length - 1) {
      setCurrentQuestionIndex(currentQuestionIndex + 1);
    }
  };

  const handlePrevious = () => {
    if (currentQuestionIndex > 0) {
      setCurrentQuestionIndex(currentQuestionIndex - 1);
    }
  };

  const handleSubmit = async () => {
    if (Object.keys(responses).length !== questions.length) {
      setError('Please answer all questions before submitting.');
      return;
    }

    setSubmitting(true);
    try {
      const submissionData = questions.map((question, index) => ({
        questionId: question.id,
        selectedAnswerId: responses[index],
      }));

      const response = await testService.submitTest(testName, submissionData);
      setSubmitted(true);
      
      // Redirect to My Test Results after 2 seconds
      setTimeout(() => {
        navigate('/student/results', {
          state: {
            successMessage: 'Test submitted successfully. Your new score is now available in My Test Results.',
            newResultId: response.data.data.id,
          },
        });
      }, 2000);
    } catch (err) {
      setError('Failed to submit test. Please try again.');
      console.error(err);
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="test-container">
        <div className="spinner"></div>
        <p>Loading questions...</p>
      </div>
    );
  }

  if (submitted) {
    return (
      <div className="test-container">
        <div className="alert alert-success">
          <h2>Test Submitted Successfully!</h2>
          <p>Your results are being processed. Redirecting...</p>
        </div>
      </div>
    );
  }

  if (!questions.length) {
    return (
      <div className="test-container">
        <div className="alert alert-danger">
          No questions available. Please try again later.
        </div>
      </div>
    );
  }

  const currentQuestion = questions[currentQuestionIndex];
  const isAnswered = responses[currentQuestionIndex] !== undefined;
  const answeredCount = Object.keys(responses).length;
  const isLastQuestion = currentQuestionIndex === questions.length - 1;

  return (
    <div className="test-container">
      <div className="container">
        {error && <div className="alert alert-danger">{error}</div>}

        <div className="test-header">
          <div className="test-progress">
            <h3>Question {currentQuestionIndex + 1} of {questions.length}</h3>
            <div className="progress-bar">
              <div 
                className="progress-fill" 
                style={{ width: `${((currentQuestionIndex + 1) / questions.length) * 100}%` }}
              ></div>
            </div>
          </div>
          <div className="test-info">
            <span>{answeredCount}/{questions.length} answered</span>
          </div>
        </div>

        <div className="test-content">
          <div className="question-card">
            <div className="question-header">
              <div className="question-category">
                {currentQuestion.category}
              </div>
              <div className={`difficulty difficulty-${currentQuestion.difficulty}`}>
                Difficulty: {currentQuestion.difficulty === 1 ? 'Easy' : currentQuestion.difficulty === 2 ? 'Medium' : 'Hard'}
              </div>
            </div>

            <div className="question-text">
              <h2>{currentQuestion.questionText}</h2>
              {currentQuestion.description && (
                <p className="question-description">{currentQuestion.description}</p>
              )}
            </div>

            <div className="answers-container">
              {currentQuestion.answers.map((answer) => (
                <label key={answer.id} className="answer-option">
                  <input
                    type="radio"
                    name="answer"
                    value={answer.id}
                    checked={responses[currentQuestionIndex] === answer.id}
                    onChange={() => handleAnswerSelect(answer.id)}
                  />
                  <span className="answer-text">{answer.answerText}</span>
                </label>
              ))}
            </div>
          </div>

          <div className="test-navigation">
            <button 
              onClick={handlePrevious} 
              className="btn btn-secondary"
              disabled={currentQuestionIndex === 0}
            >
              ← Previous
            </button>

            <div className="question-indicators">
              {questions.map((_, index) => (
                <button
                  key={index}
                  onClick={() => setCurrentQuestionIndex(index)}
                  className={`indicator ${index === currentQuestionIndex ? 'active' : ''} ${responses[index] !== undefined ? 'answered' : ''}`}
                  title={`Question ${index + 1}`}
                >
                  {index + 1}
                </button>
              ))}
            </div>

            {isLastQuestion ? (
              <button 
                onClick={handleSubmit}
                className="btn btn-success"
                disabled={submitting || !isAnswered}
              >
                {submitting ? 'Submitting...' : 'Submit Test'}
              </button>
            ) : (
              <button 
                onClick={handleNext}
                className="btn btn-primary"
                disabled={!isAnswered}
              >
                Next →
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default TakeTest;
