import useUser from "../hooks/useUser";
import Spinner from "../components/Spinner/Spinner";
import { UserCard } from "../components/UserCard";
import { UserAddresses } from "../components/UserAddresses";

type Props = {};

export const UserPage = ({}: Props) => {
  const { data, error, isLoading } = useUser();

  if (isLoading) {
    return (
      <div className="mt-6">
        <Spinner />
      </div>
    );
  }

  if (error) {
    return <p>Something went wrong...</p>;
  }

  if (data) {
  console.log(data.addresses);
  
    return (
      <div className="flex flex-col items-center">
        <UserCard user={data} />
        <UserAddresses user={data} />
      </div>
    );
  } else {
    return null;
  }
      
};
