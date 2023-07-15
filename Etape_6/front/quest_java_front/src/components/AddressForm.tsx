import { FormEvent, useEffect, useState } from "react";
import toast from "react-hot-toast";
import useAddAddress from "../hooks/useAddAddress";
import useDeleteAddress from "../hooks/useDeleteAddress";
import useUpdateAddress from "../hooks/useUpdateAddress";
import { Input } from "./Input/Input";
import Spinner from "./Spinner/Spinner";
import Resizer from "react-image-file-resizer";

type Props = { setModalIsShown: (isShown: boolean) => void; address: any };

export const AddressForm = ({ setModalIsShown, address }: Props) => {
  
  
  const { addressMutation } = useAddAddress(setModalIsShown);
  const { addressUpdate } = useUpdateAddress(
    address ? address.id : null,
    setModalIsShown
  );
  const { addressDelete } = useDeleteAddress(
    address ? address.id : null,
    setModalIsShown
  );
  // const resizeFile = (file : File) =>
  // new Promise((resolve) => {
  //   Resizer.imageFileResizer(
  //     file,
  //     448,
  //     300,
  //     "JPEG",
  //     100,
  //     0,
  //     (uri) => {
  //       resolve(uri);
  //     },
  //     "base64",
  //     0,
  //   );
  // });

  var prefillStreet = address ? address.street : "";
  const [street, setStreet] = useState(prefillStreet);
  useEffect(() => {
    setStreet(prefillStreet);
  }, [prefillStreet]);

  var prefillPostalCode = address ? address.postalCode : "";
  const [postalCode, setPostalCode] = useState(prefillPostalCode);
  useEffect(() => {
    setPostalCode(prefillPostalCode);
  }, [prefillPostalCode]);

  var prefillCity = address ? address.city : "";
  const [city, setCity] = useState(prefillCity);
  useEffect(() => {
    setCity(prefillCity);
  }, [prefillCity]);

  var prefillCountry = address ? address.country : "";
  const [country, setCountry] = useState(prefillCountry);
  useEffect(() => {
    setCountry(prefillCountry);
  }, [prefillCountry]);

  var prefillName = address ? address.name : "";
  const [name, setName] = useState(prefillName);
  useEffect(() => {
    setName(prefillName);
  }, [prefillName]);

  var prefillPrice = address ? address.price : 0;
  const [price, setPrice] = useState(prefillPrice);
  useEffect(() => {
    setPrice(prefillPrice);
  }, [prefillPrice]);

  var prefillDescription = address ? address.description : "";
  const [description, setDescription] = useState(prefillDescription);
  useEffect(() => {
    setDescription(prefillDescription);
  }, [prefillDescription]);

  const [imageData, setImageData] = useState<any>(null);
  const handleFileChange = async (e: any) => {
    console.table(e.target.files[0]);
    const fileReader : FileReader = new FileReader();
    fileReader.onloadend = ()=>{
      setImageData(fileReader.result?.toString().split(",")[1]);
      
    }
    //const image = await resizeFile(e.target.files[0]) as string
    //setImageData(image.split(",")[1]);
    fileReader.readAsDataURL(e.target.files[0])
    
  }

  const canSubmit =
    !addressMutation.isLoading &&
    street.length > 0 &&
    postalCode.length > 0 &&
    city.length > 0 &&
    country.length > 0;

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    if (canSubmit) {
      if (!address) {
        addressMutation.mutate({ street, postalCode, city, country, name, description, price, imageData });
      } else {
        addressUpdate.mutate({ street, postalCode, city, country, name, description, price, imageData });
      }
    } else {
      toast.error("fields required !");
    }
  };
  const handleDelete = (e: FormEvent) => {
    e.preventDefault();
    addressDelete.mutate(address ? address.id : null);
  };
  return (
    <form
      onSubmit={handleSubmit}
      className="flex flex-col max-w-[500px] min-w-[400px] rounded-lg mt-6"
    >
      <h1 className="font-[1000] text-3xl text-center">
        {address ? "Update" : "Create"} address
      </h1>
      <Input
        inputType="text"
        inputLabel="Street"
        inputValue={street}
        onValueChange={setStreet}
      />
      <Input
        inputType="text"
        inputLabel="Postal code"
        inputValue={postalCode}
        onValueChange={setPostalCode}
      />
      <Input
        inputType="text"
        inputLabel="City"
        inputValue={city}
        onValueChange={setCity}
      />
      <Input
        inputType="text"
        inputLabel="Country"
        inputValue={country}
        onValueChange={setCountry}
      />
      <Input
        inputType="text"
        inputLabel="Name"
        inputValue={name}
        onValueChange={setName}
      />
      <label>Description</label>
      <textarea
        className="border whitespace-pre-line border-gray-700 rounded-md h-8 mt-1 px-2"
        value={description}
        onChange={(ev:any) => {          
          setDescription(ev.target.value);
        }}
      />
      <Input
        inputType="number"
        inputLabel="Price"
        inputValue={price}
        onValueChange={setPrice}
      />
      <div className="flex flex-col my-2">
        <label className="text-left">Image</label>
        <input
          type="file"
          onChange={handleFileChange}
          className="border border-gray-700 rounded-md h-8 mt-1 px-2"
        />
        {/* <p>{image ? `${(image?.size / 1000).toFixed(2)} kB` : "No file selected"}</p> */}
      </div>
      <div className="flex justify-between">
        {address == null && (
          <button
            className={`${
              canSubmit
                ? "bg-blue-500 hover:bg-blue-600 hover:scale-105"
                : "bg-slate-400"
            } rounded max-w-[30%] p-2 text-white my-2 transition ease-in-out delay-100 duration-250`}
            type="submit"
            disabled={!canSubmit}
          >
            {addressMutation.isLoading ? (
              <Spinner white={true} size="sm" />
            ) : (
              "Create"
            )}
          </button>
        )}
        {address != null && (
          <button
            className={` ${
              canSubmit
                ? "bg-blue-500 hover:bg-blue-600 hover:scale-105"
                : "bg-slate-400"
            } rounded max-w-[30%] p-2 text-white my-2 transition ease-in-out delay-100 duration-250`}
            type="submit"
            disabled={!canSubmit}
          >
            {addressUpdate.isLoading ? (
              <Spinner white={true} size="sm" />
            ) : (
              "Update"
            )}
          </button>
        )}
        {address != null && (
          <button
            className="bg-red-500 hover:bg-red-600 hover:scale-105
              rounded max-w-[30%] p-2 text-white my-2 transition ease-in-out delay-100 duration-250"
            onClick={handleDelete}
          >
            {addressDelete.isLoading ? (
              <Spinner white={true} size="sm" />
            ) : (
              "Delete"
            )}
          </button>
        )}
      </div>
    </form>
  );
};
