import request from "./request";

const register = async ({ name, email, password }) => {
  const data = {
    name,
    email,
    password,
  };

  const response = await request.post("/auth/public/register", data);

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

  const response = await request.post("/auth/public/login", data);

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
    try {
      const response = await request.get("/auth/validate");

      if (response.status === 200) {
        return response.data;
      }

      return false;
    } catch {
      return false;
    }
  }
};

export default {
  register,
  login,
  logout,
  isLoggedIn,
};
