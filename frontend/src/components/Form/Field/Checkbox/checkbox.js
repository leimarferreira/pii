import { useRef } from "react";
import "./checkbox.css";

const Checkbox = ({ label, name, onChange }) => {
  const checkboxRef = useRef(null);

  const onCheckBoxClick = () => {
    checkboxRef.current.checked = !checkboxRef.current.checked;
  };

  const onCheck = (event) => {
    const checked = event.target.checked;
    onChange(checked);
  };

  return (
    <div className="form-field form-field-checkbox" onClick={onCheckBoxClick}>
      <input
        id={name}
        name={name}
        type="checkbox"
        ref={checkboxRef}
        onChange={onCheck}
      />
      <span className="checkmark"></span>
      <label htmlFor={name}>{label}</label>
    </div>
  );
};

export default Checkbox;
