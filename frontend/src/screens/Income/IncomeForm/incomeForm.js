/* eslint-disable no-empty */
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

  const [categories, setCategories] = useState([]);
  const [categoryOptions, setCategoryOptions] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("none");
  const [categoryName, setCategoryName] = useState("");

  useEffect(() => {
    if (edit && income) {
      setDescription(income.description);
      setIncomeValue(income.value);
      setDateValue(income.date);
      setDate(new Date(income.date).toLocaleDateString());
      setSelectedCategory(income.categoryId);
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
    getCategories();
  }, []);

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
            placeholder="dd/mm/aaaa"
            onChange={setDate}
            value={date}
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
          <Submit value="Salvar" />
          {hasError && <span className="error-message">{errorMessage}</span>}
        </Form>
      </div>
    </div>
  );
};

export default IncomeForm;
