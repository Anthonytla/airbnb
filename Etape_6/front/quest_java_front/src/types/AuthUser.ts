import Address from "./Address";
import { UserRole } from "./UserRole";

export interface AuthUser {
  id: number;
  username: string;
  role: UserRole;
  addresses: Address[];
}
