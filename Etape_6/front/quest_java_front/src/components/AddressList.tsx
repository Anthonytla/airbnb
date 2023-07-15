import Address from '../types/Address';
import { PlaceCard } from './PlaceCard';
import { MdAddLocationAlt } from 'react-icons/md';
import { useRef, useState } from 'react';
import useOnClickOutside from '../hooks/useOnClickOutside';
import { AddressForm } from './AddressForm';
import useAuthUser from '../hooks/useAuthUser';
import useAuthUserCache from '../hooks/useAuthUserCache';
import { useLocation } from 'react-router-dom';

type Props = {addresses: Address[]}

export const AddressList = ({addresses}: Props) => {
  const modalRef = useRef<HTMLLabelElement>(null);
  const [modalIsShown, setModalIsShown] = useState(false);
  const pathName = useLocation().pathname;
  console.log(pathName);
  
  useOnClickOutside(modalRef, () => setModalIsShown(false));
  const authUser = useAuthUserCache();
  console.log(addresses);
  
    if (addresses) {
      return (
        <div className="px-10">
          <div className="grid grid-cols-4 gap-4">
            {authUser && (authUser.role == "ROLE_HOTE" || authUser.role == 'ROLE_ADMIN') && pathName != '/' ? <div onClick={() => {
                setModalIsShown(true);
              }} className="cursor-pointer bg-red-400 min-h-[380px] hover:bg-white hover:shadow-lg p-4 flex flex-col justify-center rounded-lg group">
              <h3 className="font-bold text-2xl text-center text-white group-hover:text-red-400">
                Make a proposition
              </h3>
              <MdAddLocationAlt size={80} className="mx-auto text-white group-hover:text-red-400" />
            </div> : addresses.length == 0 && (<div>No addresses found</div>)}
            
            {
              addresses.map((address) => (
                  <PlaceCard key={address.id} address={address} />
                ))
              }
              
          </div>
          <input
          type="checkbox"
          readOnly
          checked={modalIsShown}
          id="my-modal-4"
          className="modal-toggle"
        />
        <label htmlFor="my-modal-4" className="modal cursor-pointer">
          <label ref={modalRef} className="modal-box relative">
              <AddressForm address={null} setModalIsShown={setModalIsShown} />
          </label>
        </label>
        </div>
      );
    } else {
      return null;
    }
};