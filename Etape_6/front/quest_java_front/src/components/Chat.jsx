//import { Stomp } from "@stomp/stompjs";
import { useEffect, useState } from "react";
import { useQueryClient } from "react-query";
import useAddressDetails from "../hooks/useAddressDetails";
import useAuthUserCache from "../hooks/useAuthUserCache";
import Feed from "./Feed";
import { Input } from "./Input/Input";

const Chat = ({ address }) => {
  const client = useQueryClient();
  //const client = useWebSocket();
  const [message, setMessage] = useState("");
  const [stompClient, setStompClient] = useState(null);
  const [isConnected, setIsConnected] = useState(false);
  const [subs, setSubs] = useState(null);
  const authUser = useAuthUserCache();
 
  const getMessage = (data) => {
    console.log("new message : ", data.body, address);
    client.setQueryData(["messages", address.id], (oldMessages) => {
      if (!oldMessages) {
        return [JSON.parse(data.body)];
      }
      oldMessages = oldMessages.filter(
        (message) => message.id != JSON.parse(data.body).id
      );
      return [...oldMessages, JSON.parse(data.body)];
    });
    setTimeout(() => window.scrollTo(0, document.body.scrollHeight), 100);
  };

  useEffect(() => {
    console.log("isConnecetd : ", isConnected);

    if (!isConnected) {
      // const socket = new SockJS("ws://localhost:8090/chat");
      const url = "http://localhost:8090/chat-example";
      const socket = new SockJS(url);

      setStompClient(Stomp.over(socket));

      if (stompClient) {
        setIsConnected(true);
        stompClient.connect(
          {},
          () => {
            setSubs(
              stompClient.subscribe("/topic/public/" + address.id, getMessage)
            );
          },
          () => {
            setIsConnected(false);
          }
        );
      }
    }
    return () => {
      if (stompClient && isConnected && subs) {
        subs.unsubscribe();
        stompClient.disconnect();
      }
    };
  }, [stompClient, subs]);
  console.log("subs", subs);
  if (address) {
    return (
      <>
        <Feed address={address} />
        <div className="fixed bottom-0 flex py-4 bg-grey-transparent w-full border border-gray-500 justify-center">
          <form
            className="flex items-end"
            onSubmit={(e) => {
              e.preventDefault();
              console.log("sending... ", isConnected);
              stompClient.send(
                "/app/chat/" + address.user.username,
                {},
                JSON.stringify({
                  fromHost: authUser ? authUser.username : "Anonymous",
                  text: message,
                  address: address,
                })
              );
              setMessage("");
            }}
          >
            <textarea
              rows={5}
              cols={50}
              value={message}
              onChange={(e) => {
                setMessage(e.target.value);
              }}
              className="border border-gray-700 whitespace-pre-wrap rounded-md mr-2"
            />
            <button type="submit" className="btn">
              Send
            </button>
          </form>
        </div>
      </>
    );
  } else return null;
};

export default Chat;
