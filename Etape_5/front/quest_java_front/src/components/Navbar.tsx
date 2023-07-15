import { Link } from "react-router-dom";
import { FiLogOut } from "react-icons/fi";
import useLogout from "../hooks/useLogout";
import { SyntheticEvent } from "react";
import { AuthUser } from "../types/AuthUser";

type NavbarProps = {
  authUser?: AuthUser | undefined;
};

const Navbar = ({ authUser }: NavbarProps) => {
  const logout = useLogout();

  return (
    <div className="navbar bg-gray-300 shadow-md sticky top-0 px-20 mb-6">
      <div className="flex-1">
        <Link to="/users" className="btn btn-ghost normal-case text-xl">
          Welcome {authUser?.username ?? "visitor"} !
        </Link>
      </div>
      <div className="flex-none">
        <ul className="menu menu-horizontal p-0">
          <li>
            <Link to="/users">Users</Link>
          </li>
          {authUser && (
            <li className="ml-5">
              <button onClick={logout} className="btn text-white">
                Logout
                <FiLogOut className="cursor-pointer" />
              </button>
            </li>
          )}
        </ul>
      </div>
    </div>
  );
};

export default Navbar;
