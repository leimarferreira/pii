/* eslint-disable no-empty */
import {
  faCreditCard,
  faGear,
  faMoneyBillTrendUp,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Button from "components/Button/button";
import Menu from "components/Menu/menu";
import MenuItem from "components/Menu/MenuItem/menuItem";
import FilterOption from "components/OptionsMenu/FilterOption/filterOption";
import OptionsMenu from "components/OptionsMenu/optionsMenu";
import Table from "components/Table/table";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import request from "services/request";
import useTitle from "utils/hooks/useTitle";
import "./expense.css";

const Expense = () => {
  useTitle("Despesas");

  const [user, setUser] = useState(null);
  const [expenses, setExpenses] = useState([]);
  const [filteredExpenses, setFilteredExpenses] = useState([]);
  const [tableRows, setTableRows] = useState([]);
  const [selected, setSelected] = useState([]);
  const [categories, setCategories] = useState({});
  const [dates, setDates] = useState({});

  const [paidFilter, setPaidFilter] = useState(-1);
  const [dateFilter, setDateFilter] = useState("");
  const [paymentMethodFilter, setPaymentMethodFilter] = useState(0);
  const [numberOfParcelsFilter, setNumberOfParcelsFilter] = useState("");

  const paymentMethods = {
    1: "Crédito",
    2: "Débito",
    3: "Dinheiro",
  };

  const navigate = useNavigate();

  const getUser = async () => {
    try {
      const response = await request.get("/user/current");
      setUser(response.data);
    } catch (error) {}
  };

  const getExpenses = async () => {
    try {
      const response = await request.get(`/expense/user/${user.id}`);
      if (response.status === 200) {
        setExpenses(response.data);
      }
    } catch (error) {}
  };

  const getCategories = async (ids) => {
    let categoriesAux = {};

    ids.forEach(async (id) => {
      try {
        const response = await request.get(`/category/id/${id}`);
        if (response.status === 200) {
          categoriesAux[id] = response.data;
          setCategories(categoriesAux);
        }
      } catch (error) {}
    });
  };

  useEffect(() => {
    const ids = new Set(expenses.map((expense) => expense.categoryId));
    getCategories(ids);

    let datesAux = {};

    expenses.forEach((expense) => {
      datesAux[expense.id] = new Date(
        expense.dueDate * 1000
      ).toLocaleDateString();
    });

    setDates(datesAux);
  }, [expenses]);

  useEffect(() => {
    const rows = filteredExpenses.map((expense) => {
      return (
        <tr
          key={expense.id}
          onClick={() => {
            if (selected.id === expense.id) {
              setSelected({});
            } else {
              setSelected({});
              setSelected({ id: expense.id });
            }
          }}
          className={selected.id === expense.id ? "selected" : ""}
        >
          <td>{expense.description}</td>
          <td>{categories[expense.categoryId]?.name}</td>
          <td>{expense.value}</td>
          <td>{paymentMethods[expense.paymentMethod]}</td>
          <td>{expense.numberOfParcels}</td>
          <td>{expense.isPaid ? "Sim" : "Não"}</td>
          <td>{dates[expense.id]}</td>
        </tr>
      );
    });

    setTableRows(rows);
  }, [filteredExpenses, selected, categories, dates]);

  useEffect(() => {
    filter();
  }, [expenses]);

  useEffect(() => {
    if (user) {
      getExpenses();
    }
  }, [user]);

  useEffect(() => {
    getUser();
  }, []);

  const filter = () => {
    let expensesAux = expenses;

    if (paidFilter !== -1) {
      const value = paidFilter === 1 ? true : false;
      expensesAux = expensesAux.filter((expense) => {
        return expense.isPaid === value;
      });
    }

    if (dateFilter.length !== 0) {
      expensesAux = expensesAux.filter((expense) => {
        return dateFilter === dates[expense.id];
      });
    }

    if (paymentMethodFilter !== 0) {
      expensesAux = expensesAux.filter((expense) => {
        return expense.paymentMethod === paymentMethodFilter;
      });
    }

    if (numberOfParcelsFilter.length !== 0) {
      expensesAux = expensesAux.filter((expense) => {
        return expense.numberOfParcels === parseInt(numberOfParcelsFilter);
      });
    }

    setFilteredExpenses(expensesAux);
  };

  const deleteSelected = async () => {
    if (selected.id) {
      try {
        await request.delete(`/expense/${selected.id}`);
        await getExpenses();
      } catch {}
    }
  };

  return (
    <div className="expense-screen">
      <Menu direction="vertical" className="expense-menu">
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

      <div className="main-content">
        <OptionsMenu
          filterOptions={
            <>
              <FilterOption
                type="select"
                label="Quitado"
                onChange={(value) => setPaidFilter(parseInt(value))}
              >
                <option value="-1">Todos</option>
                <option value="1">Quitado</option>
                <option value="0">Não quitado</option>
              </FilterOption>
              <FilterOption
                type="select"
                label="Meio de pagamento"
                onChange={(value) => setPaymentMethodFilter(parseInt(value))}
              >
                <option value="0">Todos</option>
                <option value="1">Crédito</option>
                <option value="2">Débito</option>
                <option value="3">Dinheiro</option>
              </FilterOption>
              <FilterOption type="text" label="Data" onChange={setDateFilter} />
              <FilterOption
                type="text"
                label="Número de parcelas"
                onChange={setNumberOfParcelsFilter}
              />
            </>
          }
          filterButton={
            <Button className="search-button" title="Buscar" onClick={filter} />
          }
          sideButtons={
            <>
              <Button
                title="Atualizar"
                className="update-button"
                onClick={() =>
                  selected.id && navigate(`/expense/edit/${selected.id}`)
                }
              />
              <Button
                title="Adicionar despesa"
                className="add-button"
                onClick={() => navigate("/expense/add")}
              />
              <Button
                title="Deletar"
                className="delete-button"
                onClick={deleteSelected}
              />
            </>
          }
        />
        <Table title="Despesas">
          <thead>
            <tr>
              <th>Descrição</th>
              <th>Categoria</th>
              <th>Valor (R$)</th>
              <th>Método de pagamento</th>
              <th>Número de parcelas</th>
              <th>Está pago?</th>
              <th>Data de vencimento</th>
            </tr>
          </thead>
          <tbody>{tableRows}</tbody>
        </Table>
      </div>
    </div>
  );
};

export default Expense;
