import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  EditReviewInput,
  PostReviewInput,
  Review,
} from "../types/Review";
import Spinner from "./Spinner/Spinner";
import useEditReview from "../hooks/useEditReview";
import Rating from "./Rating";
import { useEffect, useState } from "react";

interface Props {
  handleClose: (show: boolean) => void;
  review: Review;
}

const EditReviewForm = ({ handleClose, review }: Props) => {
  const { commentaire, note, cleanliness, services, qualityPrice } = review;
  
  const mutation = useEditReview(review.id);
  const {
    register,
    handleSubmit,
    formState: { errors },
    setValue
  } = useForm<PostReviewInput>({
    //resolver: zodResolver(postReviewSchema),
    defaultValues: { commentaire, note , cleanliness, services, qualityPrice},
    
  });

  
  const [cleanlinessRating, setCleanlinessRating] = useState(cleanliness);
  // useEffect(() => {
  //   setCleanlinessRating(cleanliness);
  // }, [cleanliness]);
  const [servicesRating, setServicesRating] = useState(services);
  // useEffect(() => {
  //   setServicesRating(services);
  // }, [services]);
  const [quality_priceRating, setQuality_priceRating] = useState(qualityPrice);
  // useEffect(() => {
  //   setQuality_priceRating(services);
  // }, [qualityPrice]);

  const canSubmit = !mutation.isLoading;

  const onSubmit = (formData: PostReviewInput) => {
    mutation.mutate(
      { ...formData },
      {
        onSuccess: () => {
          handleClose(false);
        },
      }
    );
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="flex flex-col max-w-[500px] min-w-[400px] rounded-lg mt-6"
    >
      <h1 className="font-[1000] text-3xl text-center">
        Modifiez ce commentaire
      </h1>
      <div className="flex flex-col my-2">
        <label className="text-left">Commentaire</label>
        <textarea
        cols={50}
          {...register("commentaire")}
          className="border whitespace-pre-wrap border-gray-700 rounded-md mt-1 px-2"
        />
        <p className="text-red-500">
          {errors?.commentaire && errors.commentaire?.message}
        </p>
      </div>
      <div className="flex items-center my-2">
        <label className="text-left">Services</label>
        <div className="ml-auto">
          <input
            type="number"
            {...register("services")}
            className="border border-gray-700 hidden rounded-md h-8 mt-1 px-2"
          />
          <Rating
            rating={servicesRating}
            onRating={(rate) => {
              setServicesRating(rate);

              setValue("services", rate);
            }}
          />
        </div>
        <p className="text-red-500">{errors?.note && errors.note?.message}</p>
      </div>

      <div className="flex items-center my-2">
        <label className="text-left">Cleanliness</label>
        <div className="ml-auto">
          <input
            type="number"
            {...register("cleanliness")}
            className="border border-gray-700 hidden rounded-md h-8 mt-1 px-2"
          />
          <Rating
            rating={cleanlinessRating}
            onRating={(rate) => {
              setCleanlinessRating(rate);

              setValue("cleanliness", rate);
            }}
          />
        </div>
      </div>

      <div className="flex items-center my-2">
        <label className="text-left">Quality/Price</label>
        <div className="ml-auto">
          <input
            type="number"
            {...register("qualityPrice")}
            className="border border-gray-700 hidden rounded-md h-8 mt-1 px-2"
          />
          <Rating
            rating={quality_priceRating}
            onRating={(rate) => {
              setQuality_priceRating(rate);

              setValue("qualityPrice", rate);
            }}
          />
        </div>
      </div>
      <button
        className={`mx-auto ${
          canSubmit
            ? "bg-blue-500 hover:bg-blue-600 hover:scale-105"
            : "bg-slate-400"
        } rounded max-w-[30%] p-2 text-white my-2 transition ease-in-out delay-100 duration-250`}
        type="submit"
        disabled={!canSubmit}
      >
        {mutation.isLoading ? <Spinner white={true} size="sm" /> : "Confirm"}
      </button>
    </form>
  );
};

export default EditReviewForm;
