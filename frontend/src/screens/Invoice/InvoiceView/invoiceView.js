/* eslint-disable no-empty */
import Button from "components/Button/button";
import GlobalMenu from "components/GlobalMenu/globalMenu";
import FilterOption from "components/OptionsMenu/FilterOption/filterOption";
import OptionsMenu from "components/OptionsMenu/optionsMenu";
import Table from "components/Table/table";
import TableHeader from "components/Table/TableHeader/tableHeader";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import request from "services/request";
import useTitle from "utils/hooks/useTitle";
import "./invoiceView.css";

const InvoiceView = () => {
  useTitle("Despesas");
  const { invoiceId } = useParams();

  const [parcels, setParcels] = useState([]);
  const [filteredParcels, setFilteredParcels] = useState([]);

  const [tableRows, setTableRows] = useState([]);
  const [selected, setSelected] = useState({});

  const [descriptionFilter, setDescriptionFilter] = useState("");
  const [dateFilter, setDateFilter] = useState("");
  const [valueFilter, setValueFilter] = useState("");

  const [sortBy, setSortBy] = useState("none");

  const [errorMessage, setErrorMessage] = useState("");
  const [hasError, setError] = useState(false);

  useEffect(() => {
    const sortedParcels = sort(filteredParcels);
    const rows = sortedParcels.map((parcel) => {
      return (
        <tr
          key={parcel.id}
          onClick={() => {
            if (selected.id === parcel.id) {
              setSelected({});
            } else {
              setSelected({});
              setSelected(parcel);
            }
          }}
          className={selected.id === parcel.id ? "selected" : ""}
        >
          <td>{parcel.description}</td>
          <td>{parcel.parcelNumber}</td>
          <td>{parcel.numberOfParcels}</td>
          <td>{new Date(parcel.dueDate * 1000)?.toLocaleDateString()}</td>
          <td>{parcel.value.toFixed(2)}</td>
        </tr>
      );
    });

    setTableRows(rows);
  }, [filteredParcels, selected, sortBy]);

  useEffect(() => {
    filter();
  }, [parcels]);

  useEffect(() => {
    getInvoices();
  }, []);

  const getInvoices = async () => {
    if (invoiceId) {
      try {
        const response = await request.get(`/parcel/invoice/${invoiceId}`);

        if (response.status === 200) {
          setParcels(response.data);
        } else if (response.status === 204) {
          setParcels([]);
        }
        setError(false);
      } catch (error) {
        setErrorMessage("Erro ao obter as despesas da fatura.");
        setError(true);
      }
    }
  };

  const sort = (input) => {
    if (sortBy === "none") {
      return input;
    } else if (sortBy === "description") {
      return sortByDescription(input);
    } else if (sortBy === "parcel-number") {
      return sortByParcelNumber(input);
    } else if (sortBy === "total-parcels") {
      return sortByTotalParcels(input);
    } else if (sortBy === "due-date") {
      return sortByDueDate(input);
    } else if (sortBy === "value") {
      return sortByValue(input);
    }

    return input;
  };

  const sortByDescription = (input) => {
    let data = input;

    data.sort((parcelA, parcelB) => {
      if (parcelA.description > parcelB.description) {
        return 1;
      }

      return 0;
    });

    return data;
  };

  const sortByParcelNumber = (input) => {
    let data = input;

    data.sort((parcelA, parcelB) => {
      return parcelA.parcelNumber - parcelB.parcelNumber;
    });

    return data;
  };

  const sortByTotalParcels = (input) => {
    let data = input;

    data.sort((parcelA, parcelB) => {
      return parcelA.numberOfParcels - parcelB.numberOfParcels;
    });

    return data;
  };

  const sortByDueDate = (input) => {
    let data = input;

    data.sort((parcelA, parcelB) => {
      return parcelA.dueDate - parcelB.dueDate;
    });

    return data;
  };

  const sortByValue = (input) => {
    let data = input;

    data.sort((parcelA, parcelB) => {
      return parcelA.value - parcelB.value;
    });

    return data;
  };

  const filter = () => {
    let parcelsAux = parcels;

    if (descriptionFilter.length !== 0) {
      let regexp = new RegExp(descriptionFilter, "gi");
      parcelsAux = parcelsAux.filter((parcel) => {
        return regexp.test(parcel.description);
      });
    }

    if (dateFilter.length !== 0) {
      let regexp = new RegExp(`^${dateFilter}`, "g");

      parcelsAux = parcelsAux.filter((parcel) => {
        let dateStr = new Date(parcel.dueDate * 1000).toLocaleDateString();
        return regexp.test(dateStr);
      });
    }

    if (valueFilter.length !== 0) {
      const numberValue = parseFloat(valueFilter);

      if (!isNaN(numberValue)) {
        parcelsAux = parcelsAux.filter((parcel) => {
          return parcel.value === numberValue;
        });
      }
    }

    setFilteredParcels(parcelsAux);
  };

  return (
    <div className="invoice-view-screen">
      <GlobalMenu direction="vertical" className="invoice-view-menu" />

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
                label="Data de vencimento"
                onChange={setDateFilter}
              />
              <FilterOption
                type="text"
                label="Valor"
                onChange={setValueFilter}
              />
            </>
          }
          filterButton={
            <Button className="search-button" title="Buscar" onClick={filter} />
          }
        />
        <Table title="Despesas">
          <TableHeader
            itens={[
              {
                label: "Descrição",
                onClick: () => setSortBy("description"),
              },
              {
                label: "Número da parcela",
                onClick: () => setSortBy("parcel-number"),
              },
              {
                label: "Total de parcelas",
                onClick: () => setSortBy("total-parcels"),
              },
              {
                label: "Data de vencimento",
                onClick: () => setSortBy("due-date"),
              },
              {
                label: "Valor (R$)",
                onClick: () => setSortBy("value"),
              },
            ]}
          />
          <tbody>{tableRows}</tbody>
        </Table>
      </div>
      {hasError && <span className="error-message">{errorMessage}</span>}
    </div>
  );
};

export default InvoiceView;
