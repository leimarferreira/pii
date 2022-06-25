import Header from "components/Header/header";
import { Navigate, Outlet } from "react-router-dom";

const App = () => {
  const userAuthenticated = true;

  return userAuthenticated ? (
    <>
      <Header />
      <Outlet />
    </>
  ) : (
    <Navigate to="/login" />
  );
};

export default App;
