import { useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleUser } from "@fortawesome/free-solid-svg-icons";
import "./header.css";
import { useEffect, useState } from "react";
import request from "services/request";

const Header = () => {
  const navigate = useNavigate();
  const [user, setUser] = useState({});

  useEffect(() => {
    request
      .get("/user/current")
      .then((response) => setUser(response.data))
      .catch(() => {});
  }, []);

  return (
    <div className="header">
      <div className="site-logo" onClick={() => navigate("/")}>
        <span className="sitename-abbr">CG</span>
        <span className="sitename">Controle de Gastos</span>
      </div>
      {/* <div className="search-box">
        <TextInput
          className="search-input"
          type="search"
          placeholder="Buscar"
          icon={<FontAwesomeIcon icon={faMagnifyingGlass} />}
        />
      </div> */}
      <div className="user-link" onClick={() => navigate("/user")}>
        {user.avatar ? (
          <div className="user-avatar">
            <img src={user.avatar} />
          </div>
        ) : (
          <span className="user-icon">
            <FontAwesomeIcon icon={faCircleUser} />
          </span>
        )}

        <p className="user-name">{user?.name ?? "Nome de usuário"}</p>
      </div>
    </div>
  );
};

export default Header;
