import "./index.css";
import { Outlet } from "react-router";
import { Toaster } from "react-hot-toast";
import useAuthUser from "./hooks/useAuthUser";
import { useEffect } from "react";
import axios from "./api/axios";
import Spinner from "./components/Spinner/Spinner";
import Navbar from "./components/Navbar";

function App() {
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      axios.defaults.headers.common["Authorization"] = "Bearer " + token;
    }
  }, []);

  const { data: authUser, isLoading } = useAuthUser();

  if (isLoading) {
    return (
      <div className="mt-6">
        <Spinner />
      </div>
    );
  }

  return (
    <div className="App h-full">
      <Navbar authUser={authUser} />
      <Outlet />
      <Toaster />
    </div>
  );
}

export default App;
