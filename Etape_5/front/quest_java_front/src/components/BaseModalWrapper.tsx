import Modal from "./Modal"

const BaseModalWrapper: React.FC<BaseModalWrapperProps> = ({onBackdropClick, isModalVisible}) => {
    if (!isModalVisible) {
        return null;
    }
    return (<Modal onBackdropClick={onBackdropClick} />);
}