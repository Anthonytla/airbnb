import { Link, useLocation } from "react-router-dom";
import { AddressForm } from "../components/AddressForm";
import { ReservationForm } from "../components/ReservationForm";
import ReviewSection from "../components/ReviewSection";
import Spinner from "../components/Spinner/Spinner";
import { UserLoginForm } from "../components/UserLoginForm/UserLoginForm";
import useAddressDetails from "../hooks/useAddressDetails";
import useAuthUser from "../hooks/useAuthUser";
import useAuthUserCache from "../hooks/useAuthUserCache";
import Reservation from "../types/Reservation";

const AddressDetailsPage = () => {
  const { data: address, status } = useAddressDetails();
  const reservation = useLocation().state as { reservation: Reservation };
  const { data: authUser } = useAuthUser(false);

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

  if (address) {
    const { user, name, city, country, imageData, description } = address;
    console.log(address);
    return (
      <div className="px-10 flex justify-between">
        <div className="basis-1/4 grow-0">
          <h1 className="font-[1000] text-3xl">{name}</h1>
          <div className="underline font-bold">
            {city}, {country}
          </div>
          <img
            className="pt-[15px] pb-[15px]"
            src={imageData ? "data:image/png;base64," + imageData : ""}
          ></img>
          <div className="whitespace-pre-line">{description}</div>
          <div className="font-bold mt-5">Contact the host</div>
          <Link to={`/address/${address.id}/chat`} className="btn w-48 mb-4 mt-5">
            {authUser?.username!=address.user.username ? <span>Chat with {address.user.username}</span> : <span>Chat</span>}
          </Link>
          <ReviewSection />
          
        </div>

        <div className="flex justify-center grow">
          <div className="">
            {authUser ? (
              user.username != authUser.username ? (
                <div className="mt-16 sticky top-28">
                  <ReservationForm
                    reservation={reservation}
                    address={address}
                  />
                </div>
              ) : (
                <div className="">
                  <AddressForm
                    address={address}
                    setModalIsShown={function (isShown: boolean): void {}}
                  />
                </div>
              )
            ) : (
              <UserLoginForm redirect={false} />
            )}
          </div>
        </div>
      </div>
    );
  } else {
    return null;
  }
};

export default AddressDetailsPage;
