import { NavLink } from "react-router-dom";

export const Navbar = () => {
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
                        <li className='nav-item'>
                            <NavLink className='nav-link' to='/search'>Books</NavLink>
                        </li>
                    </ul>
                    <ul className='navbar-nav ms-auto'>
                        <li className='nav-item'>
                            <a type='button' className='btn btn-outline-light' href='#'>Login</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    );
}