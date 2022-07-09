import { useEffect, useState } from "react";
import "./tableHeader.css";

const TableHeader = ({ itens }) => {
  const [content, setContent] = useState([]);
  const [selected, setSelected] = useState(-1);

  const getContent = () => {
    if (itens && itens instanceof Array) {
      const contentAux = itens.map((item, index) => {
        return (
          <th
            key={index}
            className={`${item.onClick ? "th-clickable" : ""} ${
              selected === index ? "th-selected" : ""
            }`.trim()}
            onClick={() => {
              if (item.onClick) {
                setSelected(index);
                item.onClick();
              }
            }}
          >
            {item.label}
          </th>
        );
      });

      setContent(contentAux);
    }
  };

  useEffect(() => {
    getContent();
  }, [selected]);

  return (
    <thead>
      <tr>{content}</tr>
    </thead>
  );
};

export default TableHeader;
