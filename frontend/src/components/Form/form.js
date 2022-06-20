/* eslint-disable react/prop-types */
// TODO: remover todos os eslint-disable-
import Field from "./Field/field";
import Checkbox from "./Field/Checkbox/checkbox";
import Submit from "./Field/Submit/submit";
import TextInput from "./Field/TextInput/textInput";
import "./form.css";

const Form = ({ onSubmit, children, title }) => {
  const submitForm = (event) => {
    event.preventDefault();
    onSubmit();
  };

  return (
    <>
      {title && (
        <div className="form-title">
          <h1>{title}</h1>
          <hr className="title-bottom-line" />
        </div>
      )}
      <form onSubmit={onSubmit ? submitForm : onsubmit} className="form">
        {children}
      </form>
    </>
  );
};

export { Field, TextInput, Submit, Checkbox };

export default Form;
