import { AxiosError } from "axios";
import { useQuery } from "react-query";
import { useParams } from "react-router-dom";
import AddressService from "../services/AddressService";
import Address from "../types/Address";

const useAddressDetails = () => {
  const addressId: string = useParams().addressId || "";
  return useQuery<Address, AxiosError>(["addresses", addressId], () =>
    AddressService.findById(parseInt(addressId))
  );
};

export default useAddressDetails;
