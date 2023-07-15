import User from "../types/User";
import { BsPencilSquare } from "react-icons/bs";
import { RiDeleteBin5Line } from "react-icons/ri";
import { useRef, useState } from "react";
import DeleteUserModal from "./DeleteUserModal";
import useOnClickOutside from "../hooks/useOnClickOutside";
import useAuthUserCache from "../hooks/useAuthUserCache";
import EditUserModal from "./EditUserModal";

type Props = { user: User };

export const UserCard = ({ user }: Props) => {
  const authUser = useAuthUserCache();
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const deleteModalRef = useRef<HTMLLabelElement>(null);
  const editModalRef = useRef<HTMLLabelElement>(null);
  useOnClickOutside(deleteModalRef, () => setShowDeleteModal(false));
  useOnClickOutside(editModalRef, () => setShowEditModal(false));

  if (user) {
    const createdDate = new Date(user.createdAt);
    console.log(createdDate);

    const updatedDate = new Date(user.updatedAt);
    return (
      <>
        <div className="max-w-[500px] min-w-[500px] rounded-lg bg-gray-300 p-5">
          <div className="flex justify-between items-center mb-2">
            <span className="font-bold">{user.username}</span>
            <span>
              {(authUser?.username === user.username ||
                authUser?.role === "ROLE_ADMIN") && (
                <>
                  <BsPencilSquare
                    size={20}
                    className="inline-block cursor-pointer hover:text-purple-700"
                    onClick={() => setShowEditModal(true)}
                  />
                  <RiDeleteBin5Line
                    onClick={() => setShowDeleteModal(true)}
                    size={20}
                    className="inline-block ml-2 cursor-pointer hover:text-purple-700"
                  />
                </>
              )}
            </span>
          </div>
          {user.role == "ROLE_USER" ? (
            <div className="text-center bg-gradient-to-t from-green-500 to-green-300 text-white min-w-[50px] max-w-[50px] rounded mb-2">
              user
            </div>
          ) : (
            <div className="rounded text-center bg-gradient-to-t from-red-500 to-red-300 text-white min-w-[50px] max-w-[50px] mb-2">
              admin
            </div>
          )}
          <div className="mb-2">
            {" "}
            Created at{" "}
            {createdDate.getDate().toString().padStart(2, "0") +
              "/" +
              (createdDate.getMonth() + 1).toString().padStart(2, "0") +
              "/" +
              createdDate.getFullYear().toString()}{" "}
            à{" "}
            {createdDate.getHours().toString().padStart(2, "0") +
              ":" +
              createdDate.getMinutes().toString().padStart(2, "0") +
              ":" +
              createdDate.getSeconds().toString().padStart(2, "0")}
          </div>
          <div className="mb-2">
            {" "}
            Updated at{" "}
            {updatedDate.getDate().toString().padStart(2, "0") +
              "/" +
              (updatedDate.getMonth() + 1).toString().padStart(2, "0") +
              "/" +
              updatedDate.getFullYear().toString().padStart(2, "0")}{" "}
            à{" "}
            {updatedDate.getHours().toString().padStart(2, "0") +
              ":" +
              updatedDate.getMinutes().toString().padStart(2, "0") +
              ":" +
              updatedDate.getSeconds().toString().padStart(2, "0")}
          </div>
        </div>
        <DeleteUserModal
          modalIsShown={showDeleteModal}
          modalRef={deleteModalRef}
          username={user.username}
        />
        <EditUserModal
          modalIsShown={showEditModal}
          setShow={setShowEditModal}
          modalRef={editModalRef}
          user={user}
        />
      </>
    );
  } else {
    return null;
  }
};
