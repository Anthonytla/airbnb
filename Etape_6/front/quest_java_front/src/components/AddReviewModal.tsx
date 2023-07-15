import AddReviewForm from "./AddReviewForm";

interface Props {
  modalIsShown: boolean,
  modalRef: React.RefObject<HTMLLabelElement>,
  handleClose: (show: boolean) => void
}

const AddReviewModal = ({ modalIsShown = false, modalRef, handleClose }: Props) => {
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
          <AddReviewForm handleClose={handleClose} />
        </label>
      </label>
    </>
  );
}

export default AddReviewModal;