import React, { useEffect, useState } from 'react';
import './App.css';
import { Navbar } from './layouts/NavbarAndFooter/Navbar';
import { Footer } from './layouts/NavbarAndFooter/Footer';
import { HomePage } from './layouts/Homepage/HomePage';
import { SearchBooksPage } from './layouts/SearchBooksPage/SearchBooksPage';
import { DataFetchRuf } from './layouts/RufWork/DataFetchRuf';
import RegisterPage from './layouts/RegisterAndLogin/RegisterPage';
import { Link, Routes, Route, Router } from 'react-router-dom';
import { BookCheckoutPage } from './layouts/BookCheckoutPage/BookCheckoutPage';
import { LoginPage } from './layouts/RegisterAndLogin/LoginPage';
import { LogOutPage } from './layouts/RegisterAndLogin/LogOutPage';
import { AuthProvider } from './layouts/Utils/AuthContext';



export const App = () => {
  const[token, setToken]= useState( false);
 
  // Wrapper for logout function (LogOutPage is an async function returning Promise<void>)
  const LogOutWrapper: React.FC = () => {
    useEffect(() => {
      LogOutPage();
    }, []);
    return <div>Logging out...</div>;
  };
 

  return (
    <AuthProvider>
    <div className='d-flex flex-column min-vh-100'>
      
      <Navbar />
      <div className='flex-grow-1'>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/search" element={<SearchBooksPage />} />
          <Route path="/checkout/:bookId" element={<BookCheckoutPage />} />
          <Route path="/login"  element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="*" element={<div>Page Not Found</div>} />
        </Routes>
      </div>
      <Footer />
     
    </div>
    </AuthProvider>
  );
}