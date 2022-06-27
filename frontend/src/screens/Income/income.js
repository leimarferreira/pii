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
import "./income.css";

const Income = () => {
  useTitle("Receitas");

  const [user, setUser] = useState(null);
  const [incomes, setIncomes] = useState([]);
  const [filteredIncomes, setFilteredIncomes] = useState([]);
  const [tableRows, setTableRows] = useState([]);
  const [selected, setSelected] = useState([]);
  const [categories, setCategories] = useState({});
  const [dates, setDates] = useState({});

  const [descriptionFilter, setDescriptionFilter] = useState("");
  const [valueFilter, setValueFilter] = useState(0);
  const [dateFilter, setDateFilter] = useState("");
  const [categoryFilter, setCategororyFilter] = useState("");

  const navigate = useNavigate();

  const getUser = async () => {
    try {
      const response = await request.get("/user/current");
      setUser(response.data);
    } catch (error) {}
  };

  const getIncomes = async () => {
    try {
      const response = await request.get(`/income/all/user/${user.id}`);
      if (response.status === 200) {
        setIncomes(response.data);
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
    const ids = new Set(incomes.map((income) => income.categoryId));
    getCategories(ids);

    let datesAux = {};

    incomes.forEach((income) => {
      datesAux[income.id] = new Date(income.date).toLocaleDateString();
    });

    setDates(datesAux);
  }, [incomes]);

  useEffect(() => {
    const rows = filteredIncomes.map((income) => {
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
          <td>{income.value}</td>
          <td>{dates[income.id]}</td>
          <td>{categories[income.categoryId]?.name}</td>
        </tr>
      );
    });

    setTableRows(rows);
  }, [filteredIncomes, selected, categories, dates]);

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
        return dateFilter === dates[income.id];
      });
    }

    if (categoryFilter.length !== 0) {
      incomesAux = incomesAux.filter((income) => {
        return new RegExp(categoryFilter, "gi").test(
          categories[income.categoryId].name
        );
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
      <Menu direction="vertical" className="income-menu">
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
          <thead>
            <tr>
              <th>Descrição</th>
              <th>Valor (R$)</th>
              <th>Data</th>
              <th>Categoria</th>
            </tr>
          </thead>
          <tbody>{tableRows}</tbody>
        </Table>
      </div>
    </div>
  );
};

export default Income;
