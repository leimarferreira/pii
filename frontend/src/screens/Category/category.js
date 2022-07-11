/* eslint-disable no-empty */
import Button from "components/Button/button";
import { TextInput } from "components/Form/form";
import GlobalMenu from "components/GlobalMenu/globalMenu";
import Modal from "components/Modal/modal";
import FilterOption from "components/OptionsMenu/FilterOption/filterOption";
import OptionsMenu from "components/OptionsMenu/optionsMenu";
import Table from "components/Table/table";
import TableHeader from "components/Table/TableHeader/tableHeader";
import { useEffect, useState } from "react";
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
  const [isAddModalVisible, setAddModalVisible] = useState(false);
  const [editMode, setEditMode] = useState(true);

  const [hasError, setError] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [hasModalError, setModalError] = useState(false);
  const [modalErrorMessage, setModalErrorMessage] = useState("");

  const [sortBy, setSortBy] = useState("none");

  useEffect(() => {
    getCategories();
  }, []);

  useEffect(() => {
    filter();
  }, [categories]);

  useEffect(() => {
    const sortedCategories = sort(filteredCategories);
    const rows = sortedCategories.map((category) => {
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
  }, [filteredCategories, selected, sortBy]);

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
      } catch {
        setErrorMessage(
          "Categoria não pode ser deletada, pois já está sendo utilizada."
        );
        setError(true);
      }
    }
  };

  const sort = (input) => {
    if (sortBy === "none") {
      return input;
    } else if (sortBy === "name") {
      return sortByName(input);
    }
  };

  const sortByName = (input) => {
    let data = input;

    data.sort((categoryA, categoryB) => {
      if (categoryA.name > categoryB.name) {
        return 1;
      }

      return 0;
    });

    return data;
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
      setModalError(false);
      await getCategories();
    } catch (error) {
      setModalErrorMessage("Ocorreu um erro ao salvar a categoria.");
      setModalError(true);
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
    setModalError(false);
  };

  return (
    <div className="category-screen">
      <GlobalMenu direction="vertical" className="category-menu" />
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
          <TableHeader
            itens={[
              {
                label: "Nome",
                onClick: () => setSortBy("name"),
              },
            ]}
          />
          <tbody>{tableRows}</tbody>
        </Table>
        {hasError && <span className="error-message">{errorMessage}</span>}
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
          {hasModalError && (
            <span className="error-message">{modalErrorMessage}</span>
          )}
        </Modal>
      </div>
    </div>
  );
};

export default Category;
