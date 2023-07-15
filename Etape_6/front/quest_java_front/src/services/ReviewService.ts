import axios from "../api/axios";
import { PostReviewInput, Review } from "../types/Review";

const ReviewService = {
  async findAll(address_id: number): Promise<Review[]> {
      const { data } = await axios.get(`/address/visitor/${address_id}/review`);
      return data;
  },

  async create(address_id: number, formData: PostReviewInput): Promise<Review> {
    const { data } = await axios.post(`/address/${address_id}/review`, formData);
    return data;
  },

  async edit(review_id: number, formData: PostReviewInput): Promise<Review> {
    const { data } = await axios.put(`/review/${review_id}`, formData);
    return data;
  },

  delete(id: number) {
    return axios.delete(`/review/${id}`);
  }
}

export default ReviewService;