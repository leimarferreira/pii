import Header from "components/Header/header";
import { useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
import authService from "services/authService";

const App = () => {
  const [userLoggedIn, setUserLoggedIn] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    authService.isLoggedIn().then((result) => {
      setUserLoggedIn(result);
      setLoading(false);
    });
  }, []);

  return loading ? (
    <></>
  ) : (
    <>
      {userLoggedIn ? (
        <>
          <Header />
          <Outlet />
        </>
      ) : (
        <Navigate to="/auth/login" />
      )}
    </>
  );
};

export default App;
