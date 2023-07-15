import { Navigate, Outlet, useLocation } from "react-router-dom";
import useAuthUserCache from "../hooks/useAuthUserCache";

const AdminGuard = () => {
  const location = useLocation();
  const authUser = useAuthUserCache();
  return authUser && authUser?.role === "ROLE_ADMIN" ? (
    <Outlet />
  ) : (
    <Navigate to="/login" state={{ from: location }} replace />
  );
};

export default AdminGuard;
