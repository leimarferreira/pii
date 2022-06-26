import "./button.css";

const Button = ({ title, onClick, className }) => {
  return (
    <button
      className={`button${className ? " " + className : ""}`}
      onClick={onClick}
    >
      {title}
    </button>
  );
};

export default Button;
