import { AxiosError, AxiosResponse } from "axios";
import { useQuery } from "react-query";
import UserService from "../services/UserService";
import User from "../types/User";

const useListUser = () => {
  return useQuery<User[], AxiosError>(["users"], UserService.list);
};

export default useListUser;
