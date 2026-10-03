import React, { useEffect, useState } from 'react';
import { authenticatedFetch } from './context/AuthContext';

const BookList = () => {
  const [books, setBooks] = useState([]);

  useEffect(() => {
    const fetchBooks = async () => {
      const response = await authenticatedFetch('http://localhost:8081/api/books');
      if (response.ok) {
        const data = await response.json();
        setBooks(data);
      }
    };

    fetchBooks();
  }, []);

  return (
    <div>
      <h2>Books Directory</h2>
      {books.map((book) => (
        <p key={book.id}>{book.title}</p>
      ))}
    </div>
  );
};

export default BookList;