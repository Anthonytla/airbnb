import axios from "../api/axios";
import ReservationFormData from "../types/ReservationFormData";
import Reservation from "../types/Reservation";
import DeleteResponse from "../types/DeleteResponse";

const ReservationService = {
    async add(address_id:number, formData: ReservationFormData): Promise<Reservation> {
        const { data } = await axios.post(`/address/${address_id}/reservation`, formData);
        return data;
    },

    async get() : Promise<Reservation[]> {
        const { data } = await axios.get(`/user/reservation`);
        return data;
    },

    async getByAddress(address_id:number) : Promise<Reservation[]> {
        const { data } = await axios.get(`/address/${address_id}/reservation`);
        return data;
    },

    async update(address_id: number, reservation_id:number, formData: ReservationFormData) : Promise<Reservation> {
        const { data } = await axios.put(`/address/${address_id}/reservation/${reservation_id}`, formData);
        return data;
    },

    async delete(address_id:number, reservation_id:number): Promise<DeleteResponse> {
        const { data } = await axios.delete(`/address/${address_id}/reservation/${reservation_id}`);
        return data;
    },
    async getByUserAndAddress(address_id: number) : Promise<Reservation[]> {
        const { data } = await axios.get(`/address/${address_id}/reservation/user`);
        return data;
    }
}

export default ReservationService;