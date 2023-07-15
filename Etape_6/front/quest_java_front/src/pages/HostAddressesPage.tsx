import React from 'react';
import { AddressList } from '../components/AddressList';
import { PlaceCard } from '../components/PlaceCard';
import Spinner from '../components/Spinner/Spinner';
import useAddressList from '../hooks/useAddressList';
import useAuthUser from '../hooks/useAuthUser';
import useAuthUserCache from '../hooks/useAuthUserCache';
import Address from '../types/Address';
import { AuthUser } from '../types/AuthUser';

type Props = {}

export const HostAddressesPage = ({}: Props) => {
  const { data: user, isLoading } = useAuthUser();

  if (isLoading) {
    return <Spinner />;
  }
  if (user) {
    return (
      <>
        <h5>My propositions</h5>
        {<AddressList addresses={user.addresses} />}
      </>
    );
  } else {
    return null;
  }
};