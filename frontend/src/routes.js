import App from "app";
import { Routes, Route } from "react-router-dom";
import Login from "screens/Auth/Login/login";
import NotFound from "screens/NotFound/notFound";
import Register from "./screens/Auth/Register/register";

import Home from "./screens/Home/home";

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<App />}>
        <Route index element={<Home />} />
      </Route>
      <Route path="/register" element={<Register />} />
      <Route path="/login" element={<Login />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};

export default AppRoutes;
