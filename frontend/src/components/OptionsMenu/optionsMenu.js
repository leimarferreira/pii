/* eslint-disable no-unused-vars */
import { faFilter } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Button from "components/Button/button";
import { Submit } from "components/Form/form";
import FilterOption from "./FilterOption/filterOption";
import "./optionsMenu.css";

const OptionsMenu = ({
  list,
  setter,
  filterOptions,
  filterButton,
  sideButtons,
}) => {
  return (
    <div className="options-menu">
      <span className="filter-icon">
        <FontAwesomeIcon icon={faFilter} />
      </span>
      <div className="filter">
        <div className="filter-options">{filterOptions}</div>
        <div className="filter-button">{filterButton}</div>
      </div>
      <div className="side-buttons">{sideButtons}</div>
    </div>
  );
};

export default OptionsMenu;
