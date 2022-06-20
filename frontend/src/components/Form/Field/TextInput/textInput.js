/* eslint-disable react/prop-types */
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
    <div
      className={`${className} form-field form-field-text-input`}
      onClick={onFieldClick}
    >
      {icon && <span className="form-field-left-icon">{icon}</span>}
      <input
        type={type}
        name={name}
        placeholder={placeholder}
        value={value}
        onChange={onChange ? handleValueChange : () => {}}
        ref={inputRef}
      />
    </div>
  );
};

export default TextInput;
