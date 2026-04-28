import React, { createContext, useState, useContext, useEffect } from 'react';
import authService from '../services/authService';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Initialize from localStorage
  useEffect(() => {
    const storedUser = authService.getCurrentUser();
    const token = authService.getToken();
    
    if (storedUser && token) {
      setUser(storedUser);
    }
    setLoading(false);
  }, []);

  const login = async (loginPayload) => {
    try {
      setError(null);
      const response = await authService.login(loginPayload);
      const { data } = response.data;
      
      authService.setToken(data.token);
      authService.setUser(data);
      setUser(data);
      
      return { success: true, data };
    } catch (err) {
      const status = err.response?.status;
      const errorMsg =
        status === 401
          ? 'Login failed'
          : err.response?.data?.message || 'Login failed';
      setError(errorMsg);
      return { success: false, error: errorMsg };
    }
  };

  const register = async (fullName, email, password, confirmPassword) => {
    try {
      setError(null);
      const response = await authService.register(fullName, email, password, confirmPassword);
      const { data } = response.data;
      
      authService.setToken(data.token);
      authService.setUser(data);
      setUser(data);
      
      return { success: true, data };
    } catch (err) {
      const errorMsg = err.response?.data?.message || 'Registration failed';
      setError(errorMsg);
      return { success: false, error: errorMsg };
    }
  };

  const logout = () => {
    authService.logout();
    setUser(null);
    setError(null);
  };

  const isAuthenticated = !!user;
  const isAdmin = user?.role === 'ADMIN';
  const isStudent = user?.role === 'STUDENT';

  return (
    <AuthContext.Provider
      value={{
        user,
        loading,
        error,
        login,
        register,
        logout,
        isAuthenticated,
        isAdmin,
        isStudent,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};
