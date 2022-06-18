import { useEffect, useState } from "react";
import Form, { Checkbox, Field, Submit, TextInput } from "components/Form/form";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faUser,
  faEnvelope,
  faKey,
  faFloppyDisk,
} from "@fortawesome/free-solid-svg-icons";
import "./register.css";
import { Link } from "react-router-dom";
import useTitle from "utils/hooks/useTitle";
import authService from "services/authService";

const Register = () => {
  useTitle("Cadastre-se");

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [isPasswordValid, setPasswordValid] = useState(false);
  const [isPasswordConfirmed, setPasswordConfirmed] = useState(false);
  const [areUseTermsAccepted, setUserTermsAccepted] = useState(false);
  const [hasError, setError] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    validatePassword();
  }, [password]);

  useEffect(() => {
    if (confirmPassword.length !== 0) {
      setPasswordConfirmed(confirmPassword === password);
    }
  }, [password, confirmPassword]);

  // TODO: passar a validação para dentro do campo e exibir uma mensagem de erro
  const validatePassword = () => {
    setPasswordValid(
      password.length >= 8 && // deve ter no mínimo 8 caracteres
        /[A-Z]/g.test(password) && // deve ter pelo menos uma letra maiúscula
        /[a-z]/g.test(password) && // deve ter pelo menos uma letra minúscula
        /\d/g.test(password) && // deve ter pelo menos um dígito
        /\D|_/g.test(password) // deve ter pelo menos um caractere especial
    );
  };

  const submitData = async () => {
    const data = {
      name,
      email,
      password,
    };

    try {
      authService.register(data);
    } catch (error) {
      // TODO: tratar esse erro corretamente
      setErrorMessage(error.message);
      setError(true);
    }
  };

  return (
    <div className="content">
      <div className="register-background"></div>
      <div className="register-form">
        <Form title={"Criar conta"} onSubmit={submitData}>
          <TextInput
            type="text"
            name="full-name"
            placeholder="Nome completo"
            icon={<FontAwesomeIcon icon={faUser} />}
            value={name}
            onChange={setName}
          />
          <TextInput
            type="text"
            name="email"
            placeholder="Endereço de email"
            icon={<FontAwesomeIcon icon={faEnvelope} />}
            value={email}
            onChange={setEmail}
          />
          <TextInput
            type="password"
            name="password"
            placeholder="Senha"
            icon={<FontAwesomeIcon icon={faKey} />}
            value={password}
            onChange={setPassword}
          />
          <Field
            type="password"
            name="confirm-password"
            placeholder="Confirme a senha"
            icon={<FontAwesomeIcon icon={faFloppyDisk} />}
            value={confirmPassword}
            onChange={setConfirmPassword}
          />
          <div className="register-form-footer">
            <Checkbox
              name="terms-of-use"
              label="Eu aceito os termos de uso"
              // TODO: refatorar a forma de capturar o valor durante a mudança
              // para que o evento possa também ser capturado se for necessário
              onChange={setUserTermsAccepted}
            />
            <Submit
              type="submit"
              value="Registrar-se"
              disabled={
                !(isPasswordValid && isPasswordConfirmed && areUseTermsAccepted)
              }
            />
            <span className="login-screen-link">
              Já tem uma conta? <Link to="/login">Entre</Link>
            </span>
          </div>
        </Form>
        {hasError && <span className="error-message">{errorMessage}</span>}
      </div>
    </div>
  );
};

export default Register;
