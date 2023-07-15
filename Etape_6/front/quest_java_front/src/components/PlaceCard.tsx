import { BiStar } from "react-icons/bi";
import { Link } from "react-router-dom";
import useAuthUserCache from "../hooks/useAuthUserCache";
import Address from "../types/Address";

type Props = { address: Address };

export const PlaceCard = ({ address }: Props) => {
  var url = `/address/${address.id}`;
  return (
    <Link to={url}>
      <div>
        <div className="flex justify-center w-full h-[300px]">
          <img
            src={
              address?.imageData
                ? "data:image/png;base64," + address.imageData
                : ""
            }
            className="object-fill"
          ></img>
        </div>

        <h4 className="font-bold">
          {address.city}, {address.country}
        </h4>
        <div className="flex justify-between">
          <div className="text-gray-400 pb-2">{address.name}</div>
          <div className="flex items-center">
            <BiStar size={25} /> {address.reviewsNb > 0 ? address.note.toFixed(1) : <span className="font-bold">New !</span>}
          </div>
        </div>

        <div>
          <span className="font-bold">{address.price} €</span> par nuit
        </div>
      </div>
    </Link>
  );
};
