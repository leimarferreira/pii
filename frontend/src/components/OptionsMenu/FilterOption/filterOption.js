import { Field } from "components/Form/form";
import "./filterOption.css";

const FilterOption = ({ type, label, onChange, children }) => {
  return (
    <div className="filter-option">
      <span className="filter-option-label">{label}</span>
      {type === "select" ? (
        <Field type={type} onChange={onChange}>
          {children}
        </Field>
      ) : (
        <Field type={type} onChange={onChange} />
      )}
    </div>
  );
};

export default FilterOption;
