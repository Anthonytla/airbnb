import { useQuery } from "react-query"
import AuthService from "../services/AuthService";
import useLogout from "./useLogout";
import useAuthUserCache from "./useAuthUserCache";

const useAuthUser = (refetch:boolean=true) => {
  const logout = useLogout();

  const authUser = useAuthUserCache();
  
  const shouldFetchMe = !!localStorage.getItem("token") || (authUser !== undefined && authUser !== null);
console.log(authUser);

  return useQuery(['authUser'], AuthService.getMe, {
    staleTime: 2000,
    refetchInterval: 3600000,
    enabled: shouldFetchMe,
    refetchOnMount:refetch,
    onError: () => {
      logout();
    }
  });
}

export default useAuthUser;