import React from "react";
import useDeleteUser from "../hooks/useDeleteUser";
import Spinner from "./Spinner/Spinner";

interface Props {
  modalIsShown: boolean,
  modalRef: React.RefObject<HTMLLabelElement>,
  username: string
}

const DeleteUserModal = ({ modalIsShown = false, modalRef, username }: Props) => {
  const deleteMutation = useDeleteUser(username);

  const confirmDelete = () => {
    deleteMutation.mutate();
  }

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
          <p className="text-center mb-3">
            Are you sure you want to delete this account ?
          </p>
          <div className="flex justify-center">
            <button className={`btn ${deleteMutation.isLoading && 'bg-slate-400'}`} onClick={confirmDelete}>
              {deleteMutation.isLoading ? (
                <Spinner white={true} size="sm" />
              ) : (
                "Confirm"
              )}
            </button>
          </div>
        </label>
      </label>
    </>
  );
}

export default DeleteUserModal;