import axios from "axios";
import {
  loginStart,
  loginSuccess,
  loginFailure,
  clearError,
} from "./authSlice";

const API_URL = "http://localhost:8080/auth/v1";

const decodeToken = (token) => {
  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return { username: payload.sub, role: payload.role };
  } catch {
    return null;
  }
};

export const loginUser = (credentials, navigate) => async (dispatch) => {
  try {
    dispatch(loginStart());

    const response = await axios.post(`${API_URL}/login`, credentials);
    const { token } = response.data;

    if (token) {
      const user = decodeToken(token); 
      dispatch(loginSuccess({ token, user }));
      navigate("/dashboard");
    }
  } catch (err) {
    const message =
      err.response?.data?.message ||
      (err.response?.status === 404 ? "User not found" : "Invalid credentials");
    dispatch(loginFailure(message));
  }
};

export const signupUser = (userData, navigate) => async (dispatch) => {
  try {
    dispatch(clearError()); 

    await axios.post(`${API_URL}/signup`, userData);

    navigate("/login");
  } catch (err) {
    const message =
      err.response?.data?.message || "Signup failed. Try again.";
    dispatch(loginFailure(message));
  }
};
