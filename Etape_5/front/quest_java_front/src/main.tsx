import ReactDOM from "react-dom/client";
import App from "./App";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { UserLoginPage } from "./pages/UserLoginPage/UserLoginPage";
import { UserRegisterPage } from "./pages/UserRegisterPage/UserRegisterPage";
import { UserListPage } from "./pages/UserListPage";
import { UserPage } from "./pages/UserPage";
import { QueryClient, QueryClientProvider } from "react-query";
import RequireAuth from "./components/RequireAuth";
import RequireNoAuth from "./components/RequireNoAuth";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
    },
  },
});

ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
  <QueryClientProvider client={queryClient}>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />}>
          {/* Public routes that are not accessible to authenticated users */}
          <Route element={<RequireNoAuth />}>
            <Route path="login" element={<UserLoginPage />} />
            <Route path="register" element={<UserRegisterPage />} />
          </Route>

          {/* Protected routes */}
          <Route element={<RequireAuth />}>
            <Route path="/" element={<UserListPage />} />
            <Route path="users" element={<UserListPage />} />
            <Route path="user/:id" element={<UserPage />} />
          </Route>
        </Route>
      </Routes>
    </BrowserRouter>
  </QueryClientProvider>
);
