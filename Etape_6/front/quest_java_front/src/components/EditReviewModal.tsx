import { Review } from "../types/Review";
import EditReviewForm from "./EditReviewForm";

interface Props {
  modalIsShown: boolean,
  modalRef: React.RefObject<HTMLLabelElement>,
  handleClose: (show: boolean) => void,
  review: Review
}

const EditReviewModal = ({ modalIsShown = false, modalRef, handleClose, review }: Props) => {
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
          <EditReviewForm review={review} handleClose={handleClose} />
        </label>
      </label>
    </>
  );
}

export default EditReviewModal;