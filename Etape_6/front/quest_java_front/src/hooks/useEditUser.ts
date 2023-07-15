import toast from "react-hot-toast";
import { useMutation, useQueryClient } from "react-query";
import { useParams } from "react-router-dom";
import UserService from "../services/UserService";
import { AuthUser } from "../types/AuthUser";
import User from "../types/User";
import { UserRole } from "../types/UserRole";
import useAuthUserCache from "./useAuthUserCache";

const useEditUser = () => {
  const queryClient = useQueryClient();
  const authUser = useAuthUserCache();
  const userId = useParams().id as string;
  const userEditMutation = useMutation(
    (formData: { username: string; role: UserRole }) =>
      UserService.edit(userId, formData),
    {
      onMutate: async (formData: { username: string; role: UserRole }) => {
        await queryClient.cancelQueries(["user", userId]);
        const previousUserData = queryClient.getQueryData([
          "user",
          userId,
        ]) as User;
        const newUser = { ...previousUserData, ...formData };
        queryClient.setQueryData(["user", userId], newUser);
        console.log("new user : ", newUser);
        return { previousUserData, newUser };
      },
      onSuccess: (_data, _formData, context) => {
        if (authUser?.username === context?.previousUserData.username) {
          queryClient.setQueryData(["authUser"], context?.newUser);
        }
        toast.success("Account updated !");
      },
      onError: (err: any, _formData, context) => {
        queryClient.setQueryData(["user", userId], context?.previousUserData);
        toast.error(err?.response?.data?.message || err?.message);
      },
      onSettled: () => {
        queryClient.invalidateQueries(["user", userId]);
      },
    }
  );

  return userEditMutation;
};

export default useEditUser;
