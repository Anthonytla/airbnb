import toast from "react-hot-toast";
import { useMutation } from "react-query"
import { useNavigate, useParams } from "react-router-dom"
import UserService from "../services/UserService";
import useAuthUserCache from "./useAuthUserCache";
import useLogout from './useLogout';

const useDeleteUser = (username: string) => {
  const logout = useLogout();
  const authUser = useAuthUserCache();
  const navigate = useNavigate();

  const userId = useParams().id as string;
  const userDeleteMutation = useMutation(() => UserService.delete(userId), {
    onSuccess: () => {
      toast.success('account deleted');

      authUser?.username === username ? logout() : navigate('/users'); 
    },
    onError: (err: any) => {
      toast.error(err?.response?.data?.message || err?.message);
    }
  })

  return userDeleteMutation;
}

export default useDeleteUser;