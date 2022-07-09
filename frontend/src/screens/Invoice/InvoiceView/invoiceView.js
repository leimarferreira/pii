/* eslint-disable no-empty */
import {
  faCreditCard,
  faGear,
  faMoneyBillTrendUp,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Button from "components/Button/button";
import Menu from "components/Menu/menu";
import MenuItem from "components/Menu/MenuItem/menuItem";
import FilterOption from "components/OptionsMenu/FilterOption/filterOption";
import OptionsMenu from "components/OptionsMenu/optionsMenu";
import Table from "components/Table/table";
import TableHeader from "components/Table/TableHeader/tableHeader";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
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

  const [errorMessage, setErrorMessage] = useState("");
  const [hasError, setError] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    const rows = filteredParcels.map((parcel) => {
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
          <td>{parcel.value}</td>
        </tr>
      );
    });

    setTableRows(rows);
  }, [filteredParcels, selected]);

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
      <Menu direction="vertical" className="invoice-view-menu">
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
              },
              {
                label: "Número da parcela",
              },
              {
                label: "Total de parcelas",
              },
              {
                label: "Data de vencimento",
              },
              {
                label: "Valor",
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
