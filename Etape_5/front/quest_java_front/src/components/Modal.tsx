import ReactDOM from "react-dom";
import ModalProps from "../types/ModalProps";

const Modal: React.FC<ModalProps> = ({onBackdropClick, children}) => {
    return ReactDOM.createPortal(
        <div onClick={onBackdropClick}>
            <span>
                zefzef
            </span>
        </div>, document.getElementById('modal-root')!);
}

export default Modal