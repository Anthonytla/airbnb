import { FormEvent, useState } from "react";
import { Input } from "./Input/Input";
import toast from "react-hot-toast";
import useRegister from "../hooks/useRegister";
import Spinner from "./Spinner/Spinner";

type Props = {};

export const UserRegisterForm = ({}: Props) => {
  const { registerMutation } = useRegister();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const canSubmit =
    !registerMutation.isLoading && username.length > 0 && password.length > 0;

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    if (canSubmit) {
      registerMutation.mutate({ username, password });
    } else {
      toast.error("username and password are required !");
    }
  };
  return (
    <form
      onSubmit={handleSubmit}
      className="flex flex-col border-gray-700 max-w-[500px] min-w-[400px] bg-gray-300 p-5 rounded-lg mt-6"
    >
      <h1 className="font-[1000] text-3xl text-center">Create account</h1>
      <Input
        inputType="text"
        inputLabel="Username"
        inputValue={username}
        onValueChange={setUsername}
      />
      <Input
        inputType="password"
        inputLabel="Password"
        inputValue={password}
        onValueChange={setPassword}
      />
      <button
        className={`mx-auto ${
          canSubmit
            ? "bg-blue-500 hover:bg-blue-600 hover:scale-105"
            : "bg-slate-400"
        } rounded max-w-[30%] p-2 text-white my-2 transition ease-in-out delay-100 duration-250`}
        type="submit"
        disabled={!canSubmit}
      >
        {registerMutation.isLoading ? (
          <Spinner white={true} size="sm" />
        ) : (
          "Register"
        )}
      </button>
    </form>
  );
};
