import Address from "./Address";
import User from "./User";
import { number, z } from 'zod';

export interface Review {
  id: number,
  commentaire: string,
  note: number,
  cleanliness: number,
  services:number,
  qualityPrice:number
  creationDate: string,
  updatedDate: string,
  user: User,
  address: Address
}

// export const postReviewSchema = z.object({
//   commentaire: z.string().min(1),
//   note: z.preprocess(
//     val => parseInt(z.string().parse(val)),
//     z.number().int('Veullez renseigner un entier').positive().lte(5)
//   )
// });

// export type PostReviewInput = z.infer<typeof postReviewSchema>;

export interface EditReviewInput {
  commentaire: string,
  note: string
};

export interface PostReviewInput extends Pick<Review, "commentaire" | "note" | "cleanliness" | "services" |"qualityPrice">{

}