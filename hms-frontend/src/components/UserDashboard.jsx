import React, { useContext, useEffect, useState } from "react";
import { Context } from "../main";
import { Navigate, useNavigate } from "react-router-dom";
import axios from "axios";
import { GoCheckCircleFill } from "react-icons/go";
import { AiFillCloseCircle } from "react-icons/ai";
import "../components/UserDashboard.css";
import Cookies from "universal-cookie";
const UserDashboard = () => {
  const cookie = new Cookies();
  const navigate = useNavigate();
  const [appointments, setAppointments] = useState([]);

  useEffect(() => {
    const fetchAppointments = async () => {
      try {
        const token = cookie.get("token");
        const { data } = await axios.get(
          `http://localhost:8080/api/v1/user/user/${user.id}/all`,
          {
            withCredentials: true,
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        // console.log("data", data);
        
        setAppointments(data);
      } catch (error) {
        setAppointments([]);
      }
    };
    fetchAppointments();
  }, []);
  
  const handlePrescribeMedicine = (appointmentId) => {
    navigate(`/prescriptions/${appointmentId}`); // Navigate using useNavigate
  };
  const { isAuthenticated, user } = useContext(Context);
  if (!isAuthenticated) {
    return <Navigate to={"/login"} />;
  }
  console.log("user", user);
  return (
    <>
      <section className="dashboard page">
        <div className="banner">
          <div className="firstBox">
            <div className="image">
              <img src="/download.png" alt="UserImg" />
            </div>
            <div className="content">
              <div>
                <p>Hello ,</p>
                <h5>{user && `${user.firstName} ${user.lastName}`} </h5>
              </div>
              <p>
                Lorem ipsum dolor sit, amet consectetur adipisicing elit.
                Facilis, nam molestias. Eaque molestiae ipsam commodi neque.
                Assumenda repellendus necessitatibus itaque.
              </p>
            </div>
          </div>
          <div className="secondBox">
            <p>Total Appointments</p>
            <h3>{appointments.length}</h3>
          </div>
          <div className="thirdBox">
            <p>Registered Doctors</p>
            <h3>10</h3>
          </div>
        </div>
        <div className="banner">
          <h5>Appointments</h5>
          <table>
            <thead>
              <tr>
                <th>Patient</th>
                <th>Date</th>
                <th>Doctor</th>
                <th>Department</th>
                <th>Status</th>
                <th>Visited</th>
                <th>Prescription</th>
              </tr>
            </thead>
            <tbody>
              {appointments.length > 0 ? (
                appointments.map((appointment) => (
                  <tr key={appointment.id}>
                    <td>{`${appointment.patient.firstName} ${appointment.patient.lastName}`}</td>
                    <td>
                      {new Date(appointment.appointmentDate).toLocaleString()}
                    </td>
                    <td>{`${appointment.doctor.firstName} ${appointment.doctor.lastName}`}</td>
                    <td>{appointment.department}</td>
                    <td>
                      <h6
                        className={
                          appointment.status === "Pending"
                            ? "value-pending"
                            : appointment.status === "Accepted"
                            ? "value-accepted"
                            : "value-rejected"
                        }
                        value={appointment.status || "Pending"}
                      >
                        {appointment.status || "Pending"}
                      </h6>
                    </td>
                    <td>
                      {appointment.hasVisited ? (
                        <GoCheckCircleFill className="green" />
                      ) : (
                        <AiFillCloseCircle className="red" />
                      )}
                    </td>
                    <td>
                      <button
                        onClick={() => handlePrescribeMedicine(appointment.id)}
                      >
                        View Prescription
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="7">No Appointments Found!</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>
    </>
  );
};

export default UserDashboard;
