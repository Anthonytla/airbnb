import toast from "react-hot-toast";
import { useMutation, useQueryClient } from "react-query";
import { useParams } from "react-router-dom";
import ReservationService from "../services/ReservationService";
import Reservation from "../types/Reservation";
import ReservationFormData from "../types/ReservationFormData";

const useUpdateReservation = (reservationId:number) => {
    const addressId = useParams().addressId as string;
    const queryClient = useQueryClient();
    const reservationUpdate = useMutation((data: ReservationFormData) => ReservationService.update(parseInt(addressId), reservationId,data), {
      onSuccess: (res : Reservation) => {
        console.log(res);
        toast.success('Reservation updated succesfully');
        queryClient.invalidateQueries(["reservations", addressId]);
      },
      onError: (err: any) => {
        console.log('Update error : ', err);
        toast.error(err?.response?.data?.message || err?.message);
      }
    });
  
    return { reservationUpdate };
  }
  
  export default useUpdateReservation;