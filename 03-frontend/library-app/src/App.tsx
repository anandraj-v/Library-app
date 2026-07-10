import React from 'react';
import './App.css';
import { Navbar } from './layouts/NavbarAndFooter/Navbar';
import { Footer } from './layouts/NavbarAndFooter/Footer';
import { HomePage } from './layouts/Homepage/HomePage';
import { SearchBooksPage } from './layouts/SearchBooksPage/SearchBooksPage';
import { DataFetchRuf } from './layouts/RufWork/DataFetchRuf';
import { Link, Routes, Route } from 'react-router-dom';
import { BookCheckoutPage } from './layouts/BookCheckoutPage/BookCheckoutPage';


export const App = () => {
  return (
    <div className='d-flex flex-column min-vh-100'>
      <Navbar />
      <div className='flex-grow-1'>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/search" element={<SearchBooksPage />} />
          <Route path="/checkout/:bookId" element={<BookCheckoutPage />} />
          <Route path="*" element={<div>Page Not Found</div>} />
        </Routes>
      </div>
      <Footer />

    </div>
  );
}