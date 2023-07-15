import { AxiosError } from "axios";
import { useQuery } from "react-query";
import { useParams } from "react-router-dom";
import ReviewService from "../services/ReviewService";
import { Review } from "../types/Review";

const useListReview = () => {
  const addressId = useParams().addressId ?? "";
  return useQuery<Review[], AxiosError>(["reviews"], () => ReviewService.findAll(parseInt(addressId)));
};

export default useListReview;