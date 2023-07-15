import Address from '../types/Address';
import Reservation from '../types/Reservation';
import { PlaceCard } from './PlaceCard';
import { ReservationCard } from './ReservationCard';

type Props = {reservations: Reservation[]}

export const ReservationList = ({reservations}: Props) => {

    if (reservations) {
      return (
        <div className="px-10">
          <div className="grid grid-cols-4 gap-4">
            {reservations.length > 0
              ? reservations.map((reservation) => <ReservationCard key={reservation.id} reservation={reservation} />)
              : "No reservations found..."}
          </div>
        </div>
      );
    } else {
      return null;
    }
};