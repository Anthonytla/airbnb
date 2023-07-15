import useDeleteReview from "../hooks/useDeleteReview";
import GenericDeleteModal from "./GenericDeleteModal";

interface Props {
  modalIsShown: boolean,
  modalRef: React.RefObject<HTMLLabelElement>,
  reviewId: number
}

const DeleteReviewModal = ({ modalIsShown = false, modalRef, reviewId }: Props) => {
  const deleteMutation = useDeleteReview(reviewId);

  return (
    <GenericDeleteModal
      show={modalIsShown}
      handleConfirm={deleteMutation.mutate}
      ref={modalRef}
      loading={deleteMutation.isLoading}
      message="Etes vous certain de vouloir supprimmer ce commentaire ?"
    />
  );
}
 
export default DeleteReviewModal;