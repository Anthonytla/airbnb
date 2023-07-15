import { useQuery } from "react-query"
import AuthService from "../services/AuthService";
import useLogout from "./useLogout";

const useAuthUser = () => {
  const logout = useLogout();

  return useQuery(['authUser'], AuthService.getMe, {
    staleTime: 2000,
    refetchInterval: 3600000,
    enabled: !!localStorage.getItem("token"),
    onError: () => {
      logout();
    }
  });
}

export default useAuthUser;