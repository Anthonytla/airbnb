import { useQueryClient } from "react-query";
import { useNavigate } from "react-router-dom";
import axios from "../api/axios";

const useLogout = () => {
  const queryClient = useQueryClient();

  const navigate = useNavigate();

  const logout = () => {
    localStorage.removeItem("token");
    queryClient.setQueryData(['authUser'], null);
    axios.defaults.headers.common['Authorization'] = "";
    navigate('/login');
  }

  return logout;
}

export default useLogout;