import LoginFormData from "./LoginFormData";
import { UserRole } from "./UserRole";

interface RegisterFormData extends LoginFormData{
  role: UserRole,
}

export default RegisterFormData;
