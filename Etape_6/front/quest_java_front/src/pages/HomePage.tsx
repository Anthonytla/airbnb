import { AddressList } from "../components/AddressList";
import Spinner from "../components/Spinner/Spinner";
import useAddressList from "../hooks/useAddressList";

const HomePage = () => {
  const { data: addresses, status } = useAddressList();
  console.log("Addresses", addresses);
  
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

  if (addresses) {
    
    return (
      <AddressList addresses={addresses}/>
    );
  } else {
    return null;
  }
};

export default HomePage;
