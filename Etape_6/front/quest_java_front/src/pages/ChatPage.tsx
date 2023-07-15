import { io } from "socket.io-client";

import React, { useEffect } from 'react';
import axios from "../api/axios";
import { Stomp } from "@stomp/stompjs";
import { useLocation, useParams } from "react-router-dom";
import Address from "../types/Address";
import Chat from "../components/Chat";
import useAddressDetails from "../hooks/useAddressDetails";

const ChatPage = () => {  
  const { data: address, isLoading } = useAddressDetails();
  if (address)
  { return <Chat address={address}/> };
  return null;
};

export default ChatPage;