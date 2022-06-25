import { useNavigate } from "react-router-dom";
import { TextInput } from "components/Form/form";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faMagnifyingGlass,
  faCircleUser,
} from "@fortawesome/free-solid-svg-icons";
import "./header.css";

const Header = () => {
  const navigate = useNavigate();

  return (
    <div className="header">
      <div className="site-logo" onClick={() => navigate("/")}>
        <span className="sitename-abbr">CG</span>
        <span className="sitename">Controle de Gastos</span>
      </div>
      <div className="search-box">
        <TextInput
          className="search-input"
          type="search"
          placeholder="Buscar"
          icon={<FontAwesomeIcon icon={faMagnifyingGlass} />}
        />
      </div>
      <div className="user-link" onClick={() => navigate("/user")}>
        <span className="user-icon">
          <FontAwesomeIcon icon={faCircleUser} />
        </span>
        <p className="user-name">Nome de usuário</p>
      </div>
    </div>
  );
};

export default Header;
