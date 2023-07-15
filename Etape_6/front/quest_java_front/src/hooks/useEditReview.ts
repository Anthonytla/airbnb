import toast from "react-hot-toast";
import { useMutation, useQueryClient } from "react-query";
import ReviewService from "../services/ReviewService";
import { PostReviewInput } from "../types/Review";

const useEditReview = (reviewId: number) => {
  const queryClient = useQueryClient();
  const reviewMutation = useMutation((data: PostReviewInput) => ReviewService.edit(reviewId, data), {
    onSuccess: () => {
      toast.success('Votre commentaire a été mis à jour !');
      queryClient.invalidateQueries(['reviews']);
    },
    onError: (err: any) => {
      toast.error(err?.response?.data?.message || err?.message);
    }
  });

  return reviewMutation;
}

export default useEditReview;