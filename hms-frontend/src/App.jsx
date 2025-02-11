import React, { useContext, useEffect } from "react";
import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./Pages/Home";
import Appointment from "./Pages/Appointment";
import AboutUs from "./Pages/AboutUs";
import Register from "./Pages/Register";
import Footer from "./components/Footer";
import Navbar from "./components/Navbar";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import axios from "axios";
import { Context } from "./main";
import Login from "./Pages/Login";
import Cookies from "universal-cookie";
import UserDashboard from "./components/UserDashboard";
import ViewPrescriptions from "./components/ViewPrescription";

const App = () => {
  const cookie = new Cookies();
  const { isAuthenticated, setIsAuthenticated, setUser , setUserId } = useContext(Context);
  const token = cookie.get("token");
  const email = cookie.get("email");

  useEffect(() => {
    // Check if both token and email exist in cookies after reload
    if (token && email) {
      const fetchUser = async () => {
        try {
          const response = await axios.get(
            `http://localhost:8080/api/v1/user/patient/me?email=${email}`,
            {
              withCredentials: true,
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );
          // console.log("User:", response.data.firstName + " " + response.data.lastName);
          
          setIsAuthenticated(true);
          setUser(response.data);
        } catch (error) {
          console.error("Error fetching user:", error);
          setIsAuthenticated(false);
          setUser({});
        }
      };

      fetchUser();
    } else {
      // If no token or email in cookies, reset authentication state
      setIsAuthenticated(false);
      setUser({});
    }
  }, [isAuthenticated, email, token, setIsAuthenticated, setUser]);

  return (
    <>
      <Router>
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/appointment" element={<Appointment />} />
          <Route path="/about" element={<AboutUs />} />
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
          <Route path="/profile" element={<UserDashboard />} />
          <Route path="/prescriptions/:appointmentId" element={<ViewPrescriptions/>} />
        </Routes>
        <Footer />
        <ToastContainer position="top-center" />
      </Router>
    </>
  );
};

export default App;
