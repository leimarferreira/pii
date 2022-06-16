import request from "./request";

const register = async ({ name, email, password }) => {
  const data = {
    name,
    email,
    password,
  };

  const response = await request.post("/auth/register", data);

  if (response.status === 200) {
    const token = response.data.token;
    localStorage.setItem("token", token);
  }
};

const login = async ({ email, password }) => {
  const data = {
    email,
    password,
  };

  const response = await request.post("/auth/login", data);

  if (response.status === 200) {
    const token = response.data.token;
    localStorage.setItem("token", token);
  }
};

const logout = () => {
  const token = localStorage.getItem("token");

  if (token) {
    localStorage.removeItem("token");
  }
};

const isLoggedIn = async () => {
  const token = localStorage.getItem("token");

  if (token) {
    const response = await request.get("/auth/validatetoken");

    if (response.status === 200) {
      return response.data;
    }
  }

  return false;
};

export default {
  register,
  login,
  logout,
  isLoggedIn,
};
