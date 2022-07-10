/* eslint-disable no-empty */
import GlobalMenu from "components/GlobalMenu/globalMenu";
import Table from "components/Table/table";
import { useEffect, useState } from "react";
import request from "services/request";
import useTitle from "utils/hooks/useTitle";
import "./home.css";

const Home = () => {
  useTitle("Início");

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
          <td>{userFinancialData.totalIncomes.toFixed(2)}</td>
          <td>{userFinancialData.creditExpenses.toFixed(2)}</td>
          <td>{userFinancialData.debitExpenses.toFixed(2)}</td>
          <td>{userFinancialData.moneyExpenses.toFixed(2)}</td>
          <td>{userFinancialData.totalExpenses}</td>
          <td>{userFinancialData.totalCredit.toFixed(2)}</td>
          <td>{userFinancialData.balance.toFixed(2)}</td>
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
      <GlobalMenu direction="horizontal" className="home-menu" />
      <Table title="Dados do usuário" className="home-table">
        <thead>
          <tr>
            <th>Total de receitas (R$)</th>
            <th>Despesas - crédito (R$)</th>
            <th>Despesas - débito (R$)</th>
            <th>Despesas - dinheiro (R$)</th>
            <th>Total de despesas (R$)</th>
            <th>Crédito total (R$)</th>
            <th>Saldo (R$)</th>
          </tr>
        </thead>
        <tbody>{tableRows}</tbody>
      </Table>
    </div>
  );
};

export default Home;
