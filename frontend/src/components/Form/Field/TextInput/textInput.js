import { useRef } from "react";
import "./textInput.css";

const TextInput = ({
  name,
  placeholder,
  type,
  icon,
  value,
  onChange,
  className,
  label,
  min,
}) => {
  const inputRef = useRef(null);

  const onFieldClick = () => {
    inputRef?.current?.focus();
  };

  const handleValueChange = (event) => {
    const value = event.target.value;
    onChange(value);
  };

  return (
    <div className="form-field-text-input-container">
      {label && (
        <label className="form-field-text-input-label" htmlFor={name}>
          {label}
        </label>
      )}
      <div
        className={`${className} form-field form-field-text-input`}
        onClick={onFieldClick}
      >
        {icon && <span className="form-field-left-icon">{icon}</span>}
        <input
          min={min}
          type={type}
          name={name}
          placeholder={placeholder}
          value={value}
          onChange={onChange ? handleValueChange : () => {}}
          ref={inputRef}
        />
      </div>
    </div>
  );
};

export default TextInput;
