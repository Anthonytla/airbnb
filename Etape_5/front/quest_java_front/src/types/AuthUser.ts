import { UserRole } from "./UserRole";

export interface AuthUser {
  username: string,
  role: UserRole
}