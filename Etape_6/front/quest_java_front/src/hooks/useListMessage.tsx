import { AxiosError } from 'axios';
import React from 'react';
import { useQuery } from 'react-query';
import { useLocation, useParams } from 'react-router-dom';
import MessageService from '../services/MessageService';
import Message from '../types/Message';


const useListMessage = () => {
    const addressId = useParams().addressId as string;
    return useQuery<Message[], AxiosError>(["messages", parseInt(addressId)], () => MessageService.list(parseInt(addressId)), {
      onSuccess: () => {
        setTimeout(() => window.scrollTo(0, document.body.scrollHeight), 100)
      }
    });
}

export default useListMessage;