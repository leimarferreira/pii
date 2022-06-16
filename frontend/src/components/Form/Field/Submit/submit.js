/* eslint-disable react/prop-types */
import "./submit.css";

const Submit = ({ value }) => {
  return (
    <input
      type="submit"
      value={value}
      className="form-field form-field-submit"
    />
  );
};

export default Submit;
