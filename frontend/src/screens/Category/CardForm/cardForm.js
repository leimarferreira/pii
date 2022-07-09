import {
  faCreditCard,
  faGear,
  faMoneyBillTrendUp,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Select from "components/Form/Field/Select/select";
import Form, { Submit, TextInput } from "components/Form/form";
import Menu from "components/Menu/menu";
import MenuItem from "components/Menu/MenuItem/menuItem";
import { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import request from "services/request";
import useTitle from "utils/hooks/useTitle";
import "./cardForm.css";

const CardForm = () => {
  const navigate = useNavigate();
  const { pathname } = useLocation();
  let { id } = useParams();

  const [title, setTitle] = useState("");

  const [edit, setEdit] = useState(false);

  const [user, setUser] = useState({});

  const [card, setCard] = useState(null);

  const [hasError, setError] = useState(false);
  const [errorMessage, setErrorMessage] = useState(false);

  const [cardNumber, setCardNumber] = useState("");
  const [cardType, setCardType] = useState(1);
  const [cardBrand, setCardBrand] = useState("");
  const [currentValue, setCurrentValue] = useState(0.0);
  const [limit, setLimit] = useState(0.0);
  const [dueDate, setDueDate] = useState(1);

  useEffect(() => {
    if (edit && card) {
      setCardNumber(card.number);
      setCardType(card.type);
      setCardBrand(card.brand);
      setCurrentValue(card.currentValue);
      setLimit(card.limit);
      setDueDate(card.dueDate);
    }

    if (edit) {
      setTitle("Editar cartão");
    } else {
      setTitle("Adicionar cartão");
    }
  }, [edit, card]);

  useEffect(() => {
    useTitle(title);
  }, [title]);

  useEffect(() => {
    getCard();
  }, [id]);

  useEffect(() => {
    if (/\/card\/edit\/.*/g.test(pathname)) {
      setEdit(true);
    }
  }, []);

  useEffect(() => {
    getUser();
  }, []);

  const getCard = async () => {
    try {
      const { data } = await request.get(`/card/id/${id}`);
      setCard(data);
    } catch {
      setErrorMessage("Ocorreu um erro durante a comunicação.");
      setError(true);
    }
  };

  const getUser = async () => {
    try {
      const { data } = await request.get("/user/current");
      setUser(data);
    } catch {
      setErrorMessage("Ocorreu um erro durante a comunicação.");
      setError(true);
    }
  };

  const sanitizeInt = (value) => {
    return parseInt(value);
  };

  const submitData = async () => {
    const data = {
      userId: user.id,
      number: cardNumber,
      type: cardType,
      brand: cardBrand,
      currentValue: parseFloat(currentValue),
      limit: parseFloat(limit),
      dueDate: dueDate,
    };

    try {
      const response = await (edit
        ? request.put(`/card/${id}`, data)
        : request.post("/card", data));

      if (response.status === 200 || response.status === 201) {
        navigate("/card");
      }
    } catch {
      setErrorMessage("Erro ao salvar cartão.");
      setError(true);
    }
  };

  return (
    <div className="card-form-screen">
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
      <div className="card-form-container">
        <Form
          className="card-form"
          title="Adicionar cartão"
          onSubmit={submitData}
        >
          <TextInput
            label="Número do cartão"
            name="number"
            type="text"
            placeholder="Número do cartão"
            value={cardNumber}
            onChange={setCardNumber}
          />
          <Select
            label="Tipo de cartão"
            onChange={(value) => setCardType(sanitizeInt(value))}
            value={cardType}
          >
            <option value="1">Crédito</option>
            <option value="2">Débito</option>
          </Select>
          <TextInput
            label="Bandeira do cartão"
            name="brand"
            type="text"
            placeholder="Bandeira"
            onChange={setCardBrand}
            value={cardBrand}
          />
          {cardType === 1 && (
            <>
              <TextInput
                label="Valor atual"
                name="current-value"
                type="text"
                placeholder="0.00"
                onChange={setCurrentValue}
                value={currentValue}
              />
              <TextInput
                label="Limite"
                name="limit"
                type="text"
                placeholder="0.00"
                onChange={setLimit}
                value={limit}
              />

              <Select
                label="Dia do fechamento"
                onChange={(value) => setDueDate(sanitizeInt(value))}
                value={dueDate}
              >
                {Array(30)
                  .fill(0)
                  .map((_, index) => {
                    return (
                      <option key={index} value={index + 1}>
                        {index + 1}
                      </option>
                    );
                  })}
              </Select>
            </>
          )}
          <Submit value="Salvar" />
          {hasError && <span className="error-message">{errorMessage}</span>}
        </Form>
      </div>
    </div>
  );
};

export default CardForm;
