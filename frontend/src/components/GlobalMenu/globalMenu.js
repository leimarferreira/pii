import {
  faCreditCard,
  faGear,
  faList,
  faMoneyBillTrendUp,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Menu from "components/Menu/menu";
import MenuItem from "components/Menu/MenuItem/menuItem";
import { useLocation, useNavigate } from "react-router-dom";

const GlobalMenu = ({ direction, className, appendItems }) => {
  const location = useLocation();
  const navigate = useNavigate();

  return (
    <>
      <Menu className={className} direction={direction}>
        {location.pathname !== "/card" && (
          <MenuItem
            title="Cartão"
            onClick={() => navigate("/card")}
            icon={<FontAwesomeIcon icon={faCreditCard} />}
          />
        )}
        {location.pathname !== "/income" && (
          <MenuItem
            title="Receita"
            onClick={() => navigate("/income")}
            icon={<FontAwesomeIcon icon={faMoneyBillTrendUp} />}
          />
        )}
        {location.pathname !== "/expense" && (
          <MenuItem
            title="Despesa"
            onClick={() => navigate("/expense")}
            icon={
              <span className="icon-expense">
                <FontAwesomeIcon icon={faMoneyBillTrendUp} />
              </span>
            }
          />
        )}
        {location.pathname !== "/category" && (
          <MenuItem
            title="Categorias"
            onClick={() => navigate("/category")}
            icon={<FontAwesomeIcon icon={faList} />}
          />
        )}
        {location.pathname !== "/settings" && (
          <MenuItem
            title="Ajuste"
            onClick={() => navigate("/settings")}
            icon={<FontAwesomeIcon icon={faGear} />}
          />
        )}
        {appendItems}
      </Menu>
    </>
  );
};

export default GlobalMenu;
