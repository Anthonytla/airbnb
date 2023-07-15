import { Navigate, Outlet, useLocation, useParams } from "react-router-dom";
import useAuthUserCache from "../hooks/useAuthUserCache";

const OwnerOrAdminGuard = () => {
  const location = useLocation();
  const authUser = useAuthUserCache();
  const id = useParams().id ?? "";
  return authUser &&
    (authUser?.role === "ROLE_ADMIN" || authUser.id === parseInt(id)) ? (
    <Outlet />
  ) : (
    <Navigate to="/login" state={{ from: location }} replace />
  );
};

export default OwnerOrAdminGuard;
