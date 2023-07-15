import toast from "react-hot-toast";
import { useMutation, useQueryClient } from "react-query";
import { useNavigate, useParams } from "react-router-dom";
import ReservationService from "../services/ReservationService";
import DeleteResponse from "../types/DeleteResponse";

const useDeleteReservation = (reservationId:number) => {
    const id = useParams().addressId || "";
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    const reservationDelete = useMutation(() => ReservationService.delete(parseInt(id), reservationId), {
      onSuccess: (res : DeleteResponse) => {
        toast.success('Reservation cancelled succesfully');
        navigate("/traveler/reservations")
      },
      onError: (err: any) => {
        console.log('Delete error : ', err);
        toast.error(err?.response?.data?.message || err?.message);
      }
    });
  
    return { reservationDelete };
  }
  
  export default useDeleteReservation;