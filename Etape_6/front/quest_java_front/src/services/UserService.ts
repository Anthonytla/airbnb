import axios from "../api/axios"
import { AuthUser } from "../types/AuthUser";
import User from "../types/User";
import { UserRole } from "../types/UserRole";

const UserService = {
  async list(): Promise<User[]> {
    const { data } = await axios.get("/user");
    return data;
  },

  async get(id: number): Promise<User> {
    const { data } = await axios.get("/user/"+id);    
    return data;
  },

  async edit(id: string, formData:  {username:string, role:UserRole }) {
    const { data } = await axios.put(`/user/${id}`, formData);
    return data;
  },

  delete(id: string) {
    return axios.delete(`/user/${id}`);
  }
}

export default UserService;