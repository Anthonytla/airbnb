import { useLocation, useParams } from 'react-router-dom';
import { UserRegisterForm } from '../../components/UserRegisterForm';
import { UserRole } from '../../types/UserRole';

type Props = {}

export const UserRegisterPage = ({}: Props) => {
  const location = useLocation();
  const role = location.state as UserRole;
  return (
  <div className="flex justify-center">
    <UserRegisterForm defaultRole={role}/>
  </div>
  )
};