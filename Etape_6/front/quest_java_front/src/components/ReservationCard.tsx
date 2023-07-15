import React from 'react';
import { Link } from 'react-router-dom';
import Reservation from '../types/Reservation';

type Props = {reservation:Reservation}

export const ReservationCard = ({reservation}: Props) => {
    
    const address = reservation.address;
    const startDate = new Date(reservation.starting_date);
    const endDate = new Date(reservation.ending_date)
    return (
      <Link to={`/address/${address.id}`} state={{reservation}}>
        <div>
          <div className="aspect-video w-full h-[300px]">
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
          <div className="text-gray-400 pb-2">{address.name}</div>
          <div className="text-red-500">
            Vous avez une réservation de{" "}
            {startDate.getDate().toString().padStart(2, "0") +
              "/" +
              (startDate.getMonth() + 1).toString().padStart(2, "0") +
              "/" +
              startDate.getFullYear().toString()}{" "}
            au{" "}
            {endDate.getDate().toString().padStart(2, "0") +
              "/" +
              (endDate.getMonth() + 1).toString().padStart(2, "0") +
              "/" +
              endDate.getFullYear().toString()}
          </div>
        </div>
      </Link>
    );};