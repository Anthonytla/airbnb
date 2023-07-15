import User from '../types/User';
import { MdArrowForwardIos } from 'react-icons/md';
import { FaUser } from 'react-icons/fa';
import { Link } from 'react-router-dom';

type Props = {user: User, index: number}

export const UserItem = ({ user, index }: Props) => (
  <Link
    key={user.id}
    to={"/user/" + user.id}
    state={user}
    className={`${
      index % 2 == 0 ? "bg-gray-100" : "bg-gray-200"
    } flex-row justify-between hover:bg-gray-300 flex max-w-[800px] min-w-[800px] p-5`}
  >
    <div className='flex items-center'>
      <FaUser className='mr-2' />
      <div>{user.username}</div>
    </div>
    <div>
      <MdArrowForwardIos className="inline" />
    </div>
  </Link>
);