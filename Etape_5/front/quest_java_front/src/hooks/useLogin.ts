import toast from "react-hot-toast";
import { useMutation } from "react-query";
import { useNavigate } from "react-router-dom";
import AuthService from "../services/AuthService";
import RegisterFormData from "../types/RegisterFormData";
import AuthResponse from "../types/AuthResponse";
import axios from "../api/axios";

const useLogin = () => {
  const navigate = useNavigate();
  const loginMutation = useMutation((userData: RegisterFormData) => AuthService.authenticate(userData), {
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
