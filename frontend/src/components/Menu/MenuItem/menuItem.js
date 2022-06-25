import "./menuItem.css";

const MenuItem = ({ title, icon, onClick, className }) => {
  const handleMenuItemClick = () => {
    onClick();
  };

  return (
    <div
      className={`menu-item ${className}`}
      onClick={onClick ? handleMenuItemClick : () => {}}
    >
      <span className="menu-item-icon">{icon}</span>
      <span className="menu-item-title">{title}</span>
    </div>
  );
};

export default MenuItem;
