import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { signupUser } from "../features/auth/authActions";
import { clearError } from "../features/auth/authSlice";
import { Link, useNavigate } from "react-router-dom";

const Signup = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { loading, error } = useSelector((state) => state.auth);

  const [form, setForm] = useState({ username: "", password: "" });

  useEffect(() => {
    dispatch(clearError());
  }, [dispatch]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    dispatch(signupUser(form, navigate));
  };

  return (
    <div className="relative flex flex-col items-center justify-center min-h-screen bg-gradient-to-br from-gray-900 via-blue-900 to-purple-900 overflow-hidden">

      <svg className="absolute inset-0 w-full h-full" xmlns="http://www.w3.org/2000/svg">
        {[...Array(20)].map((_, i) => (
          <line
            key={`h${i}`}
            x1="0"
            y1={(i * 5) + '%'}
            x2="100%"
            y2={(i * 5) + '%'}
            stroke="rgba(255,255,255,0.05)"
            strokeWidth="1"
          />
        ))}
        {[...Array(20)].map((_, i) => (
          <line
            key={`v${i}`}
            x1={(i * 5) + '%'}
            y1="0"
            x2={(i * 5) + '%'}
            y2="100%"
            stroke="rgba(255,255,255,0.05)"
            strokeWidth="1"
          />
        ))}

        {[...Array(15)].map((_, i) => (
          <circle
            key={i}
            cx={`${Math.random() * 100}%`}
            cy={`${Math.random() * 100}%`}
            r="5"
            fill="rgba(0,255,255,0.1)"
          />
        ))}
      </svg>

      {/* Form card */}
      <div className="relative z-10 bg-white/90 backdrop-blur-md p-8 rounded-2xl shadow-2xl w-96 hover:scale-105 transition-transform duration-300">
        <h1 className="text-4xl font-bold mb-10 text-blue-600 text-center">Fraud Guard</h1>
        <h2 className="text-2xl font-bold mb-6 text-center">Signup</h2>
        {error && <p className="text-red-500 mb-4">{error}</p>}
        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="text"
            name="username"
            placeholder="Username"
            value={form.username}
            onChange={handleChange}
            className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
          />
          <input
            type="password"
            name="password"
            placeholder="Password"
            value={form.password}
            onChange={handleChange}
            className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
          />
          <button
            type="submit"
            className="w-full bg-blue-500 text-white p-3 rounded-lg hover:bg-blue-600 transition"
            disabled={loading}
          >
            {loading ? "Signing up..." : "Signup"}
          </button>
        </form>
        <p className="mt-4 text-center">
          Already have an account?{" "}
          <Link
            to="/login"
            className="text-blue-500 hover:underline"
            onClick={() => dispatch(clearError())}
          >
            Login
          </Link>
        </p>
      </div>
    </div>
  );
};

export default Signup;
