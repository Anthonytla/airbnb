import ReactDOM from "react-dom/client";
import App from "./App";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { UserLoginPage } from "./pages/UserLoginPage/UserLoginPage";
import { UserRegisterPage } from "./pages/UserRegisterPage/UserRegisterPage";
import { UserListPage } from "./pages/UserListPage";
import { UserPage } from "./pages/UserPage";
import { QueryClient, QueryClientProvider } from "react-query";
import RequireAuth from "./components/RequireAuth";
import RequireNoAuth from "./components/RequireNoAuth";
import HomePage from "./pages/HomePage";
import AddressDetailsPage from "./pages/AddressDetailsPage";
import { HostAddressesPage } from "./pages/HostAddressesPage";
import { TravelerAddressesPage } from "./pages/TravelerAddressesPage";
import { UserRoleProvider } from "./providers/useUserRole";
import ChatPage from "./pages/ChatPage";
import AdminGuard from "./components/AdminGuard";
import OwnerOrAdminGuard from "./components/OwnerOrAdminGuard";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
      refetchOnWindowFocus: false,
    },
  },
});

ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
  <QueryClientProvider client={queryClient}>
    <UserRoleProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<App />}>
            {/* Routes that are fully accessible */}
            <Route index element={<HomePage />} />
            <Route path="address/:addressId" element={<AddressDetailsPage />} />

            {/* Public routes that are not accessible to authenticated users */}
            <Route element={<RequireNoAuth />}>
              <Route path="login" element={<UserLoginPage />} />
              <Route path="register" element={<UserRegisterPage />} />
            </Route>

            {/* Protected routes */}
            <Route element={<RequireAuth />}>
              <Route path="host/propositions" element={<HostAddressesPage />} />
              <Route
                path="traveler/reservations"
                element={<TravelerAddressesPage />}
              />
              <Route path="address/:addressId/chat" element={<ChatPage />} />
            </Route>

            <Route path="admin/" element={<AdminGuard />}>
              <Route path="users" element={<UserListPage />} />
            </Route>

            <Route element={<OwnerOrAdminGuard />}>
              <Route path="user/:id" element={<UserPage />} />
            </Route>

            <Route path="*" element={<Navigate to="/" />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </UserRoleProvider>
  </QueryClientProvider>
);
