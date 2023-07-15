import { AxiosError } from "axios";
import { useQuery } from "react-query";
import ReservationService from "../services/ReservationService";
import Reservation from "../types/Reservation";

const useReservationAddressList = (addressId: any) => {
  return useQuery<Reservation[], AxiosError>(["reservations", addressId], ()=> ReservationService.getByAddress(addressId));
};

export default useReservationAddressList;