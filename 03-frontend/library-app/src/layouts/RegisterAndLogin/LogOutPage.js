export const LogOutPage = async () => {
    const token = localStorage.getItem("jwtToken");
    try {
         await fetch('http://localhost:8081/logout', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Authorization': `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        }
        )
    }catch (error) {
        console.error("Backend logout cleanup failed:", error);
    }finally {
       
        //localStorage.removeItem('token');

       // delete axios.defaults.headers.common['Authorization'];
       
       // window.location.href = '/login'; 
    }
}