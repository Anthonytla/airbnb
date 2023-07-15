import toast from "react-hot-toast";
import { useMutation, useQueryClient } from "react-query";
import { useParams } from "react-router-dom";
import AddressService from "../services/AddressService";
import Address from "../types/Address";
import AddressCreationForm from "../types/AddressCreationForm";

const useUpdateAddress = (addressId:number, setModalIsShown : (isShown:boolean) => void) => {
    const id = useParams().id || "";
    const queryClient = useQueryClient();
    const addressUpdate = useMutation((data: AddressCreationForm) => AddressService.update(addressId,data), {
      onSuccess: (res : Address) => {
        console.log(res);
        toast.success('Address updated succesfully');
        queryClient.invalidateQueries(['user', id]);
        queryClient.invalidateQueries(['addresses']);
        setModalIsShown(false);
      },
      onError: (err: any) => {
        console.log('Update error : ', err);
        toast.error(err?.response?.data?.message || err?.message);
      }
    });
  
    return { addressUpdate };
  }
  
  export default useUpdateAddress;