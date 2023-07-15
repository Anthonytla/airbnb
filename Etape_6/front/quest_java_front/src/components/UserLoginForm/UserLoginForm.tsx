import { FormEvent, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { Input } from "../Input/Input";
import Spinner from "../Spinner/Spinner";
import useLogin from "../../hooks/useLogin";
import AuthService from "../../services/AuthService";
import { useQueryClient } from "react-query";

type Props = {redirect?:boolean};

export const UserLoginForm = ({redirect=true}: Props) => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("");

  const { loginMutation } = useLogin();

  const canSubmit = !loginMutation.isLoading && username.length > 0 && password.length > 0;

  const location = useLocation();
  const state = location.state as { from: { pathname: string } };
  const from = state?.from?.pathname;
  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (canSubmit) {
      try {
        await loginMutation.mutateAsync({ username, password });
        const authUser = await AuthService.getMe();
        queryClient.setQueryData(['authUser'], authUser);
        toast.success('You are logged in !');
        if (redirect) {
          if (from) {
            navigate(from, { replace: true });
          } else {
            navigate('/');
          }
        }
        
      } catch (err: any) {
        console.error(err?.message);
      }
    } else {
      toast.error("username and password are required !");
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="flex flex-col border-gray-700 max-w-[500px] min-w-[400px] bg-gray-300 p-5 rounded-lg mt-6"
    >
      <h1 className="font-[1000] text-3xl text-center">Please sign-in</h1>
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
        {loginMutation.isLoading ? (
          <Spinner white={true} size="sm" />
        ) : (
          "Login"
        )}
      </button>
      <Link
        to="/register"
        state={'ROLE_VOYAGEUR'}
        className="text-center underline hover:text-purple-800"
      >
        You don't have an account ?
      </Link>
    </form>
  );
};
