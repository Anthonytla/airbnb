import Address from "./Address";
import { UserRole } from "./UserRole";

interface User {
    id: number,
    username: string,
    role: UserRole,
    createdAt: string,
    updatedAt: string,
    addresses: Address[],
}

export default User;