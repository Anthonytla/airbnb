import toast from "react-hot-toast";
import { useMutation } from "react-query";
import { useNavigate } from "react-router-dom";
import ReservationService from "../services/ReservationService";
import ReservationFormData from "../types/ReservationFormData";

const useAddReservation = (addressId: number) => {
    const navigate = useNavigate();
    const reservationMutation = useMutation((reservationData: ReservationFormData) => ReservationService.add(addressId, reservationData), {
        onSuccess: (data) => {
          console.log(data);
          toast.success('You have reserved successfully this address');
          navigate('/traveler/reservations');
        },
        onError: (err: any) => {
          console.log('error : ', err);
          toast.error(err?.response?.data?.message || err?.message);
        }
      });
      return { reservationMutation };
}

export default useAddReservation;