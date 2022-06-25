import Form, { Submit, TextInput } from "components/Form/form";
import useTitle from "utils/hooks/useTitle";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEnvelope, faKey } from "@fortawesome/free-solid-svg-icons";
import "./login.css";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import authService from "services/authService";

const Login = () => {
  useTitle("Entre na sua conta");

  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [hasError, setError] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const submitData = async () => {
    const data = {
      email,
      password,
    };

    try {
      await authService.login(data);
      navigate("/");
    } catch (error) {
      if (error?.response?.status === 401) {
        setErrorMessage("Email ou senha inválidos.");
      } else if (error?.response?.status === 500) {
        setErrorMessage(
          "Serviço indisponível. Tente novamente em alguns instantes."
        );
      } else {
        setErrorMessage(error?.response?.data?.message ?? "Ocorreu um erro.");
      }
      setError(true);
    }
  };

  return (
    <div className="login-screen">
      <div className="background-image"></div>
      <div className="login-form">
        <Form title="Login" onSubmit={submitData}>
          <TextInput
            type="email"
            name="email"
            placeholder="Endereço de email"
            value={email}
            onChange={setEmail}
            icon={<FontAwesomeIcon icon={faEnvelope} />}
          />
          <TextInput
            type="password"
            name="password"
            placeholder="Senha"
            value={password}
            onChange={setPassword}
            icon={<FontAwesomeIcon icon={faKey} />}
          />
          <Submit className="login-submit" value="Entre" />
          <span className="register-screen-link">
            <Link to="/register">Cadastre-se</Link>
          </span>
        </Form>
        {hasError && <span className="error-message">{errorMessage}</span>}
      </div>
    </div>
  );
};

export default Login;
