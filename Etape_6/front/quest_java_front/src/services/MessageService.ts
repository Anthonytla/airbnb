import axios from "../api/axios"

const MessageService = {
    async list(addressId:number) {
        const { data } = await axios.get('/message/'+addressId);
        return data;
    }
}

export default MessageService;