import toast from "react-hot-toast";
import { useMutation, useQueryClient } from "react-query";
import AddressCreationForm from "../types/AddressCreationForm";
import AddressService from "../services/AddressService";
import Address from "../types/Address";
import { useParams } from "react-router-dom";

const useAddAddress = (setModalIsShown : (isShown:boolean) => void) => {
  const { id } = useParams();
  const queryClient = useQueryClient();
  const addressMutation = useMutation((data: AddressCreationForm) => AddressService.add(data), {
    onSuccess: (res : Address) => {
      console.log(res);
      toast.success('Address added succesfully');
      queryClient.invalidateQueries(['user', id]);
      setModalIsShown(false);
    },
    onError: (err: any) => {
      console.log('Add error : ', err);
      toast.error(err?.response?.data?.message || err?.message);
    }
  });

  return { addressMutation };
}

export default useAddAddress;