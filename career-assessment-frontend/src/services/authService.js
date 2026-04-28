import axiosInstance from './axiosInstance';

const authService = {
  register: (fullName, email, password, confirmPassword) => {
    return axiosInstance.post('/auth/register', {
      fullName,
      email,
      password,
      confirmPassword,
    });
  },

  login: ({ email, password, captchaId, captchaAnswer }) => {
    return axiosInstance.post('/auth/login', {
      email,
      password,
      captchaId,
      captchaAnswer,
    });
  },

  fetchCaptcha: () => {
    return axiosInstance.get('/auth/captcha');
  },

  validateToken: () => {
    return axiosInstance.post('/auth/validate-token');
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  getCurrentUser: () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },

  setToken: (token) => {
    localStorage.setItem('token', token);
  },

  setUser: (user) => {
    localStorage.setItem('user', JSON.stringify(user));
  },

  getToken: () => {
    return localStorage.getItem('token');
  },
};

export default authService;
