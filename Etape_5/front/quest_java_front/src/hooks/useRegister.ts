import toast from "react-hot-toast";
import { useMutation } from "react-query";
import { useNavigate } from "react-router-dom";
import AuthService from "../services/AuthService";
import RegisterFormData from "../types/RegisterFormData";

const useRegister = () => {
  const navigate = useNavigate();
  const registerMutation = useMutation((userData: RegisterFormData) => AuthService.register(userData), {
    onSuccess: (data) => {
      console.log(data);
      toast.success('You registered succesfully');
      navigate('/login');
    },
    onError: (err: any) => {
      console.log('error : ', err);
      toast.error(err?.response?.data?.message || err?.message);
    }
  });

  return { registerMutation };
}

export default useRegister;
