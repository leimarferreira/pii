/* eslint-disable react/prop-types */
import "./table.css";

const Table = ({ title, children, className }) => {
  return (
    <div className={`${className} table`}>
      <table>
        {title && <caption>{title}</caption>}
        {children}
      </table>
    </div>
  );
};

export default Table;
