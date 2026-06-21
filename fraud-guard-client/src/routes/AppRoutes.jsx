import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Dashboard from "../pages/Dashboard"; 
import AdminDashboard from "../pages/AdminDashboard";
import Login from "../pages/Login";
import Signup from "../pages/Signup";
import { jwtDecode } from "jwt-decode";

const AppRoutes = () => {
  const token = localStorage.getItem("token");
  const user = token ? jwtDecode(token) : null;

  return (
    <Router>
      <Routes>
        <Route
          path="/"
          element={user ? <Navigate to="/dashboard" /> : <Navigate to="/login" />}
        />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route
          path="/dashboard"
          element={user ? <Dashboard /> : <Navigate to="/login" />}
        />
        <Route
          path="/adminDashboard"
          element={user && user.role === "ADMIN" ? <AdminDashboard /> : <Navigate to="/dashboard" />}
        />
      </Routes>
    </Router>
  );
};

export default AppRoutes;
