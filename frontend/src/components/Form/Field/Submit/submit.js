/* eslint-disable react/prop-types */
import "./submit.css";

const Submit = ({ value, className }) => {
  return (
    <input
      type="submit"
      value={value}
      className={`${className} form-field form-field-submit`}
    />
  );
};

export default Submit;
