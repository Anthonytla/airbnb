import axios from "../api/axios"
import AuthResponse from "../types/AuthResponse";
import LoginFormData from "../types/LoginFormData";
import RegisterFormData from "../types/RegisterFormData";
import User from "../types/User";

const AuthService = {
  async register(credentials: RegisterFormData) {
    const { data } = await axios.post("/register", credentials);
    return data;;
  },

  async authenticate(credentials: LoginFormData): Promise<AuthResponse> {
    const { data } = await axios.post("/authenticate", credentials);
    return data;
  },

  async getMe(): Promise<User> {
    const { data } = await axios.get("/me");
    return data;
  },

  header() {
    const jwtStr = localStorage.getItem("token");
    if (jwtStr) {
      return { Authorization: "Bearer " + jwtStr };
    } else {
      return { Authorization: "" };
    }
  },
};

export default AuthService;