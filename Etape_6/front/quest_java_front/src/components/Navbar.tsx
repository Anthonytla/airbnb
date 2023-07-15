import { Link, useLocation } from "react-router-dom";
import { FiLogOut } from "react-icons/fi";
import useLogout from "../hooks/useLogout";
import { ReactNode, SyntheticEvent, useContext } from "react";
import { AuthUser } from "../types/AuthUser";
import { FaUserAlt, FaUserCircle } from "react-icons/fa";
import { UserRoleContext, UserRoleContextType } from "../providers/useUserRole";

type NavbarProps = {
  authUser?: AuthUser | undefined;
};

const Navbar = ({ authUser }: NavbarProps) => {
  const logout = useLogout();
  const dropdownContent: ReactNode[] = [];
  const location = useLocation();
  const { setUserRole } = useContext(UserRoleContext) as UserRoleContextType;

  var navClass =
    "bg-gradient-to-t navbar shadow-md sticky top-0 px-20 mb-6 from-gray-500 to-gray-300 text-white mb-2";
  if (authUser) {
    dropdownContent.push(
      <li key="profile">
        <Link className="w-full" to={`/user/${authUser.id}`}>
          My profile
        </Link>
      </li>
    );
    if (authUser.role == "ROLE_HOTE") {
      dropdownContent.push(
        <li key="propositions">
          <Link className="w-full" to="/host/propositions">
            My propositions
          </Link>
        </li>
      );
      navClass =
        "bg-gradient-to-t navbar shadow-md sticky top-0 px-20 mb-6 from-blue-500 to-blue-300 text-white mb-2";
    } else if (authUser && authUser.role === "ROLE_ADMIN") {
      dropdownContent.push(
        <li key="usersList">
          <Link className="w-full" to="/admin/users">
            users
          </Link>
        </li>
      );
      navClass =
        "bg-gradient-to-t navbar shadow-md sticky top-0 px-20 mb-6 from-red-500 to-red-300 text-white mb-2";
    } else if (authUser && authUser.role === "ROLE_VOYAGEUR") {
      navClass =
        "bg-gradient-to-t navbar shadow-md sticky top-0 px-20 mb-6 from-green-500 to-green-300 text-white";
    }
    dropdownContent.push(
      <li key="bookings">
        <Link className="w-full" to="/traveler/reservations">
          My bookings
        </Link>
      </li>
    );

    dropdownContent.push(
      <li key="logout">
        <a onClick={logout} className="w-full">
          Logout
        </a>
      </li>
    );
  } else {
    dropdownContent.push(
      <div key="auth">
        <li className="">
          <Link
            to="/login"
            className="w-full"
            state={{ from: location }}
            replace
          >
            Login
          </Link>
        </li>
        <li className="">
          <Link
            to="/register"
            onClick={() => setUserRole("ROLE_VOYAGEUR")}
            className="w-full"
          >
            New traveler
          </Link>
        </li>
      </div>
    );
  }

  return (
    <div className={navClass}>
      <div className="flex-1">
        <Link to="/" className="btn btn-ghost normal-case text-xl">
          Welcome {authUser?.username ?? "visitor"} !
        </Link>
      </div>
      <div className="flex-none">
        <ul className="menu menu-horizontal p-0">
          <li>
            {authUser && authUser.role == "ROLE_HOTE" && (
              <a className="cursor-default pointer-events-none">Host mode</a>
            )}
            {authUser && authUser.role == "ROLE_VOYAGEUR" && (
              <a className="cursor-default pointer-events-none">
                Traveler mode
              </a>
            )}
            {authUser && authUser.role == "ROLE_ADMIN" && (
              <a className="cursor-default pointer-events-none">Admin mode</a>
            )}
            {!authUser && (
              <Link to="/register" onClick={() => setUserRole("ROLE_HOTE")}>
                Become host
              </Link>
            )}
          </li>
          <div className="flex items-center">
            <div className="dropdown dropdown-end">
              <label tabIndex={0}>
                <FaUserCircle className="cursor-pointer" size={40} />
              </label>
              <ul
                tabIndex={0}
                className="dropdown-content text-black z-auto menu p-2 shadow bg-base-100 rounded-box w-52"
              >
                {dropdownContent.map((link) => link)}
              </ul>
            </div>
          </div>
        </ul>
      </div>
    </div>
  );
};

export default Navbar;
