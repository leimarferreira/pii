/* eslint-disable no-empty */
import {
  faCreditCard,
  faGear,
  faMoneyBillTrendUp,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Form, { Submit, TextInput } from "components/Form/form";
import Menu from "components/Menu/menu";
import MenuItem from "components/Menu/MenuItem/menuItem";
import { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import request from "services/request";
import useTitle from "utils/hooks/useTitle";
import "./incomeForm.css";

const IncomeForm = () => {
  const navigate = useNavigate();
  const { pathname } = useLocation();
  let { id } = useParams();

  const [title, setTitle] = useState("");

  const [edit, setEdit] = useState(false);

  const [user, setUser] = useState({});

  const [income, setIncome] = useState(null);

  const [hasError, setError] = useState(false);
  const [errorMessage, setErrorMessage] = useState(false);

  const [description, setDescription] = useState("");
  const [incomeValue, setIncomeValue] = useState(0);
  const [date, setDate] = useState("");
  const [dateValue, setDateValue] = useState(0);
  const [category, setCategory] = useState("");
  const [categoryId, setCategoryId] = useState(-1);

  useEffect(() => {
    if (edit && income) {
      setDescription(income.description);
      setIncomeValue(income.value);
      setDateValue(income.date);
      setDate(new Date(income.date).toLocaleDateString());
      getCategory();
    }

    if (edit) {
      setTitle("Editar receita");
    } else {
      setTitle("Adicionar receita");
    }
  }, [edit, income]);

  useEffect(() => {
    useTitle(title);
  }, [title]);

  useEffect(() => {
    getCategoryId();
  }, [category]);

  useEffect(() => {
    const [day, month, year] = date.split("/");
    const utcDate = `${year}-${month}-${day}T00:00:00.000`;
    setDateValue(Date.parse(utcDate));
  }, [date]);

  useEffect(() => {
    if (id) {
      getIncome();
    }
  }, [id]);

  useEffect(() => {
    if (/\/income\/edit\/.*/g.test(pathname)) {
      setEdit(true);
    }
  }, [pathname]);

  useEffect(() => {
    getUser();
  }, []);

  const getIncome = async () => {
    try {
      const { data } = await request.get(`/income/id/${id}`);
      setIncome(data);
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
      const response = await request.get(`/category/id/${income.categoryId}`);
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
    if (isNaN(parseFloat(incomeValue))) {
      setErrorMessage("Valor inválido.");
      setError(true);
    } else if (isNaN(dateValue)) {
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
        description: description,
        value: parseFloat(incomeValue),
        date: dateValue,
        categoryId: categoryId,
      };

      try {
        const response = await (edit
          ? request.put(`/income/${id}`, data)
          : request.post("/income", data));

        if (response.status === 200 || response.status === 201) {
          navigate("/income");
        }
      } catch {
        setErrorMessage("Erro ao salvar receita.");
        setError(true);
      }
    }
  };

  return (
    <div className="income-form-screen">
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
      <div className="income-form-container">
        <Form className="income-form" title={title} onSubmit={submitData}>
          <TextInput
            label="Descrição"
            name="description"
            type="text"
            placeholder="Descrição da receita"
            value={description}
            onChange={setDescription}
          />
          <TextInput
            label="Valor"
            name="value"
            type="text"
            placeholder="0.00"
            onChange={setIncomeValue}
            value={incomeValue}
          />
          <TextInput
            label="Data"
            name="date"
            type="text"
            placeholder="mm/dd/aaaa"
            onChange={setDate}
            value={date}
          />
          <TextInput
            label="Categoria"
            name="category"
            type="text"
            placeholder="Salário, venda, retorno de investimento..."
            onChange={setCategory}
            value={category}
          />
          <Submit value="Salvar" />
          {hasError && <span className="error-message">{errorMessage}</span>}
        </Form>
      </div>
    </div>
  );
};

export default IncomeForm;
