/* eslint-disable no-empty */
import Button from "components/Button/button";
import GlobalMenu from "components/GlobalMenu/globalMenu";
import FilterOption from "components/OptionsMenu/FilterOption/filterOption";
import OptionsMenu from "components/OptionsMenu/optionsMenu";
import Table from "components/Table/table";
import TableHeader from "components/Table/TableHeader/tableHeader";
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

  const [paidFilter, setPaidFilter] = useState(-1);
  const [dateFilter, setDateFilter] = useState("");
  const [paymentMethodFilter, setPaymentMethodFilter] = useState(0);
  const [numberOfParcelsFilter, setNumberOfParcelsFilter] = useState("");

  const [sortBy, setSortBy] = useState("none");

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
      const { status, data } = await request.get(`/expense/user/${user.id}`);
      if (status === 200) {
        let expensesAux = await Promise.all(
          data.map(async (item) => {
            const category = await getCategory(item.categoryId);

            return {
              ...item,
              categoryName: category.name,
            };
          })
        );
        setExpenses(expensesAux);
      } else if (status === 204) {
        setExpenses([]);
      }
    } catch (error) {}
  };

  const getCategory = async (id) => {
    try {
      const response = await request.get(`/category/id/${id}`);

      if (response.status === 200) {
        return response.data;
      }
    } catch (error) {
      return null;
    }
  };

  useEffect(() => {
    const sortedExpenses = sort(filteredExpenses);
    const rows = sortedExpenses.map((expense) => {
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
          <td>{expense.categoryName}</td>
          <td>{expense.value}</td>
          <td>{paymentMethods[expense.paymentMethod]}</td>
          <td>{expense.numberOfParcels}</td>
          <td>{expense.isPaid ? "Sim" : "Não"}</td>
          <td>{new Date(expense.dueDate * 1000).toLocaleDateString()}</td>
        </tr>
      );
    });

    setTableRows(rows);
  }, [filteredExpenses, selected, sortBy]);

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

  const sort = (input) => {
    if (sortBy === "none") {
      return input;
    } else if (sortBy === "description") {
      return sortByDescription(input);
    } else if (sortBy === "category") {
      return sortByCategory(input);
    } else if (sortBy === "value") {
      return sortByValue(input);
    } else if (sortBy === "payment-method") {
      return sortByPaymentMethod(input);
    } else if (sortBy === "number-of-parcels") {
      return sortByNumberOfParcels(input);
    } else if (sortBy === "is-paid") {
      return sortByPaid(input);
    } else if (sortBy === "due-date") {
      return sortByDueDate(input);
    }
  };

  const sortByDescription = (input) => {
    let data = input;

    data.sort((expenseA, expenseB) => {
      if (expenseA.description > expenseB.description) {
        return 1;
      }

      return 0;
    });

    return data;
  };
  const sortByCategory = (input) => {
    let data = input;

    data.sort((expenseA, expenseB) => {
      if (
        expenseA.categoryName.toLowerCase() >
        expenseB.categoryName.toLowerCase()
      ) {
        return 1;
      }

      return 0;
    });

    return data;
  };

  const sortByValue = (input) => {
    let data = input;

    data.sort((expenseA, expenseB) => {
      return expenseA.value - expenseB.value;
    });

    return data;
  };

  const sortByPaymentMethod = (input) => {
    let data = input;

    data.sort((expenseA, expenseB) => {
      return expenseA.paymentMethod - expenseB.paymentMethod;
    });

    return data;
  };

  const sortByNumberOfParcels = (input) => {
    let data = input;

    data.sort((expenseA, expenseB) => {
      return expenseA.numberOfParcels - expenseB.numberOfParcels;
    });

    return data;
  };

  const sortByPaid = (input) => {
    let data = input;

    data.sort((expenseA, expenseB) => {
      if (expenseA.isPaid && expenseB.isPaid) {
        return 0;
      } else if (expenseA.isPaid && !expenseB.isPaid) {
        return 1;
      } else {
        return -1;
      }
    });

    return data;
  };

  const sortByDueDate = (input) => {
    let data = input;

    data.sort((expenseA, expenseB) => {
      return expenseA.dueDate - expenseB.dueDate;
    });

    return data;
  };

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
        return (
          dateFilter === new Date(expense.dueDate * 1000).toLocaleDateString()
        );
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
      <GlobalMenu direction="vertical" className="expense-menu" />
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
          <TableHeader
            itens={[
              {
                label: "Descrição",
                onClick: () => setSortBy("description"),
              },
              {
                label: "Categoria",
                onClick: () => setSortBy("category"),
              },
              {
                label: "Valor (R$)",
                onClick: () => setSortBy("value"),
              },
              {
                label: "Método de pagamento",
                onClick: () => setSortBy("payment-method"),
              },
              {
                label: "Número de parcelas",
                onClick: () => setSortBy("number-of-parcels"),
              },
              {
                label: "Está pago?",
                onClick: () => setSortBy("is-paid"),
              },
              {
                label: "Data de vencimento",
                onClick: () => setSortBy("due-date"),
              },
            ]}
          />
          <tbody>{tableRows}</tbody>
        </Table>
      </div>
    </div>
  );
};

export default Expense;
