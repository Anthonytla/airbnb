import User from "../types/User";
import EditUserForm from "./EditUserForm";

interface Props {
  modalIsShown: boolean,
  modalRef: React.RefObject<HTMLLabelElement>,
  user: User,
  setShow: (show: boolean) => void
}

const EditUserModal = ({ modalIsShown = false, modalRef, user, setShow }: Props) => {
  return (
    <>
      <input
        type="checkbox"
        checked={modalIsShown}
        readOnly
        id="my-modal-4"
        className="modal-toggle"
      />
      <label htmlFor="my-modal-4" className="modal cursor-pointer">
        <label ref={modalRef} className="modal-box relative">
          <EditUserForm user={user} setShowModal={setShow} />
        </label>
      </label>
    </>
  );
}

export default EditUserModal