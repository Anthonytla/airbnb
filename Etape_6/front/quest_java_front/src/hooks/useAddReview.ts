import toast from "react-hot-toast";
import { useMutation, useQueryClient } from "react-query";
import { useParams } from "react-router-dom";
import ReviewService from "../services/ReviewService";
import { PostReviewInput } from "../types/Review";

const useAddReview = () => {
  const addressId = useParams().addressId ?? "";
  const queryClient = useQueryClient();
  const reviewMutation = useMutation((data: PostReviewInput) => ReviewService.create(parseInt(addressId), data), {
    onSuccess: () => {
      toast.success('Review added succesfully');
      queryClient.invalidateQueries(['reviews']);
    },
    onError: (err: any) => {
      toast.error(err?.response?.data?.message || err?.message);
    }
  });

  return reviewMutation;
}

export default useAddReview;