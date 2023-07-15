import { useQueryClient } from "react-query"
import { AuthUser } from "../types/AuthUser";

const useAuthUserCache = () => {
  const queryClient = useQueryClient();

  return queryClient.getQueryData('authUser') as AuthUser | null;
}

export default useAuthUserCache;