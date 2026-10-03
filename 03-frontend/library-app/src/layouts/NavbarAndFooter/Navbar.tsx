import { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import { useAuth } from "../Utils/AuthContext";


export const Navbar = () => {

    
    const { user, logout, loading } = useAuth();

  if (loading) return <div>Loading...</div>;
   
    const handleLogout = async () => {

         try {
         await fetch('http://localhost:8081//api/auth/logout', {
            method: 'POST',
            credentials: 'include',
            headers: {
               // 'Authorization': `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        }
        )
    }catch (error) {
        console.error("Backend logout cleanup failed:", error);
    }
        
    
    }

    return (
        <nav className='navbar navbar-expand-lg navbar-dark main-color py-3'>
            <div className='container-fluid'>
                <span className='navbar-brand'>Library App</span>
                <button className='navbar-toggler' type='button' data-bs-toggle='collapse'
                    data-bs-target='#navbarNav' aria-controls='navbarNav'
                    aria-expanded='false' aria-label='Toggle navigation'>
                    <span className='navbar-toggler-icon'></span>
                </button>
                <div className='collapse navbar-collapse' id='navbarNav'>
                    <ul className='navbar-nav'>
                        <li className='nav-item'>
                            <NavLink className='nav-link active' aria-current='page' to='/'>Home</NavLink>
                        </li>
                       {user && (
                        <li className='nav-item'>
                            <NavLink className='nav-link' to='/search'>Books</NavLink>
                        </li>)
                    }
                    </ul>
                    <ul className='navbar-nav ms-auto'>
                        <li className='nav-item'>
                            {user ? (
                                <NavLink className='nav-link' to='/' onClick={logout}>Logout</NavLink>
                            ) : (       
                                <NavLink className='nav-link' to='/login'>Login</NavLink>
                            )}
                        
                        
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    );
}