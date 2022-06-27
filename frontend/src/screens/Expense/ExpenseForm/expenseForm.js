/* eslint-disable no-empty */
import {
  faCreditCard,
  faGear,
  faMoneyBillTrendUp,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Select from "components/Form/Field/Select/select";
import Form, { Checkbox, Submit, TextInput } from "components/Form/form";
import Menu from "components/Menu/menu";
import MenuItem from "components/Menu/MenuItem/menuItem";
import { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import request from "services/request";
import useTitle from "utils/hooks/useTitle";
import "./expenseForm.css";

const ExpenseForm = () => {
  const navigate = useNavigate();
  const { pathname } = useLocation();
  let { id } = useParams();

  const [title, setTitle] = useState("");

  const [edit, setEdit] = useState(false);

  const [user, setUser] = useState({});

  const [creditCards, setCreditCards] = useState([]);
  const [debitCards, setDebitCards] = useState([]);
  const [allCards, setAllCards] = useState([]);
  const [cardList, setCardList] = useState([]);

  const [expense, setExpense] = useState(null);

  const [hasError, setError] = useState(false);
  const [errorMessage, setErrorMessage] = useState(false);

  const [description, setDescription] = useState("");
  const [expenseValue, setExpenseValue] = useState(0);
  const [paymentMethod, setPaymentMethod] = useState(1);
  const [numberOfParcels, setNumberOfParcels] = useState(1);
  const [isPaid, setIsPaid] = useState(false);
  const [dueDate, setDueDate] = useState("");
  const [cardId, setCardId] = useState(0);

  const [dueDateValue, setDueDateValue] = useState(0);
  const [category, setCategory] = useState("");
  const [categoryId, setCategoryId] = useState(-1);

  useEffect(() => {
    if (edit && expense) {
      setDescription(expense.description);
      setExpenseValue(expense.value);
      setPaymentMethod(expense.paymentMethod);
      setNumberOfParcels(expense.numberOfParcels);
      setIsPaid(expense.isPaid);
      setDueDate(new Date(expense.date).toLocaleDateString());
      setDueDateValue(expense.date);

      getCategory();
    }

    if (edit) {
      setTitle("Editar despesa");
    } else {
      setTitle("Adicionar despesa");
    }
  }, [edit, expense]);

  useEffect(() => {
    useTitle(title);
  }, [title]);

  useEffect(() => {
    getCategoryId();
  }, [category]);

  useEffect(() => {
    const [day, month, year] = dueDate.split("/");
    const utcDate = `${year}-${month}-${day}T00:00:00.000`;
    setDueDateValue(Date.parse(utcDate));
  }, [dueDate]);

  useEffect(() => {
    if (id) {
      getExpense();
    }
  }, [id]);

  useEffect(() => {
    if (/\/expense\/edit\/.*/g.test(pathname)) {
      setEdit(true);
    }
  }, [pathname]);

  useEffect(() => {
    getUser();
  }, []);

  useEffect(() => {
    if (user) {
      getCards();
    }
  }, [user]);

  useEffect(() => {
    if (allCards && allCards.length !== 0) {
      setCreditCards(allCards.filter((card) => card.type === 1));
      setDebitCards(allCards.filter((card) => card.type === 2));
    }
  }, [allCards]);

  useEffect(() => {
    let cardsAux = [];
    if (paymentMethod === 1) {
      cardsAux = creditCards;
    } else if (paymentMethod === 2) {
      cardsAux = debitCards;
    } else if (paymentMethod === 3) {
      setCardId(0);
    }

    let cardListAux = cardsAux.map((card) => {
      return (
        <option value={card.id} key={card.id}>
          {`${card.brand}, ${
            card.type === 1
              ? `limite ${card.limit}`
              : `valor atual ${card.currentValue}`
          }`}
        </option>
      );
    });
    setCardList(cardListAux);
  }, [paymentMethod, creditCards, allCards]);

  const getCards = async () => {
    try {
      const { data } = await request.get(`/card/user/${user.id}`);
      setAllCards(data);
    } catch {}
  };

  const getExpense = async () => {
    try {
      const { data } = await request.get(`/expense/${id}`);
      setExpense(data);
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

  const getCategory = async () => {
    try {
      const response = await request.get(`/category/id/${expense.categoryId}`);
      setCategory(response.data.name);
    } catch {}
  };

  const getCategoryId = async () => {
    try {
      const response = await request.get(`/category/name/${category}`);

      if (response.status === 200) {
        setCategoryId(response.data.id);
      }
    } catch (error) {
      if (error.response.status === 404) {
        try {
          const response = await request.post("/category", { name: category });

          if (response.status === 201) {
            setCategoryId(response.data.id);
          }
        } catch {}
      }
    }
  };

  const validateData = () => {
    if (isNaN(parseFloat(expenseValue))) {
      setErrorMessage("Valor inválido.");
      setError(true);
    } else if (isNaN(dueDateValue)) {
      setErrorMessage("Data inválida.");
      setError(true);
    } else {
      setError(false);
    }
  };

  const submitData = async () => {
    await getCategoryId();
    validateData();

    if (!hasError && categoryId != -1) {
      const data = {
        userId: user.id,
        value: parseFloat(expenseValue),
        description: description,
        categoryId: categoryId,
        paymentMethod: paymentMethod,
        numberOfParcels: numberOfParcels,
        cardId: cardId !== 0 ? cardId : null,
        isPaid: isPaid,
        dueDate: dueDateValue,
      };

      try {
        const response = await (edit
          ? request.put(`/expense/${id}`, data)
          : request.post("/expense/", data));

        if (response.status === 200 || response.status === 201) {
          navigate("/expense");
        }
      } catch {
        setErrorMessage("Erro ao salvar cartão.");
        setError(true);
      }
    }
  };

  return (
    <div className="expense-form-screen">
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
      <div className="expense-form-container">
        <Form className="expense-form" title={title} onSubmit={submitData}>
          <TextInput
            label="Descrição"
            name="description"
            type="text"
            placeholder="Descrição da despesa"
            value={description}
            onChange={setDescription}
          />
          <TextInput
            label="Categoria"
            name="category"
            type="text"
            placeholder="Casa, carro, alimentação..."
            onChange={setCategory}
            value={category}
          />
          <TextInput
            label="Valor"
            name="value"
            type="text"
            placeholder="0.00"
            onChange={setExpenseValue}
            value={expenseValue}
          />
          <Select
            label="Método de pagamento"
            onChange={(value) => setPaymentMethod(parseInt(value))}
            value={paymentMethod}
          >
            <option value="1">Crédito</option>
            <option value="2">Débito</option>
            <option value="3">Dinheiro</option>
          </Select>
          {paymentMethod === 1 && (
            <TextInput
              label="Número de parcelas"
              name="number-of-parcels"
              type="number"
              min="1"
              onChange={(value) => setNumberOfParcels(parseInt(value))}
              value={numberOfParcels}
            />
          )}
          {paymentMethod !== 3 && (
            <Select
              label="Cartão"
              value={cardId}
              onChange={(value) => setCardId(parseInt(value))}
            >
              {cardList}
            </Select>
          )}
          <TextInput
            label="Data de vencimento"
            name="date"
            type="text"
            placeholder="mm/dd/aaaa"
            onChange={setDueDate}
            value={dueDate}
          />
          <Checkbox
            label="Está pago"
            onChange={setIsPaid}
            value={isPaid}
            name="is-paid"
          />
          <Submit value="Salvar" />
          {hasError && <span className="error-message">{errorMessage}</span>}
        </Form>
      </div>
    </div>
  );
};

export default ExpenseForm;
