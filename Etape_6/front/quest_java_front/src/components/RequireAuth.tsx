import { useEffect, useRef } from "react";
import { useQueryClient } from "react-query";
import { Navigate, Outlet, useLocation } from "react-router-dom";
import useAuthUser from "../hooks/useAuthUser";

const RequireAuth = () => {
  const { data: authUser } = useAuthUser();
  const location = useLocation();
  const queryClient = useQueryClient();

  const isMounted = useRef(false);

  useEffect(() => {
    if (isMounted.current) queryClient.invalidateQueries('authUser');
    isMounted.current = true;
  }, [location]);

  return authUser ? (
    <Outlet />
  ) : (
    <Navigate to="/login" state={{ from: location }} replace />
  );
};

export default RequireAuth;
