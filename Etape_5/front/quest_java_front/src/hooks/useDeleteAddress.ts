import toast from "react-hot-toast";
import { useMutation, useQueryClient } from "react-query";
import { useParams } from "react-router-dom";
import AddressService from "../services/AddressService";
import DeleteResponse from "../types/DeleteResponse";

const useDeleteAddress = (addressId:number, setModalIsShown : (isShown:boolean) => void) => {
    const id = useParams().id || "";
    const queryClient = useQueryClient();
    const addressDelete = useMutation((addressId:number) => AddressService.delete(addressId), {
      onSuccess: (res : DeleteResponse) => {
        toast.success('Address deleted succesfully');
        queryClient.invalidateQueries(['user', id]);
        setModalIsShown(false);
      },
      onError: (err: any) => {
        console.log('Delete error : ', err);
        toast.error(err?.response?.data?.message || err?.message);
      }
    });
  
    return { addressDelete };
  }
  
  export default useDeleteAddress;