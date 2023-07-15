import { AxiosError } from "axios";
import { useQuery } from "react-query";
import AddressService from "../services/AddressService";
import Address from "../types/Address";

const useAddressList = () => {
  return useQuery<Address[], AxiosError>(["addresses"], AddressService.findAll);
};

export default useAddressList;