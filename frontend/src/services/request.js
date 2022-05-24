import axios from "axios";

const request = axios.create({
  // eslint-disable-next-line no-undef
  baseURL: process.env.REACT_APP_BASE_URL,
});

request.interceptors.request.use(async (config) => {
  const token = localStorage.getItem("token");
  config.headers = {
    Authorization: token,
  };

  return config;
});

export default request;
