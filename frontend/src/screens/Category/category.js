/* eslint-disable no-empty */
import {
  faCreditCard,
  faGear,
  faMoneyBillTrendUp,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Button from "components/Button/button";
import { TextInput } from "components/Form/form";
import Menu from "components/Menu/menu";
import MenuItem from "components/Menu/MenuItem/menuItem";
import Modal from "components/Modal/modal";
import FilterOption from "components/OptionsMenu/FilterOption/filterOption";
import OptionsMenu from "components/OptionsMenu/optionsMenu";
import Table from "components/Table/table";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import request from "services/request";
import useTitle from "utils/hooks/useTitle";
import "./category.css";

const Category = () => {
  useTitle("Categorias");

  const [categories, setCategories] = useState([]);
  const [filteredCategories, setFilteredCategories] = useState([]);
  const [nameFilter, setNameFilter] = useState([]);
  const [selected, setSelected] = useState({});
  const [tableRows, setTableRows] = useState([]);

  const [categoryName, setCategoryName] = useState("");
  const [hasError, setError] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [isAddModalVisible, setAddModalVisible] = useState(false);
  const [editMode, setEditMode] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {
    getCategories();
  }, []);

  useEffect(() => {
    filter();
  }, [categories]);

  useEffect(() => {
    const rows = filteredCategories.map((category) => {
      return (
        <tr
          key={category.id}
          onClick={() => {
            if (selected.id === category.id) {
              setSelected({});
            } else {
              setSelected(category);
            }
          }}
          className={selected.id === category.id ? "selected" : ""}
        >
          <td>{category.name}</td>
        </tr>
      );
    });

    setTableRows(rows);
  }, [filteredCategories, selected]);

  const getCategories = async () => {
    try {
      const response = await request.get("/category/all");
      if (response.status === 200) {
        setCategories(response.data);
      } else if (response.status === 204) {
        setCategories([]);
      }
    } catch (error) {}
  };

  const deleteSelected = async () => {
    if (selected.id) {
      try {
        await request.delete(`/category/${selected.id}`);
        await getCategories();
      } catch {}
    }
  };

  const filter = () => {
    let categoriesAux = categories;

    if (nameFilter.length !== 0) {
      categoriesAux = categoriesAux.filter((category) => {
        return new RegExp(`^${nameFilter}`, "gi").test(category.name);
      });
    }

    setFilteredCategories(categoriesAux);
  };

  const submitData = async () => {
    const data = {
      name: categoryName,
    };

    try {
      if (editMode && selected.id) {
        await request.put(`/category/${selected.id}`, data);
      } else {
        await request.post("/category", data);
      }

      setAddModalVisible(false);
      setCategoryName("");
      setError(false);
      await getCategories();
    } catch (error) {
      setErrorMessage("Ocorreu um erro ao salvar a categoria.");
      setError(true);
    }
  };

  const handleAddButtonClick = () => {
    setEditMode(false);
    setAddModalVisible(true);
  };

  const handleEditButtonClick = () => {
    if (selected.id) {
      setEditMode(true);
      setCategoryName(selected.name);
      setAddModalVisible(true);
    }
  };

  const cancelCategoryAdd = () => {
    setCategoryName("");
    setAddModalVisible(false);
    setEditMode(false);
    setError(false);
  };

  return (
    <div className="category-screen">
      <Menu direction="vertical" className="category-menu">
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
            <FilterOption type="text" label="Nome" onChange={setNameFilter} />
          }
          filterButton={
            <Button className="search-button" title="Buscar" onClick={filter} />
          }
          sideButtons={
            <>
              <Button
                title="Atualizar"
                className="update-button"
                onClick={handleEditButtonClick}
              />
              <Button
                title="Criar nova categoria"
                className="add-button"
                onClick={handleAddButtonClick}
              />
              <Button
                title="Deletar"
                className="delete-button"
                onClick={deleteSelected}
              />
            </>
          }
        />
        <Table title="Categorias">
          <thead>
            <tr>
              <th>Nome</th>
            </tr>
          </thead>
          <tbody>{tableRows}</tbody>
        </Table>
        <Modal
          cancelAction={cancelCategoryAdd}
          confirmAction={submitData}
          visible={isAddModalVisible}
        >
          <TextInput
            className="category-name-field"
            placeholder="Nome da categoria"
            label="Nome"
            onChange={setCategoryName}
            value={categoryName}
          />
          {hasError && <span className="error-message">{errorMessage}</span>}
        </Modal>
      </div>
    </div>
  );
};

export default Category;
