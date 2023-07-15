import toast from "react-hot-toast";
import { useMutation, useQueryClient } from "react-query"
import ReviewService from "../services/ReviewService";

const useDeleteReview = (reviewId: number) => {
  const queryClient = useQueryClient();

  const reviewDeleteMutation = useMutation(() => ReviewService.delete(reviewId), {
    onSuccess: () => {
      queryClient.invalidateQueries('reviews');
      toast.success('Commentaire supprimé !');
    },
    onError: (err: any) => {
      toast.error(err?.response?.data?.message || err?.message);
    }
  })

  return reviewDeleteMutation;
}

export default useDeleteReview;