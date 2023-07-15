import { createContext, ReactNode, useContext, useState } from "react";
import { UserRole } from "../types/UserRole";

interface Props {
  children: ReactNode
}

export interface UserRoleContextType {
  userRole: UserRole,
  setUserRole: (role: UserRole) => void
}

export const UserRoleContext = createContext<UserRoleContextType | null>(null);

export const UserRoleProvider = ({ children }: Props) => {
  const [userRole, setUserRole] = useState<UserRole>('ROLE_VOYAGEUR');

  return (
    <UserRoleContext.Provider value={{ userRole, setUserRole }}>
      {children}
    </UserRoleContext.Provider>
  );
}

// export const useUserRole = useContext(UserRoleContext);