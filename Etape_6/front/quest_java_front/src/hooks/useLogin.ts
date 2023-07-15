import toast from "react-hot-toast";
import { useMutation } from "react-query";
import AuthService from "../services/AuthService";
import AuthResponse from "../types/AuthResponse";
import axios from "../api/axios";
import LoginFormData from "../types/LoginFormData";

const useLogin = () => {
  const loginMutation = useMutation((userData: LoginFormData) => AuthService.authenticate(userData), {
    onSuccess: (res : AuthResponse) => {
      console.log('token : ', res.token);
      localStorage.setItem("token", res.token);
      axios.defaults.headers.common['Authorization'] = 'Bearer ' + res.token;
    },
    onError: (err: any) => {
      console.log('login error : ', err);
      toast.error(err?.response?.data?.message || err?.message);
    }
  });

  return { loginMutation };
}

export default useLogin;
