import React, { useState } from 'react';
import { Link } from 'react-router';
import { useNavigate } from 'react-router-dom';
import { useAuth } from "../Utils/AuthContext";

export const LoginPage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });

  const [message, setMessage] = useState('');

  const { checkAuthStatus } = useAuth();
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8081/login', {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData),
       
      });
        const data = await response.json();
        //const jwtToken = data.token;
       
      if (response.ok) {
      
        // localStorage.setItem("jwtToken", jwtToken);
        await checkAuthStatus();
        setMessage("Login successful! ");
        navigate("/home");
       

        } else {
          const failedText = data.error;
          setMessage(failedText);
        }
      
    } catch (error) {
        console.error('Error occurred during login:', error);
      }
    };

    return (
      <div className="container py-5">
        <div className="row justify-content-center">
          <div className="col-md-6 col-lg-5">
            <div className="card shadow-sm">
              <div className="card-body p-4">
                {message && <div className="alert alert-info">{message}</div>}
                <h2 className="card-title text-center mb-4">Login</h2>

                <form onSubmit={handleSubmit}>
                  <div className="mb-3">
                    <label htmlFor="email" className="form-label">
                      Email address
                    </label>
                    <input
                      type="email"
                      className="form-control"
                      id="email"
                      name="email"
                      placeholder="Enter your email"
                      value={formData.email}
                      onChange={handleChange}
                      required
                    />
                  </div>

                  <div className="mb-3">
                    <label htmlFor="password" className="form-label">
                      Password
                    </label>
                    <input
                      type="password"
                      className="form-control"
                      id="password"
                      name="password"
                      placeholder="Enter your password"
                      value={formData.password}
                      onChange={handleChange}
                      required
                    />
                  </div>

                  <button type="submit" className="btn btn-primary w-100">
                    Sign In
                  </button>
                </form>
              </div>
            </div>
          </div>
          <div>
            <p className="text-center mt-3">
              Don't have an account? <Link to="/register">Register here</Link>
            </p>
          </div>
        </div>
      </div>
    );
  };


function useEffect(arg0: () => void, arg1: boolean[]) {
  throw new Error('Function not implemented.');
}

