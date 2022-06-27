import { faCircleExclamation } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { validate } from "components/Form/helpers/validator";
import Popover from "components/Popover/popover";
import { useRef, useState } from "react";
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
  validation,
  onError,
}) => {
  const [hasError, setError] = useState(false);
  const [errorMessages, setErrorMessages] = useState([]);
  const inputRef = useRef(null);

  const onFieldClick = () => {
    inputRef?.current?.focus();
  };

  const handleValueChange = (event) => {
    const value = event.target.value;
    onChange(value);
    validateInput(value);
  };

  const validateInput = (input) => {
    if (validation) {
      const result = validate(input, validation);
      setError(result.hasError);
      setErrorMessages(result.errorMessages);
      if (result.hasError && onError) {
        onError();
      }
    }
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
        {hasError && (
          <span className="form-field-right-icon error-icon">
            <FontAwesomeIcon icon={faCircleExclamation} />
          </span>
        )}
        <Popover className="error-message-popover">
          {errorMessages.map((message) => {
            return (
              <span key={message} className="error-message">
                {message}
              </span>
            );
          })}
        </Popover>
      </div>
    </div>
  );
};

export default TextInput;
