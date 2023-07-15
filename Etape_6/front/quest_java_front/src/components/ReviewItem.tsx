import { Review } from "../types/Review";
import { CgProfile } from "react-icons/cg";
import dayjs from "dayjs";
import "dayjs/locale/fr";
import useAuthUserCache from "../hooks/useAuthUserCache";
import { BsPencilSquare } from "react-icons/bs";
import { RiDeleteBin5Line } from "react-icons/ri";
import { BiStar } from "react-icons/bi";

import DeleteReviewModal from "./DeleteReviewModal";
import { useRef, useState } from "react";
import useOnClickOutside from "../hooks/useOnClickOutside";
import EditReviewModal from "./EditReviewModal";

interface Props {
  review: Review;
}

dayjs.locale("fr");

const ReviewItem = ({ review }: Props) => {
  const date = dayjs(review.updatedDate ?? review.creationDate).format(
    "MMMM YYYY"
  );

  const authUser = useAuthUserCache();
  const canEdit =
    authUser?.username === review.user.username ||
    authUser?.role === "ROLE_ADMIN";

  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const deleteModalRef = useRef<HTMLLabelElement>(null);
  const editModalRef = useRef<HTMLLabelElement>(null);
  useOnClickOutside(deleteModalRef, () => setShowDeleteModal(false));
  useOnClickOutside(editModalRef, () => setShowEditModal(false));

  return (
    <div className="p-3">
      <div className="mb-2 flex items-center justify-between">
        <div className="flex items-center">
          <CgProfile size={40} className="mr-2" />
          <div>
            <div className="font-bold">{review.user.username}</div>
            <div className="text-sm text-gray-400">{date}</div>
          </div>
        </div>
        <div className="flex-col">
          <div className="flex items-center justify-end">
            <BiStar size={25} /> {review.note.toFixed(1)}
          </div>

          {canEdit && (
            <div className="mt-2">
              <span>
                <BsPencilSquare
                  size={25}
                  className="inline-block cursor-pointer hover:text-purple-700"
                  onClick={() => setShowEditModal(true)}
                />
                <RiDeleteBin5Line
                  size={25}
                  className="inline-block ml-2 cursor-pointer hover:text-purple-700"
                  onClick={() => setShowDeleteModal(true)}
                />
              </span>
            </div>
          )}
        </div>
      </div>
      <div className="whitespace-pre-wrap max-w-[460px] break-words">{review.commentaire}</div>
      <DeleteReviewModal
        modalIsShown={showDeleteModal}
        modalRef={deleteModalRef}
        reviewId={review.id}
      />
      <EditReviewModal
        modalIsShown={showEditModal}
        modalRef={editModalRef}
        review={review}
        handleClose={setShowEditModal}
      />
    </div>
  );
};

export default ReviewItem;
