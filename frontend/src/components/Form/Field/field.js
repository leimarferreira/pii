import Checkbox from "./Checkbox/checkbox";
import "./field.css";
import Submit from "./Submit/submit";
import TextInput from "./TextInput/textInput";

const Field = ({
  name,
  placeholder,
  type,
  icon,
  value,
  onChange,
  disabled,
  label,
}) => {
  switch (type) {
    case "text":
    case "email":
    case "number":
    case "password":
    case "search":
    case "tel":
    case "url":
      return (
        <TextInput
          name={name}
          placeholder={placeholder}
          type={type}
          icon={icon}
          value={value}
          onChange={onChange}
          disabled={disabled}
        />
      );
    case "checkbox":
      return <Checkbox label={label} name={name} onChange={onChange} />;
    case "submit":
      return <Submit value={value} />;
  }
};

export default Field;
