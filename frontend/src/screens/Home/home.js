/* eslint-disable no-empty */
import {
  faCreditCard,
  faGear,
  faList,
  faMoneyBillTrendUp,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Menu from "components/Menu/menu";
import MenuItem from "components/Menu/MenuItem/menuItem";
import Table from "components/Table/table";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import request from "services/request";
import useTitle from "utils/hooks/useTitle";
import "./home.css";

const Home = () => {
  useTitle("Início");
  const navigate = useNavigate();

  const [user, setUser] = useState(null);
  const [userFinancialData, setUserFinancialData] = useState(null);
  const [tableRows, setTableRows] = useState([]);

  useEffect(() => {
    getUser();
  }, []);

  useEffect(() => {
    if (user) {
      getUserData();
    }
  }, [user]);

  useEffect(() => {
    if (userFinancialData) {
      let tableRowsAux = (
        <tr>
          <td>{userFinancialData.totalIncomes}</td>
          <td>{userFinancialData.totalExpenses}</td>
          <td>{userFinancialData.totalCards}</td>
          <td>{userFinancialData.balance}</td>
        </tr>
      );

      setTableRows(tableRowsAux);
    }
  }, [userFinancialData]);

  const getUser = async () => {
    try {
      const response = await request.get("/user/current");

      if (response.status === 200) {
        setUser(response.data);
      }
    } catch {}
  };

  const getUserData = async () => {
    try {
      const response = await request.get(`/user/data/${user.id}`);

      if (response.status === 200) {
        setUserFinancialData(response.data);
      }
    } catch {}
  };

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
          title="Categorias"
          onClick={() => navigate("/category")}
          icon={<FontAwesomeIcon icon={faList} />}
        />
        <MenuItem
          title="Ajuste"
          onClick={() => navigate("/settings")}
          icon={<FontAwesomeIcon icon={faGear} />}
        />
      </Menu>
      <Table title="Dados do usuário" className="home-table">
        <thead>
          <tr>
            <th>Total de receitas (R$)</th>
            <th>Total de despesas (R$)</th>
            <th>Total de cartões (crédito + débito)</th>
            <th>Saldo</th>
          </tr>
        </thead>
        <tbody>{tableRows}</tbody>
      </Table>
    </div>
  );
};

export default Home;
