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
import "./card.css";

const Card = () => {
  useTitle("Cartões");

  const [user, setUser] = useState(null);
  const [cards, setCards] = useState([]);
  const [userFinancialData, setUserFinancialData] = useState({});
  const [filteredCards, setFilteredCards] = useState([]);
  const [tableRows, setTableRows] = useState([]);
  const [selected, setSelected] = useState([]);

  const [brandFilter, setBrandFilter] = useState("");
  const [numberFilter, setNumberFilter] = useState("");
  const [typeFilter, setTypeFilter] = useState(0);
  const [dueDateFilter, setDueDateFilter] = useState(0);

  const [sortBy, setSortBy] = useState("none");

  const [errorMessage, setErrorMessage] = useState("");
  const [hasError, setError] = useState(false);

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
      } else if (response.status === 204) {
        setCards([]);
      }
    } catch (error) {}
  };

  useEffect(() => {
    const sortedCards = sort(filteredCards);
    const rows = sortedCards.map((card) => {
      return (
        <tr
          key={card.id}
          onClick={() => {
            if (selected.id === card.id) {
              setSelected({});
            } else {
              setSelected({});
              setSelected(card);
            }
          }}
          className={selected.id === card.id ? "selected" : ""}
        >
          <td>{card.number}</td>
          <td>{cardTypes[card.type]}</td>
          <td>{card.brand}</td>
          <td>
            {card.type === 1
              ? card.limit.toFixed(2)
              : userFinancialData.totalIncomes?.toFixed(2)}
          </td>
          <td>{card.type === 1 ? card.currentValue.toFixed(2) : "-"}</td>
          <td>{card.type === 1 ? card.dueDate : "-"}</td>
        </tr>
      );
    });

    setTableRows(rows);
  }, [filteredCards, selected, sortBy]);

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

  useEffect(() => {
    if (user) {
      request
        .get(`/user/data/${user.id}`)
        .then((response) => setUserFinancialData(response.data))
        .catch(() => {});
    }
  }, [user]);

  const sort = (input) => {
    if (sortBy === "none") {
      return input;
    } else if (sortBy === "number") {
      return sortByNumber(input);
    } else if (sortBy === "type") {
      return sortByType(input);
    } else if (sortBy === "brand") {
      return sortByBrand(input);
    } else if (sortBy === "limit") {
      return sortByLimit(input);
    } else if (sortBy === "value") {
      return sortByValue(input);
    } else if (sortBy === "due-date") {
      return sortByDueDay(input);
    }
  };

  const sortByNumber = (input) => {
    let data = input;

    data.sort((cardA, cardB) => {
      if (cardA.number > cardB.number) {
        return 1;
      }

      return 0;
    });

    return data;
  };

  const sortByType = (input) => {
    let data = input;

    data.sort((cardA, cardB) => {
      return cardA.type - cardB.type;
    });

    return data;
  };

  const sortByBrand = (input) => {
    let data = input;

    data.sort((cardA, cardB) => {
      if (cardA.brand > cardB.brand) {
        return 1;
      }

      return 0;
    });

    return data;
  };

  const sortByLimit = (input) => {
    let data = input;

    data.sort((cardA, cardB) => {
      return cardA.limit - cardB.limit;
    });

    return data;
  };

  const sortByValue = (input) => {
    let data = input;

    data.sort((cardA, cardB) => {
      if (cardA.type === 2 && cardB.type === 1) {
        return 1;
      } else if (cardB.type === 2 && cardA.type === 1) {
        return -1;
      }

      return cardA.currentValue - cardB.currentValue;
    });

    return data;
  };

  const sortByDueDay = (input) => {
    let data = input;

    data.sort((cardA, cardB) => {
      if (cardA.type === 2 && cardB.type === 1) {
        return 1;
      } else if (cardB.type === 2 && cardA.type === 1) {
        return -1;
      }

      return cardA.dueDate - cardB.dueDate;
    });

    return data;
  };

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
        setSelected({});
      } catch {
        setErrorMessage(
          "Cartão não pode ser deletado por ser utilizado em alguma despesa."
        );
        setError(true);
      }
    }
  };

  return (
    <div className="card-screen">
      <GlobalMenu direction="vertical" className="card-menu" />

      <div className="main-content">
        <OptionsMenu
          className="card-opt-menu"
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
              {selected.id && selected.type === 1 && (
                <Button
                  title="Faturas"
                  className="invoice-button"
                  onClick={() =>
                    selected.id && navigate(`/card/${selected.id}/invoice`)
                  }
                />
              )}
              <Button
                title="Atualizar"
                className="update-button"
                onClick={() =>
                  selected.id && navigate(`/card/edit/${selected.id}`)
                }
              />
              <Button
                title="Criar novo cartão"
                className="add-button"
                onClick={() => navigate("/card/add")}
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
          <TableHeader
            itens={[
              {
                label: "Número",
                onClick: () => setSortBy("number"),
              },
              {
                label: "Tipo",
                onClick: () => setSortBy("type"),
              },
              {
                label: "Bandeira",
                onClick: () => setSortBy("brand"),
              },
              {
                label: "Limite (R$)",
                onClick: () => setSortBy("limit"),
              },
              {
                label: "Valor atual (R$)",
                onClick: () => setSortBy("value"),
              },
              {
                label: "Dia do fechamento",
                onClick: () => setSortBy("due-date"),
              },
            ]}
          />
          <tbody>{tableRows}</tbody>
        </Table>
        {hasError && <span className="error-message">{errorMessage}</span>}
      </div>
    </div>
  );
};

export default Card;
