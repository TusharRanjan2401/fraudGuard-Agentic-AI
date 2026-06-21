import { Link, useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { logout } from "../features/auth/authSlice";

const Navbar = () => {
  const user = useSelector((state) => state.auth.user);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch(logout());
    navigate("/login");
  };

  return (
    <nav className="bg-gradient-to-r from-blue-600 to-indigo-600 text-white p-4 flex justify-between items-center shadow-md">
      <div className="flex items-center gap-4">
        <h1 className="text-2xl font-bold">FraudGuard</h1>
        <Link className="hover:underline" to="/dashboard">
          Dashboard
        </Link>
        {user?.role === "ADMIN" && (
          <Link className="hover:underline" to="/adminDashboard">
            Admin Dashboard
          </Link>
        )}
      </div>
      <div className="flex items-center gap-4">
        <span>Hello, {user?.username}</span>
        <button
          onClick={handleLogout}
          className="bg-red-500 px-3 py-1 rounded hover:bg-red-600 transition"
        >
          Logout
        </button>
      </div>
    </nav>
  );
};

export default Navbar;
