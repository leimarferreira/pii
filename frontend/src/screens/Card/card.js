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
import "./card.css";

const Card = () => {
  useTitle("Cartões");

  const [user, setUser] = useState(null);
  const [cards, setCards] = useState([]);
  const [filteredCards, setFilteredCards] = useState([]);
  const [tableRows, setTableRows] = useState([]);
  const [selected, setSelected] = useState([]);

  const [brandFilter, setBrandFilter] = useState("");
  const [numberFilter, setNumberFilter] = useState("");
  const [typeFilter, setTypeFilter] = useState(0);
  const [dueDateFilter, setDueDateFilter] = useState(0);

  const navigate = useNavigate();

  const cardTypes = {
    1: "Crédito",
    2: "Débito",
  };

  const getUser = async () => {
    try {
      const response = await request.get("/user/current");
      setUser(response.data);
    } catch (error) {}
  };

  const getCards = async () => {
    try {
      const response = await request.get(`/card/user/${user.id}`);
      if (response.status === 200) {
        setCards(response.data);
      }
    } catch (error) {}
  };

  useEffect(() => {
    const rows = filteredCards.map((card) => {
      return (
        <tr
          key={card.id}
          onClick={() => {
            if (selected.id === card.id) {
              setSelected({});
            } else {
              setSelected({});
              setSelected({ id: card.id });
            }
          }}
          className={selected.id === card.id ? "selected" : ""}
        >
          <td>{card.number}</td>
          <td>{cardTypes[card.type]}</td>
          <td>{card.brand}</td>
          <td>{card.type === 1 ? card.limit : ""}</td>
          <td>{card.type === 2 ? card.currentValue : ""}</td>
          <td>{card.dueDate}</td>
        </tr>
      );
    });

    setTableRows(rows);
  }, [filteredCards, selected]);

  useEffect(() => {
    setFilteredCards(cards);
  }, [cards]);

  useEffect(() => {
    if (user) {
      getCards();
    }
  }, [user]);

  useEffect(() => {
    getUser();
  }, []);

  const filter = () => {
    let cardsAux = cards;
    if (brandFilter.length !== 0) {
      cardsAux = cardsAux.filter((card) => {
        return card.brand === brandFilter;
      });
    }

    if (numberFilter.length !== 0) {
      cardsAux = cardsAux.filter((card) => {
        return card.number === numberFilter;
      });
    }

    if (typeFilter !== 0) {
      cardsAux = cardsAux.filter((card) => {
        return card.type === typeFilter;
      });
    }

    if (dueDateFilter !== 0) {
      cardsAux = cardsAux.filter((card) => {
        return card.dueDate === dueDateFilter;
      });
    }

    setFilteredCards(cardsAux);
  };

  const deleteSelected = async () => {
    if (selected.id) {
      try {
        await request.delete(`/card/${selected.id}`);
        await getCards();
      } catch {}
    }
  };

  return (
    <div className="card-screen">
      <Menu direction="vertical" className="card-menu">
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
                label="Tipo"
                onChange={(value) => {
                  const numericValue = parseInt(value);
                  setTypeFilter(numericValue);
                }}
              >
                <option value="0">Todos</option>
                <option value="1">Crédito</option>
                <option value="2">Débito</option>
              </FilterOption>
              <FilterOption
                type="text"
                label="Número do cartão"
                onChange={setNumberFilter}
              />
              <FilterOption
                type="text"
                label="Bandeira"
                onChange={setBrandFilter}
              />
              <FilterOption
                type="text"
                label="Dia do fechamento"
                onChange={(value) => {
                  if (value.length !== 0) {
                    const numericValue = parseInt(value);
                    setDueDateFilter(numericValue);
                  } else {
                    setDueDateFilter(0);
                  }
                }}
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
                onClick={() => navigate(`/card/edit/${selected.id}`)}
              />
              <Button
                title="Criar novo cartão"
                className="add-button"
                onClick={() => selected.id && navigate("/card/add")}
              />
              <Button
                title="Deletar"
                className="delete-button"
                onClick={deleteSelected}
              />
            </>
          }
        />
        <Table title="Cartões">
          <thead>
            <tr>
              <th>Número</th>
              <th>Tipo</th>
              <th>Bandeira</th>
              <th>Limite (R$)</th>
              <th>Valor atual (R$)</th>
              <th>Dia do fechamento</th>
            </tr>
          </thead>
          <tbody>{tableRows}</tbody>
        </Table>
      </div>
    </div>
  );
};

export default Card;
