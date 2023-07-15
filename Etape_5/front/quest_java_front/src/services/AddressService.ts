import axios from "../api/axios";
import Address from "../types/Address";
import AddressCreationForm from "../types/AddressCreationForm";
import DeleteResponse from "../types/DeleteResponse";

const AddressService = {
  async add(formData: AddressCreationForm): Promise<Address> {
    const { data } = await axios.post("/address", formData);
    return data;
  },

  async update(id:number, formData: AddressCreationForm): Promise<Address> {
    const { data } = await axios.put("/address/"+id, formData);
    return data;
  },

  async delete(id:number) : Promise<DeleteResponse> {
    const { data } = await axios.delete("address/" + id);
    return data;
  }
};

export default AddressService;