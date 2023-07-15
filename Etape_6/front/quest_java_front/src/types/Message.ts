import Address from "./Address";

interface Message {
  id: number;
  fromHost: string;
  text: string;
  address: Address;
}

export default Message;