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
import "./income.css";

const Income = () => {
  useTitle("Receitas");

  const [user, setUser] = useState(null);
  const [incomes, setIncomes] = useState([]);
  const [filteredIncomes, setFilteredIncomes] = useState([]);
  const [tableRows, setTableRows] = useState([]);
  const [selected, setSelected] = useState([]);

  const [descriptionFilter, setDescriptionFilter] = useState("");
  const [valueFilter, setValueFilter] = useState(0);
  const [dateFilter, setDateFilter] = useState("");
  const [categoryFilter, setCategororyFilter] = useState("");

  const [sortBy, setSortBy] = useState("none");

  const navigate = useNavigate();

  const getUser = async () => {
    try {
      const response = await request.get("/user/current");
      setUser(response.data);
    } catch (error) {}
  };

  const getIncomes = async () => {
    try {
      const { status, data } = await request.get(`/income/all/user/${user.id}`);
      if (status === 200) {
        let incomesAux = await Promise.all(
          data.map(async (item) => {
            const category = await getCategory(item.categoryId);

            return {
              ...item,
              categoryName: category.name,
            };
          })
        );

        setIncomes(incomesAux);
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
    const sortedIncomes = sort(filteredIncomes);
    const rows = sortedIncomes.map((income) => {
      return (
        <tr
          key={income.id}
          onClick={() => {
            if (selected.id === income.id) {
              setSelected({});
            } else {
              setSelected({});
              setSelected({ id: income.id });
            }
          }}
          className={selected.id === income.id ? "selected" : ""}
        >
          <td>{income.description}</td>
          <td>{income.value.toFixed(2)}</td>
          <td>{new Date(income.date).toLocaleDateString()}</td>
          <td>{income?.categoryName}</td>
        </tr>
      );
    });

    setTableRows(rows);
  }, [filteredIncomes, selected, sortBy]);

  useEffect(() => {
    filter();
  }, [incomes]);

  useEffect(() => {
    if (user) {
      getIncomes();
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
    } else if (sortBy === "value") {
      return sortByValue(input);
    } else if (sortBy === "date") {
      return sortByDate(input);
    } else if (sortBy === "category") {
      return sortByCategory(input);
    }
  };

  const sortByDescription = (input) => {
    let data = input;

    data.sort((incomeA, incomeB) => {
      if (incomeA.description > incomeB.description) {
        return 1;
      }

      return 0;
    });

    return data;
  };

  const sortByValue = (input) => {
    let data = input;

    data.sort((incomeA, incomeB) => {
      return incomeA.value - incomeB.value;
    });

    return data;
  };

  const sortByDate = (input) => {
    let data = input;

    data.sort((incomeA, incomeB) => {
      return incomeA.date - incomeB.date;
    });

    return data;
  };

  const sortByCategory = (input) => {
    let data = input;

    data.sort((incomeA, incomeB) => {
      if (
        incomeA.categoryName.toLowerCase() > incomeB.categoryName.toLowerCase()
      ) {
        return 1;
      }

      return 0;
    });

    return data;
  };

  const filter = () => {
    let incomesAux = incomes;

    if (descriptionFilter.length !== 0) {
      incomesAux = incomesAux.filter((income) => {
        return new RegExp(descriptionFilter, "gi").test(income.description);
      });
    }

    if (valueFilter !== 0) {
      incomesAux = incomesAux.filter((income) => {
        return income.value === valueFilter;
      });
    }

    if (dateFilter.length !== 0) {
      incomesAux = incomesAux.filter((income) => {
        return dateFilter === new Date(income.date).toLocaleDateString();
      });
    }

    if (categoryFilter.length !== 0) {
      incomesAux = incomesAux.filter((income) => {
        return new RegExp(categoryFilter, "gi").test(income.categoryName);
      });
    }

    setFilteredIncomes(incomesAux);
  };

  const deleteSelected = async () => {
    if (selected.id) {
      try {
        await request.delete(`/income/${selected.id}`);
        await getIncomes();
      } catch {}
    }
  };

  return (
    <div className="income-screen">
      <GlobalMenu direction="vertical" className="income-menu" />

      <div className="main-content">
        <OptionsMenu
          filterOptions={
            <>
              <FilterOption
                type="text"
                label="Descrição"
                onChange={setDescriptionFilter}
              />
              <FilterOption
                type="text"
                label="Valor"
                onChange={(value) => {
                  setValueFilter(parseFloat(value));
                }}
              />
              <FilterOption type="text" label="Data" onChange={setDateFilter} />
              <FilterOption
                type="text"
                label="Categoria"
                onChange={setCategororyFilter}
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
                  selected.id && navigate(`/income/edit/${selected.id}`)
                }
              />
              <Button
                title="Adicionar receita"
                className="add-button"
                onClick={() => navigate("/income/add")}
              />
              <Button
                title="Deletar"
                className="delete-button"
                onClick={deleteSelected}
              />
            </>
          }
        />
        <Table title="Receitas">
          <TableHeader
            itens={[
              {
                label: "Descrição",
                onClick: () => setSortBy("description"),
              },
              {
                label: "Valor (R$)",
                onClick: () => setSortBy("value"),
              },
              {
                label: "Data",
                onClick: () => setSortBy("date"),
              },
              {
                label: "Categoria",
                onClick: () => setSortBy("category"),
              },
            ]}
          />
          <tbody>{tableRows}</tbody>
        </Table>
      </div>
    </div>
  );
};

export default Income;
