import "./popover.css";

const Popover = ({ children, className }) => {
  return (
    <div className={`${className} popover`}>
      <div className="popover-content">{children}</div>
    </div>
  );
};

export default Popover;
