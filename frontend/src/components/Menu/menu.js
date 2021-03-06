import "./menu.css";

const Menu = ({ direction, children, className }) => {
  const directionClass =
    direction !== "horizontal" && direction !== "vertical"
      ? "horizontal"
      : direction;

  return (
    <div className={`menu ${directionClass} ${className ?? ""}`.trim()}>
      {children}
    </div>
  );
};

export default Menu;
