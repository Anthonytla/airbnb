import Spinner from "../components/Spinner/Spinner";
import { UserItem } from "../components/UserItem";
import useListUser from "../hooks/useListUser";
import User from "../types/User";

type Props = {};

export const UserListPage = ({}: Props) => {
  const { data: users, status } = useListUser();


  if (status === "loading") {
     return (
       <div className="mt-6">
         <Spinner />
       </div>
     );
   }

   if (status === "error") {
     return <p>Something went wrong...</p>;
   }

  if (users) {
    return (
      <div className='flex flex-col items-center mt-10'>
        {users.length > 0 ? (
          users.map((user: User, i) => (
              <UserItem key={user.id} user={user} index={i} />
          ))
        ) : (
          <div>No users found</div>
        )}
      </div>
    );
  } else {
    return null;
  }
  
};
