import React, { useMemo, useState } from "react";
import { AiFillStar, AiOutlineClose, AiOutlineStar } from "react-icons/ai";
import { FaFontAwesome } from "react-icons/fa";

type Props = {
  count?: number;
  rating?: number;
  onRating: (idx: number) => void;
};

export const Rating = ({ count = 5, rating = 0, onRating }: Props) => {
  const color = {
    filled: "#ffca00",
    unfilled: "gray",
  };

  const clearRating = () => {
    onRating(0);
  }

  const [hoverRating, setHoverRating] = useState(0);
  const getColor = (index: number) => {

    if (hoverRating >= index) {
      return color.filled;
    } else if (!hoverRating && rating >= index) {
      return color.filled;
    }
    return color.unfilled;
  };
  const stars = useMemo(() => {
    return Array(count)
      .fill(0)
      .map((_, i) => i + 1)
      .map((idx) => (
        <div className="flex-row">
          <AiFillStar
            size={30}
            onClick={(event: any) => {
              rating == 1 && idx == 1 ? setHoverRating(0) : onRating(idx);
            }}
            onMouseEnter={() => setHoverRating(idx)}
            style={{ color: getColor(idx) }}
            onMouseLeave={() => setHoverRating(0)}
            className="cursor-pointer"
          />
        </div>
      ));
  }, [count, rating]);


  return (
    <>
      <div className="flex justify-end items-center">
        {stars.map((elm) => (
          <span>{elm}</span>
        ))}<AiOutlineClose onClick={clearRating}/>
      </div>
    </>
  );
};

export default Rating;
