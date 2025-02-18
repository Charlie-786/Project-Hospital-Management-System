import React, { useContext, useEffect, useState } from "react";
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
import RescheduleAppointment from "./Pages/RescheduleAppointment";

const App = () => {
  const cookie = new Cookies();
  const { isAuthenticated, setIsAuthenticated, setUser } = useContext(Context);
  const [loading, setLoading] = useState(true); // Add loading state
  const token = cookie.get("token");
  const email = cookie.get("email");

  useEffect(() => {
    const fetchUser = async () => {
      setLoading(true); // Start loading
      try {
        if (token && email) {
          const response = await axios.get(
            `http://localhost:8080/api/v1/user/patient/me?email=${email}`,
            {
              withCredentials: true,
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );
          setIsAuthenticated(true);
          setUser(response.data);
        } else {
          setIsAuthenticated(false);
          setUser({});
        }
      } catch (error) {
        console.error("Error fetching user:", error);
        setIsAuthenticated(false);
        setUser({});
      } finally {
        setLoading(false); // Stop loading
      }
    };

    fetchUser();
  }, [email, setIsAuthenticated, setUser, token]); // Dependency array

  // Conditionally render based on loading state
  if (loading) {
    return <div>Loading...</div>; // Or a more appropriate loading indicator
  }

  return (
    <>
      <Router>
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/appointment" element={<Appointment />} />
          <Route
            path="/rescheduleAppointment/:appointmentId"
            element={<RescheduleAppointment />}
          />
          <Route path="/about" element={<AboutUs />} />
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
          <Route path="/profile" element={<UserDashboard />} />
          <Route
            path="/prescriptions/:appointmentId"
            element={<ViewPrescriptions />}
          />
        </Routes>
        <Footer />
        <ToastContainer position="top-center" />
      </Router>
    </>
  );
};

export default App;