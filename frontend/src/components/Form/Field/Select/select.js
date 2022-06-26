import "./select.css";

const Select = ({ children, icon, label, onChange, value }) => {
  const handleValueChange = (event) => {
    const value = event.target.value;
    onChange(value);
  };

  return (
    <div className="form-field-select-container">
      {label && <label className="form-field-select-label">{label}</label>}
      <div className="form-field form-field-select">
        {icon && <span className="form-field-left-icon">{icon}</span>}
        <select
          onChange={onChange ? handleValueChange : () => {}}
          value={value}
        >
          {children}
        </select>
      </div>
    </div>
  );
};

export default Select;
