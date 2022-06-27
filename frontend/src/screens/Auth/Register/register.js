import { useEffect, useState } from "react";
import Form, { Submit, TextInput } from "components/Form/form";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faUser,
  faEnvelope,
  faKey,
  faFloppyDisk,
} from "@fortawesome/free-solid-svg-icons";
import "./register.css";
import { Link, useNavigate } from "react-router-dom";
import useTitle from "utils/hooks/useTitle";
import authService from "services/authService";

const Register = () => {
  useTitle("Cadastre-se");
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [isPasswordValid, setPasswordValid] = useState(false);
  const [isPasswordConfirmed, setPasswordConfirmed] = useState(false);
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
      await authService.register(data);
      navigate("/");
    } catch (error) {
      if (error?.response?.status === 500) {
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
            validation={{
              required: { value: true, message: "Digite seu nome completo." },
            }}
          />
          <TextInput
            type="text"
            name="email"
            placeholder="Endereço de email"
            icon={<FontAwesomeIcon icon={faEnvelope} />}
            value={email}
            onChange={setEmail}
            validation={{
              required: {
                value: true,
                message: "É necessário digitar um email.",
              },
            }}
          />
          <TextInput
            type="password"
            name="password"
            placeholder="Senha"
            icon={<FontAwesomeIcon icon={faKey} />}
            value={password}
            onChange={setPassword}
            validation={{
              required: {
                value: true,
                message: "É necessário digitar uma senha.",
              },
              patterns: [
                {
                  pattern: "[A-Z]",
                  message: "A senha deve ter pelo menos uma letra maiúscula.",
                },
                {
                  pattern: "[a-z]",
                  message: "A senha deve ter pelo menos uma letra minúscula.",
                },
                {
                  pattern: "\\d",
                  message: "A senha deve ter pelo menos um dígito.",
                },
                {
                  pattern: "\\W|_",
                  message: "A senha deve ter pelo menos um caractere especial.",
                },
                {
                  pattern: "^.{8,}$",
                  message: "A senha deve ter no mínimo 8 caracteres.",
                },
              ],
            }}
          />
          <TextInput
            type="password"
            name="confirm-password"
            placeholder="Confirme a senha"
            icon={<FontAwesomeIcon icon={faFloppyDisk} />}
            value={confirmPassword}
            onChange={setConfirmPassword}
            validation={{
              required: {
                value: true,
                message: "Este campo é obrigatório.",
              },
              rules: [
                {
                  rule: (input) => {
                    if (input === password) {
                      setPasswordConfirmed(true);
                      return true;
                    } else {
                      setPasswordConfirmed(false);
                      return false;
                    }
                  },
                  message: "As senhas não coincidem.",
                },
              ],
            }}
          />
          <div className="register-form-footer">
            {/* <Checkbox
              name="terms-of-use"
              label="Eu aceito os termos de uso"
              onChange={setUserTermsAccepted}
            /> */}
            <Submit
              type="submit"
              value="Registrar-se"
              disabled={!(isPasswordValid && isPasswordConfirmed)}
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
