import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import authService from '../services/authService';
import '../styles/auth.css';

const Login = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    captchaAnswer: '',
  });
  const [captcha, setCaptcha] = useState({
    captchaId: '',
    question: '',
  });
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false);
  const [isCaptchaLoading, setIsCaptchaLoading] = useState(true);
  const { login } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    loadCaptcha();
  }, []);

  const loadCaptcha = async () => {
    setIsCaptchaLoading(true);
    setErrors(prev => ({ ...prev, submit: '' }));

    try {
      const response = await authService.fetchCaptcha();
      const { data } = response.data;
      setCaptcha({ captchaId: data.captchaId, question: data.question });
      setFormData(prev => ({ ...prev, captchaAnswer: '' }));
    } catch (err) {
      setErrors({ submit: 'Unable to load captcha. Please refresh the page.' });
    } finally {
      setIsCaptchaLoading(false);
    }
  };

  const validateForm = () => {
    const newErrors = {};
    if (!formData.email) newErrors.email = 'Email is required';
    else if (!/\S+@\S+\.\S+/.test(formData.email)) newErrors.email = 'Email is invalid';
    
    if (!formData.password) newErrors.password = 'Password is required';
    else if (formData.password.length < 6) newErrors.password = 'Password must be at least 6 characters';

    if (!formData.captchaAnswer) newErrors.captchaAnswer = 'Captcha answer is required';

    return newErrors;
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: '',
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const newErrors = validateForm();
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    setIsLoading(true);
    try {
      const result = await login({
        email: formData.email,
        password: formData.password,
        captchaId: captcha.captchaId,
        captchaAnswer: formData.captchaAnswer,
      });
      if (result.success) {
        navigate(result.data.role === 'ADMIN' ? '/admin/dashboard' : '/student/dashboard');
      } else {
        setErrors({ submit: result.error });
      }
    } catch (error) {
      setErrors({ submit: 'An error occurred. Please try again.' });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <h1>Career Assessment</h1>
          <p>Student / Admin Portal</p>
        </div>

        <form onSubmit={handleSubmit}>
          {errors.submit && (
            <div className="alert alert-danger">
              {errors.submit}
            </div>
          )}

          <div className="form-group">
            <label htmlFor="email">Email Address</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              placeholder="Enter your email"
            />
            {errors.email && <span className="error-message">{errors.email}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleInputChange}
              placeholder="Enter your password"
            />
            {errors.password && <span className="error-message">{errors.password}</span>}
          </div>

          <div className="form-group captcha-group">
            <label htmlFor="captchaAnswer">Enter verification code</label>
            <div className="captcha-row">
              <div className="captcha-box">
                {isCaptchaLoading ? 'Loading code...' : captcha.question || 'Unable to load code'}
              </div>
              <button
                type="button"
                className="btn btn-secondary btn-sm"
                onClick={loadCaptcha}
                disabled={isCaptchaLoading}
              >
                Refresh
              </button>
            </div>
            <input
              type="text"
              id="captchaAnswer"
              name="captchaAnswer"
              value={formData.captchaAnswer}
              onChange={handleInputChange}
              placeholder="Enter verification code"
            />
            {errors.captchaAnswer && <span className="error-message">{errors.captchaAnswer}</span>}
          </div>

          <button 
            type="submit" 
            className="btn btn-primary btn-block"
            disabled={isLoading || isCaptchaLoading}
          >
            {isLoading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        <div className="auth-footer">
          <p>Don't have an account? <Link to="/register">Register here</Link></p>
          <p className="demo-login">
            <strong>Demo Admin:</strong> admin@assessment.com / admin123
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;
