import { MdArrowForwardIos } from 'react-icons/md';
import { GoLocation } from 'react-icons/go';
import Address from '../types/Address';

type Props = {address: Address, index: number}

export const AddressItem = ({ address, index }: Props) => (
  <div
    className={`${
      index % 2 == 0 ? "bg-gray-100" : "bg-gray-200"
    } flex-row justify-between hover:bg-gray-300 flex p-5 mx-auto max-w-[500px] min-w-[500px]`}
  >
    <div className='flex items-center'>
      <GoLocation className='mr-2' />
      <div>{address.street + " " + address.city + " "+ address.postalCode + " "+address.country}</div>
    </div>
    <div>
      <MdArrowForwardIos className="inline" />
    </div>
  </div>
);