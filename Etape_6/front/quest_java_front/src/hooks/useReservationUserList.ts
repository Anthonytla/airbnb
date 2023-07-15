import { AxiosError } from "axios";
import { useQuery } from "react-query";
import ReservationService from "../services/ReservationService";
import Reservation from "../types/Reservation";

const useReservationUserList = () => {
  return useQuery<Reservation[], AxiosError>(["reservations"], ReservationService.get);
};

export default useReservationUserList;