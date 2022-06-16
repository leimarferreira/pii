import { Routes, Route } from "react-router-dom";
import Login from "screens/Auth/Login/login";
import Register from "./screens/Auth/Register/register";

import Home from "./screens/Home/home";

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/register" element={<Register />} />
      <Route path="/login" element={<Login />} />
    </Routes>
  );
};

export default AppRoutes;
