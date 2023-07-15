import React, { FormEvent, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import useAddReservation from "../hooks/useAddReservation";
import useReservationAddressList from "../hooks/useReservationAddressList";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import Reservation from "../types/Reservation";
import useAuthUserCache from "../hooks/useAuthUserCache";
import toast from "react-hot-toast";
import User from "../types/User";
import useUpdateReservation from "../hooks/useUpdateReservation";
import Address from "../types/Address";
import useDeleteReservation from "../hooks/useDeleteReservation";
import { UserLoginForm } from "./UserLoginForm/UserLoginForm";
import Spinner from "./Spinner/Spinner";

type Props = { address: Address; reservation: { reservation: Reservation } };

export const ReservationForm = ({ reservation, address }: Props) => {
  const addressId: string = useParams().addressId || "";
  const { reservationMutation } = useAddReservation(parseInt(addressId));
  const {
    data: reservations,
    isLoading,
    error,
  } = useReservationAddressList(addressId);
  const { reservationUpdate } = useUpdateReservation(
    reservation?.reservation.id
  );
  const { reservationDelete } = useDeleteReservation(
    reservation?.reservation.id
  );

  const authUser = useAuthUserCache();


  const navigate = useNavigate();

  var now = new Date();
  now.setHours(0);
  now.setMinutes(0);
  now.setSeconds(0);
  now.setMilliseconds(0);

  const [starting_date, setStarting_date] = useState(
    reservation?.reservation
      ? new Date(reservation.reservation.starting_date)
      : now
  );
  const [ending_date, setEnding_date] = useState(
    reservation?.reservation
      ? new Date(reservation.reservation.ending_date)
      : now
  );

  var disabledDates = reservations?.map((reservation) => ({
    id: reservation.id,
    start: new Date(reservation.starting_date),
    end: new Date(reservation.ending_date),
    user: reservation.user,
  })) as Array<{ id: number; start: Date; end: Date; user: User }>;


  const cancel = () => {
    reservationDelete.mutate();
  };

  const submit = (e: FormEvent) => {
    e.preventDefault();
    if (starting_date.getTime() >= ending_date.getTime()) {
      toast.error("Please choose a different ending date");
      return;
    }
    // if( starting_date.getFullYear() === ending_date.getFullYear() && starting_date.getDay() === ending_date.getDay() && starting_date.getMonth() == ending_date.getMonth()) {
    //   toast.error("Please choose a different ending date");
    //   return;
    // }
    if (reservation) {
      reservationUpdate.mutate({ starting_date, ending_date });
    } else {
      reservationMutation.mutate({ starting_date, ending_date });
    }
  };

  if (isLoading) {
    return <Spinner />;
  }
  return (
    <>
      {authUser ? (
        <div>
          <div className="border-gray-700 bg-gray-300 p-5 w-fit rounded-lg mt-6 flex gap-5 mb-[15px]">
            <div>
              <div className="font-[1000]">From</div>
              <Calendar
                value={starting_date}
                onChange={setStarting_date}
                tileDisabled={({ date, view }) =>
                  (view === "month" &&
                    disabledDates.some((disabledDate) => {
                      if (
                        reservation &&
                        reservation.reservation.id == disabledDate.id
                      ) {
                        return false;
                      }
                      disabledDate.start.setHours(0);
                      disabledDate.end.setHours(0);
                      return (
                        date >= disabledDate.start && date <= disabledDate.end
                      );
                    })) ||
                  date < now
                }
                tileClassName={({ date, view }) => {

                  if (
                    view == "month" &&
                    reservations?.find((r) => {
                      return (
                        r.user.username === authUser.username &&
                        new Date(r.ending_date).getTime() >= date.getTime() &&
                        new Date(r.starting_date).getTime() <= date.getTime() &&
                        date.getTime() > new Date().getTime()
                      );
                    })
                  ) {

                    return "highlight";
                  }
                  return null;
                }}
              />
            </div>
            <div>
              <div className="font-[1000]">To</div>
              <Calendar
                value={ending_date}
                onChange={setEnding_date}
                tileDisabled={({ date, view }) =>
                  (view === "month" &&
                    disabledDates.some((disabledDate) => {
                      if (
                        reservation &&
                        reservation.reservation.id == disabledDate.id
                      ) {
                        return false;
                      }
                      disabledDate.start.setHours(0);
                      disabledDate.end.setHours(0);
                      return (
                        date >= disabledDate.start && date <= disabledDate.end
                      );
                    })) ||
                  date < now
                }
                tileClassName={({ date, view }) => {

                  if (
                    view == "month" &&
                    reservations?.find((r) => {
                      return (
                        r.user.username === authUser.username &&
                        new Date(r.ending_date).getTime() >= date.getTime() &&
                        new Date(r.starting_date).getTime() <= date.getTime() &&
                        date.getTime() > new Date().getTime()
                      );
                    })
                  ) {

                    return "highlight";
                  }
                  return null;
                }}
              />
            </div>
          </div>
          {reservation?.reservation == null ? (
            <div className="flex flex-col justify-around">
              <button
                className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                onClick={submit}
              >
                Book{" "}
                {starting_date
                  ? `for ` +
                    starting_date.getDate().toString().padStart(2, "0") +
                    "/" +
                    (starting_date.getMonth() + 1).toString().padStart(2, "0") +
                    "/" +
                    starting_date.getFullYear().toString()
                  : ""}{" "}
                {ending_date
                  ? "until " +
                    ending_date.getDate().toString().padStart(2, "0") +
                    "/" +
                    (ending_date.getMonth() + 1).toString().padStart(2, "0") +
                    "/" +
                    ending_date.getFullYear().toString()
                  : ""}{" "}
              </button>
            </div>
          ) : (
            <div className="flex flex-col justify-around">
              <button
                className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                onClick={submit}
              >
                Update{" "}
                {starting_date
                  ? `for ` +
                    starting_date.getDate().toString().padStart(2, "0") +
                    "/" +
                    (starting_date.getMonth() + 1).toString().padStart(2, "0") +
                    "/" +
                    starting_date.getFullYear().toString()
                  : ""}{" "}
                {ending_date
                  ? "until " +
                    ending_date.getDate().toString().padStart(2, "0") +
                    "/" +
                    (ending_date.getMonth() + 1).toString().padStart(2, "0") +
                    "/" +
                    ending_date.getFullYear().toString()
                  : ""}
              </button>
              <button
                className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                onClick={cancel}
              >
                Cancel this reservation
              </button>
            </div>
          )}{" "}
          {new Date(ending_date).getTime() - new Date(starting_date).getTime() >
          0 ? (
            <div>
              for{" "}
              {/* for{" "}
                {new Date(ending_date) }
                {" "}
                {new Date(starting_date) }
                  {"  "}
                {(new Date(ending_date).getTime() -
                  new Date(starting_date).getTime()) }
                  {"  "} */}
              {((new Date(ending_date).getTime() -
                new Date(starting_date).getTime()) *
                address.price) /
                (1000 * 60 * 60 * 24)}{" "}
              €
            </div>
          ) : (
            <div>Wrong date</div>
          )}
        </div>
      ) : (
        <UserLoginForm />
      )}
    </>
  );
};
