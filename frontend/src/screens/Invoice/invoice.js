/* eslint-disable no-empty */
import Button from "components/Button/button";
import GlobalMenu from "components/GlobalMenu/globalMenu";
import FilterOption from "components/OptionsMenu/FilterOption/filterOption";
import OptionsMenu from "components/OptionsMenu/optionsMenu";
import Table from "components/Table/table";
import TableHeader from "components/Table/TableHeader/tableHeader";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import request from "services/request";
import useTitle from "utils/hooks/useTitle";
import "./invoice.css";

const Invoice = () => {
  useTitle("Faturas");
  const { id } = useParams();

  const [invoices, setInvoices] = useState([]);
  const [filteredInvoices, setFilteredInvoices] = useState([]);
  const [tableRows, setTableRows] = useState([]);
  const [selected, setSelected] = useState({});

  const [sortBy, setSortBy] = useState("none");

  const [monthFilter, setMonthFilter] = useState("");
  const [valueFilter, setValueFilter] = useState("");

  const [errorMessage, setErrorMessage] = useState("");
  const [hasError, setError] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    const sortedRows = sort(filteredInvoices);
    const rows = sortedRows.map((invoice) => {
      return (
        <tr
          key={invoice.id}
          onClick={() => {
            if (selected.id === invoice.id) {
              setSelected({});
            } else {
              setSelected({});
              setSelected(invoice);
            }
          }}
          className={selected.id === invoice.id ? "selected" : ""}
        >
          <td>{invoice.month}</td>
          <td>{invoice.value.toFixed(2)}</td>
        </tr>
      );
    });

    setTableRows(rows);
  }, [filteredInvoices, selected, sortBy]);

  useEffect(() => {
    filter();
  }, [invoices]);

  useEffect(() => {
    getInvoices();
  }, []);

  const getInvoices = async () => {
    if (id) {
      try {
        const response = await request.get(`/invoice/card/${id}`);

        if (response.status === 200) {
          setInvoices(response.data);
        } else if (response.status === 204) {
          setInvoices([]);
        }
        setError(false);
      } catch (error) {
        setErrorMessage("Erro ao obter as faturas do cartão.");
        setError(true);
      }
    }
  };

  const sort = (input) => {
    if (sortBy === "none") {
      return input;
    } else if (sortBy === "month") {
      return sortByMonth(input);
    } else if (sortBy === "value") {
      return sortByValue(input);
    }

    return input;
  };

  const sortByMonth = (input) => {
    let data = input;

    data.sort((invoiceA, invoiceB) => {
      const [monthA, yearA] = invoiceA.month.split("/");
      const [monthB, yearB] = invoiceB.month.split("/");

      const yearOrder = parseInt(yearA) - parseInt(yearB);

      if (yearOrder > 0 || yearOrder < 0) {
        return yearOrder;
      }

      return parseInt(monthA) - parseInt(monthB);
    });

    return data;
  };

  const sortByValue = (input) => {
    let data = input;

    data.sort((invoiceA, invoiceB) => {
      return invoiceA.value - invoiceB.value;
    });

    return data;
  };

  const filter = () => {
    let invoicesAux = invoices;

    if (monthFilter.length !== 0) {
      const regexp = new RegExp(`^${monthFilter}`, "g");

      invoicesAux = invoicesAux.filter((item) => {
        return regexp.test(item.month);
      });
    }

    if (valueFilter.length !== 0) {
      const value = parseFloat(valueFilter);

      if (!isNaN(value)) {
        invoicesAux = invoicesAux.filter((item) => {
          return item.value === value;
        });
      }
    }

    setFilteredInvoices(invoicesAux);
  };

  return (
    <div className="invoice-screen">
      <GlobalMenu direction="vertical" className="invoice-menu" />

      <div className="main-content">
        <OptionsMenu
          filterOptions={
            <>
              <FilterOption type="text" label="Mês" onChange={setMonthFilter} />
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
          sideButtons={
            <>
              <Button
                title="Ver despesas"
                className="expense-button"
                onClick={() =>
                  selected.id && navigate(`/card/${id}/invoice/${selected.id}`)
                }
              />
            </>
          }
        />
        <Table title="Faturas">
          <TableHeader
            itens={[
              {
                label: "Mês",
                onClick: () => setSortBy("month"),
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

export default Invoice;
