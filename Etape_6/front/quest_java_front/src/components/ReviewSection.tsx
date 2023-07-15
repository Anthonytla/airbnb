import useListReview from "../hooks/useListReview";
import AddReviewModal from "./AddReviewModal";
import ReviewItem from "./ReviewItem";
import Spinner from "./Spinner/Spinner";
import { AiOutlinePlusCircle } from "react-icons/ai";
import { useRef, useState } from "react";
import useOnClickOutside from "../hooks/useOnClickOutside";
import useAuthUserCache from "../hooks/useAuthUserCache";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import useAddressDetails from "../hooks/useAddressDetails";

const ReviewSection = () => {
  const { data: reviews, status } = useListReview();

  const [showModal, setShowModal] = useState(false);
  const modalRef = useRef<HTMLLabelElement>(null);
  useOnClickOutside(modalRef, () => setShowModal(false));
  const {data:address} = useAddressDetails()

  const authUser = useAuthUserCache();
  const navigate = useNavigate();
  const location = useLocation();

  const handleAddReview = () => {
    if (!authUser) {
      navigate("/login", { state: { from: location }, replace: true });
    } else {
      setShowModal(true);
    }
  };

  if (status === "loading") {
    return (
      <div className="mt-6">
        <Spinner />
      </div>
    );
  }

  if (status === "error") {
    return <p>Something went wrong...</p>;
  }

  if (reviews) {
    return (
      <div className="mt-5">
        <div className="flex items-center justify-between">
          {!reviews.some((rev) => rev.user.username === authUser?.username) &&
            authUser?.username != address?.user.username && (
              <AiOutlinePlusCircle
                className="cursor-pointer hover:text-purple-700"
                size={40}
                onClick={handleAddReview}
              />
            )}
          <h4 className="font-bold text-xl">
            {reviews?.length ?? 0} commentaires
          </h4>
        </div>
        {reviews?.length > 0 ? (
          <div className="mt-3 flex flex-col gap-4">
            {reviews?.map((review) => (
              <ReviewItem key={review.id} review={review} />
            ))}
          </div>
        ) : (
          <div className="alert shadow-lg">
            <div>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                className="stroke-info flex-shrink-0 w-6 h-6"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                ></path>
              </svg>
              <span>No review yet</span>
            </div>
          </div>
        )}
        <AddReviewModal
          modalIsShown={showModal}
          modalRef={modalRef}
          handleClose={setShowModal}
        />
      </div>
    );
  } else {
    return null;
  }
};

export default ReviewSection;
