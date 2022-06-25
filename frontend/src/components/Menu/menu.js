/* eslint-disable react/prop-types */
import { useEffect } from "react";
import "./menu.css";

const Menu = ({ direction, children, className }) => {
  const directionClass =
    direction !== "horizontal" && direction !== "vertical"
      ? "horizontal"
      : direction;

  useEffect(() => {
    console.log(directionClass);
  }, []);

  return (
    <div className={`menu ${directionClass} ${className ?? ""}`.trim()}>
      {children}
    </div>
  );
};

export default Menu;
