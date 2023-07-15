import Address from "./Address";
import User from "./User";

interface Reservation {
    id: number,
    starting_date: Date,
    ending_date: Date,
    user: User,
    address:Address
}

export default Reservation;