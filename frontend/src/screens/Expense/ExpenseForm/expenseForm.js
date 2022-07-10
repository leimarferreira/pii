/* eslint-disable no-empty */
import Select from "components/Form/Field/Select/select";
import Form, { Checkbox, Submit, TextInput } from "components/Form/form";
import GlobalMenu from "components/GlobalMenu/globalMenu";
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
  const [categories, setCategories] = useState([]);
  const [categoryOptions, setCategoryOptions] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("none");
  const [categoryName, setCategoryName] = useState("");

  useEffect(() => {
    if (edit && expense) {
      setDescription(expense.description);
      setExpenseValue(expense.value);
      setPaymentMethod(expense.paymentMethod);
      setNumberOfParcels(expense.numberOfParcels);
      setIsPaid(expense.isPaid);
      setDueDate(new Date(expense.dueDate * 1000).toLocaleDateString());
      setDueDateValue(expense.date * 1000);
      setSelectedCategory(expense.categoryId);
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
    getCategories();
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
      setCardId(creditCards[0]?.id ?? 0);
    } else if (paymentMethod === 2) {
      cardsAux = debitCards;
      setCardId(debitCards[0]?.id ?? 0);
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

  useEffect(() => {
    const categoryOptionsAux = categories
      .sort((categoryA, categoryB) => {
        return categoryA.name > categoryB.name;
      })
      .map((category) => {
        return (
          <option key={category.id} value={category.id}>
            {category.name}
          </option>
        );
      });

    setCategoryOptions(categoryOptionsAux);
  }, [categories]);

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

  const getCategories = async () => {
    try {
      const response = await request.get("/category/all");
      if (response.status === 200) {
        setCategories(response.data);
      }
    } catch (error) {}
  };

  const getCategoryId = async () => {
    if (selectedCategory === "new") {
      if (categoryName.length === 0) {
        setErrorMessage("Por favor, selecione uma categoria.");
        setError(true);
        return -1;
      }

      try {
        const response = await request.post("/category", {
          name: categoryName,
        });

        if (response.status === 201) {
          return response.data.id;
        }
      } catch (error) {
        if (error.response.status === 409) {
          setErrorMessage("Já exite uma categoria com o mesmo nome.");
          setError(true);
        } else {
          setErrorMessage("Erro ao salvar categoria.");
          setError(true);
        }
        return -1;
      }
    } else if (selectedCategory === "none") {
      setErrorMessage("Por favor, selecione uma categoria.");
      setError(true);
      return -1;
    } else {
      return selectedCategory;
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
    validateData();

    if (hasError) {
      return;
    }

    const categoryId = await getCategoryId();
    if (categoryId === -1) {
      return;
    }

    const data = {
      userId: user.id,
      value: parseFloat(expenseValue),
      description: description,
      categoryId: categoryId,
      paymentMethod: paymentMethod,
      numberOfParcels: numberOfParcels,
      cardId: cardId !== 0 ? cardId : null,
      isPaid: isPaid,
      dueDate: dueDateValue / 1000,
    };

    try {
      const response = await (edit
        ? request.put(`/expense/${id}`, data)
        : request.post("/expense/", data));

      if (response.status === 200 || response.status === 201) {
        navigate("/expense");
      }
    } catch (error) {
      setErrorMessage(
        error?.response?.data?.message ?? "Erro ao salvar despesa."
      );
      setError(true);
    }
  };

  return (
    <div className="expense-form-screen">
      <GlobalMenu direction="vertical" className="expense-menu" />
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
          <div className="inline-fields">
            <Select
              label="Categoria"
              onChange={setSelectedCategory}
              value={selectedCategory}
            >
              {selectedCategory === "none" && (
                <option value="none" selected></option>
              )}
              <option value="new">Nova categoria</option>
              <optgroup label="Categorias existentes">
                {categoryOptions}
              </optgroup>
            </Select>
            <TextInput
              label="Nome da categoria"
              name="category"
              type="text"
              placeholder="Nome da categoria"
              onChange={setCategoryName}
              value={categoryName}
              disabled={selectedCategory !== "new"}
            />
          </div>
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
            placeholder="dd/mm/aaaa"
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
