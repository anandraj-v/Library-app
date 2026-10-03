import React, { createContext, useState, useEffect, useContext } from 'react';

export const authenticatedFetch = async (url, options = {}) => {
  const fetchOptions = {
    ...options,
    credentials: 'include', // Automatically attaches HTTP-Only cookies
  };

  let response = await fetch(url, fetchOptions);

  // If access token is expired (401), attempt to refresh
  if (response.status === 401 && !url.includes('/refresh')) {
    try {
      const refreshResponse = await fetch('http://localhost:8081/refresh', {
        method: 'POST',
        credentials: 'include',
      });

      if (refreshResponse.ok) {
        // Retry original request after new accessToken cookie is set
        response = await fetch(url, fetchOptions);
      } else {
        // Refresh token failed -> Force redirect to login
        window.location.href = '/login';
      }
    } catch (refreshError) {
      console.error('Refresh token request failed:', refreshError);
      window.location.href = '/login';
    }
  }

  return response;
};


const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Check if user is authenticated when app loads
  const checkAuthStatus = async () => {
    try {
      const response = await fetch('http://localhost:8081/me', {
        method: 'GET',
        credentials: 'include', // Automatically sends HTTP-Only accessToken cookie
      });

      if (response.ok) {
        const data = await response.json();
        setUser({ username: data.email || data.username });
      } else {
        setUser(null);
      }
    } catch (error) {
      console.error('Auth status check failed:', error);
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    checkAuthStatus();
  }, []);

  // Logout handler
  const logout = async () => {
    try {
      await fetch('http://localhost:8081/api/auth/logout', {
        method: 'POST',
        credentials: 'include',
      });
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      setUser(null); // Clear state locally regardless
    }
  };

  return (
    <AuthContext.Provider value={{ user, setUser, logout, loading, checkAuthStatus }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);