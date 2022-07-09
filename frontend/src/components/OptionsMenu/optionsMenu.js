import { faFilter } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import "./optionsMenu.css";

const OptionsMenu = ({
  filterOptions,
  filterButton,
  sideButtons,
  className,
}) => {
  return (
    <div className={`options-menu ${className}`.trim()}>
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
