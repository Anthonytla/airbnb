import { AxiosError } from "axios";
import { useQuery } from "react-query";
import { useParams } from "react-router-dom";
import UserService from "../services/UserService";
import User from "../types/User";
import useLogout from "./useLogout";

const useUser = () => {
    const userId: string = useParams().id || "";
    const logout = useLogout();
    return useQuery<User, AxiosError>(["user", userId], () => UserService.get(parseInt(userId)), {
      onError: (err: any) => {
        console.log(err.response)
        if (err.response.status === 401) logout();
      }
    });
}

export default useUser