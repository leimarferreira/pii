import {
  faCreditCard,
  faGear,
  faMoneyBillTrendUp,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Menu from "components/Menu/menu";
import MenuItem from "components/Menu/MenuItem/menuItem";
import Table from "components/Table/table";
import { useNavigate } from "react-router-dom";
import useTitle from "utils/hooks/useTitle";
import "./home.css";

const Home = () => {
  useTitle("Início");
  const navigate = useNavigate();

  return (
    <div className="home-screen">
      <Menu className="home-menu">
        <MenuItem
          title="Cartão"
          onClick={() => navigate("/card")}
          icon={<FontAwesomeIcon icon={faCreditCard} />}
        />
        <MenuItem
          title="Receita"
          onClick={() => navigate("/income")}
          icon={<FontAwesomeIcon icon={faMoneyBillTrendUp} />}
        />
        <MenuItem
          title="Despesa"
          onClick={() => navigate("/expense")}
          icon={
            <span className="icon-expense">
              <FontAwesomeIcon icon={faMoneyBillTrendUp} />
            </span>
          }
        />
        <MenuItem
          title="Ajuste"
          onClick={() => navigate("/settings")}
          icon={<FontAwesomeIcon icon={faGear} />}
        />
      </Menu>
      <Table title="Receita do mês" className="home-table">
        <tr>
          <th>Data</th>
          <th>Valor</th>
          <th>Descrição</th>
          <th>Categoria</th>
          <th>Meio de pagamento</th>
          <th>Número de parcelas</th>
        </tr>
      </Table>
    </div>
  );
};

export default Home;
