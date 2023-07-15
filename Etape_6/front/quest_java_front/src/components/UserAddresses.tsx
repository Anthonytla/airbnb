import Address from "../types/Address";
import { AddressItem } from "./AddressItem";
import { AiOutlinePlusCircle } from "react-icons/ai";
import { AddressForm } from "./AddressForm";
import { useQueryClient } from "react-query";
import User from "../types/User";
import { useRef, useState } from "react";
import useOnClickOutside from "../hooks/useOnClickOutside";

type Props = { user: User };

export const UserAddresses = ({ user }: Props) => {
  const { addresses } = user;
  const queryClient = useQueryClient();
  const [selected, setSelected] = useState<Address | null>(null!);
  const modalRef = useRef<HTMLLabelElement>(null);
  const [modalIsShown, setModalIsShown] = useState(false);
  useOnClickOutside(modalRef, () => setModalIsShown(false));
  const onGoToUpdate = (address: Address) => {
    setSelected(address);
    setModalIsShown(true);
  };
  const { username } = queryClient.getQueryData("authUser") as User;
  if (addresses) {
    return (
      <div className="flex flex-col mt-20 max-w-[500px] min-w-[500px]">
        <div className="flex justify-between pb-5">
          <h1 className="text-center font-bold">Addresses</h1>
          {username == user.username && (
            <AiOutlinePlusCircle
              size={25}
              className="cursor-pointer"
              onClick={() => {
                setSelected(null);
                setModalIsShown(true);
              }}
            />
          )}
        </div>
        {addresses.length > 0 ? (
          addresses.map((address: Address, i) => (
            <div
              key={address.id}
              onClick={() => onGoToUpdate(address)}
              className="cursor-pointer"
            >
              <AddressItem key={address.id} address={address} index={i} />
            </div>
          ))
        ) : (
          <div className="text-center">No addresses found</div>
        )}

        <input
          type="checkbox"
          readOnly
          checked={modalIsShown}
          id="my-modal-4"
          className="modal-toggle"
        />
        <label htmlFor="my-modal-4" className="modal cursor-pointer">
          <label ref={modalRef} className="modal-box relative">
            <AddressForm address={selected} setModalIsShown={setModalIsShown} />
          </label>
        </label>
      </div>
    );
  } else {
    return null;
  }
};
