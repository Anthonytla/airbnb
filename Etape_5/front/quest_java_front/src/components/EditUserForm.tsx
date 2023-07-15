import useEditUser from "../hooks/useEditUser";
import User from "../types/User";
import Spinner from "./Spinner/Spinner";
import { useForm } from "react-hook-form";
import { AuthUser } from "../types/AuthUser";

interface Props {
  user: User;
  setShowModal: (show: boolean) => void
}

const EditUserForm = ({ user, setShowModal }: Props) => {
  const { username, role } = user;
  const mutation = useEditUser();
  const { register, handleSubmit, formState, reset } = useForm({
    defaultValues: {
      username,
      role,
    }
  });

  const canSubmit = true;

  const onSubmit = (formData: AuthUser) => {
    mutation.mutate(formData, {
      onSuccess: () => {
        setShowModal(false);
        if (formState.isSubmitSuccessful) reset();
      }
    });
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="flex flex-col max-w-[500px] min-w-[400px] rounded-lg mt-6"
    >
      <h1 className="font-[1000] text-3xl text-center">
        Edit this user profile
      </h1>
      <div className="flex flex-col my-2">
        <label className="text-left">Username</label>
        <input
          type="text"
          {...register("username", { required: true })}
          className="border border-gray-700 rounded-md h-8 mt-1 px-2"
        />
      </div>
      <div className="flex flex-col my-2">
        <label className="text-left">Role</label>
        <select
          {...register("role")}
          className="border border-gray-700 rounded-md h-8 mt-1 px-2"
        >
          <option value="ROLE_ADMIN">Admin</option>
          <option value="ROLE_USER">User</option>
        </select>
      </div>
      <button
        className={`mx-auto ${
          canSubmit
            ? "bg-blue-500 hover:bg-blue-600 hover:scale-105"
            : "bg-slate-400"
        } rounded max-w-[30%] p-2 text-white my-2 transition ease-in-out delay-100 duration-250`}
        type="submit"
        disabled={!canSubmit}
      >
        {mutation.isLoading ? <Spinner white={true} size="sm" /> : "Confirm"}
      </button>
    </form>
  );
};

export default EditUserForm;
