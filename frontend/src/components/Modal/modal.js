import Button from "components/Button/button";
import "./modal.css";

const Modal = ({ children, confirmAction, cancelAction, visible }) => {
  return (
    visible && (
      <div className="modal">
        <div className="modal-box">
          <div className="modal-content">{children}</div>
          <div className="modal-buttons">
            <Button
              className="modal-confirm-button modal-button"
              onClick={confirmAction}
              title="Confirmar"
            />
            <Button
              className="modal-cancel-button modal-button"
              onClick={cancelAction}
              title="Cancelar"
            />
          </div>
        </div>
      </div>
    )
  );
};

export default Modal;
