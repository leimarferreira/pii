import {
  faArrowRightFromBracket,
  faTrashCan,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import FilePicker from "components/Form/Field/FilePicker/filePicker";
import Form, { Submit, TextInput } from "components/Form/form";
import GlobalMenu from "components/GlobalMenu/globalMenu";
import MenuItem from "components/Menu/MenuItem/menuItem";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import authService from "services/authService";
import request from "services/request";
import useTitle from "utils/hooks/useTitle";
import "./userSettings.css";

const UserSettings = () => {
  const navigate = useNavigate();

  useTitle("Configurações do usuário");

  const [user, setUser] = useState(null);

  const [hasError, setError] = useState(false);
  const [errorMessage, setErrorMessage] = useState(false);

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [currentPassword, setCurrentPassword] = useState("");
  const [avatar, setAvatar] = useState("");
  const [passwordValid, setPasswordValid] = useState(false);

  useEffect(() => {
    getUser();
  }, []);

  useEffect(() => {
    if (user) {
      setName(user.name);
      setEmail(user.email);
      setAvatar(user.avatar);
    }
  }, [user]);

  const getUser = async () => {
    try {
      const { data } = await request.get("/user/current");
      setUser(data);
    } catch {
      setErrorMessage("Ocorreu um erro durante a comunicação.");
      setError(true);
    }
  };

  useEffect(() => {
    validatePassword();
  }, [password]);

  const validatePassword = () => {
    setPasswordValid(
      password.length >= 8 && // deve ter no mínimo 8 caracteres
        /[A-Z]/g.test(password) && // deve ter pelo menos uma letra maiúscula
        /[a-z]/g.test(password) && // deve ter pelo menos uma letra minúscula
        /\d/g.test(password) && // deve ter pelo menos um dígito
        /\D|_/g.test(password) // deve ter pelo menos um caractere especial
    );
  };

  const logout = () => {
    authService.logout();
    navigate("/login");
  };

  const deleteAccount = async () => {
    try {
      const response = await request.delete(`/user/${user.id}`);

      if (response.status === 200) {
        authService.logout();
        navigate("/login");
      }
      // eslint-disable-next-line no-empty
    } catch {}
  };

  const submitData = async () => {
    if (password.length !== 0 && !passwordValid) {
      setErrorMessage("Nova senha inválida.");
      setError(true);
      return;
    }

    if (currentPassword.length === 0) {
      setErrorMessage("Por favor, digite a senha atual.");
      setError(true);
      return;
    }

    const currentCredentials = {
      email: user.email,
      password: currentPassword,
    };

    const credentialsValid = await authService.validateCredentials(
      currentCredentials
    );

    if (!credentialsValid) {
      setErrorMessage("Senha atual inválida.");
      setError(true);
      return;
    }

    const data = {
      name: name,
      email: email,
      avatar: avatar,
    };

    try {
      await request.put(`/user/${user.id}`, data);

      const credentials = {
        email: email,
        password: currentPassword,
      };

      await authService.login(credentials);
    } catch {
      setErrorMessage("Erro ao salvar usuário.");
      setError(true);
      return;
    }

    if (password.length === 0) {
      return;
    }

    const passwordData = {
      password: password,
      id: user.id,
    };

    try {
      await authService.updatePassword(passwordData);
    } catch {
      setErrorMessage("Erro ao atualizar a senha do usuário.");
      setError(true);
    }
  };

  return (
    <div className="user-form-screen">
      <GlobalMenu
        direction="vertical"
        className="user-menu"
        appendItems={
          <>
            <MenuItem
              className="important-action"
              title="Apagar conta"
              onClick={() => deleteAccount()}
              icon={
                <span className="important-icon">
                  <FontAwesomeIcon icon={faTrashCan} />
                </span>
              }
            />
            <MenuItem
              className="important-action"
              title="Sair da conta"
              onClick={() => logout()}
              icon={
                <span className="important-icon">
                  <FontAwesomeIcon icon={faArrowRightFromBracket} />
                </span>
              }
            />
          </>
        }
      />
      <div className="user-form-container">
        <Form
          className="user-form"
          title={"Dados do usuário"}
          onSubmit={submitData}
        >
          <TextInput
            label="Nome"
            name="name"
            type="text"
            placeholder="Nome completo"
            value={name}
            onChange={setName}
          />
          <TextInput
            label="Email"
            name="email"
            type="email"
            placeholder="usuario@email.com"
            onChange={setEmail}
            value={email}
          />
          <FilePicker label="Avatar" name="avatar" onChange={setAvatar} />
          <TextInput
            label="Senha atual"
            name="current-password"
            type="password"
            placeholder="Senha"
            onChange={setCurrentPassword}
            value={currentPassword}
          />
          <TextInput
            label="Nova senha"
            name="new-password"
            type="password"
            placeholder="Senha"
            onChange={setPassword}
            value={password}
          />
          <Submit value="Salvar" />
          {hasError && <span className="error-message">{errorMessage}</span>}
        </Form>
      </div>
    </div>
  );
};

export default UserSettings;
