import { Review } from "./Review";
import User from "./User";

interface Address {
    id: number,
    street: string,
    postalCode: string,
    city:string,
    country:string,
    name:string,
    price:number,
    description:string,
    imageData: string,
    user:User,
    note: number,
    reviewsNb: number
}

export default Address;