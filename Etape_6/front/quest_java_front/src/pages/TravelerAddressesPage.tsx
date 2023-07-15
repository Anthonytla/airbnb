import React from 'react';
import { AddressList } from '../components/AddressList';
import { PlaceCard } from '../components/PlaceCard';
import { ReservationList } from '../components/ReservationList';
import useReservationUserList from '../hooks/useReservationUserList';

type Props = {}

export const TravelerAddressesPage = ({}: Props) => {
  const { data:reservations } = useReservationUserList();
  return (
  <>
    {reservations ? <ReservationList reservations={reservations}  /> : "You haven't any reservations"}
  </>
)}