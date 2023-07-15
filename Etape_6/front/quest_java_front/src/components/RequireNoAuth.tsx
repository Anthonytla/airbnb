import { Navigate, Outlet } from "react-router-dom";
import useAuthUser from "../hooks/useAuthUser";

const RequireNoAuth = () => {
  const { data: authUser } = useAuthUser();

  return !authUser ? (
    <Outlet />
  ) : (
    <Navigate to="/" />
  );
};

export default RequireNoAuth;
