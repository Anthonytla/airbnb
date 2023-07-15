import { useEffect } from "react";
import useAuthUserCache from "../hooks/useAuthUserCache";
import useListMessage from "../hooks/useListMessage";
import Address from "../types/Address";

interface Props {
  address: Address;
}

const Feed = ({ address }: Props) => {
  const { data: messages, isLoading } = useListMessage();

  
  const authUser = useAuthUserCache();

  if (isLoading) return "loading...";

  if (messages) {
    return (
      <div className="mx-40 pb-32 whitespace-pre-wrap">
        {messages.length > 0
          ? messages.map((msg) => (
              <div
                key={msg.id}
                className={`rounded-xl my-6 max-w-md p-3 ${
                  authUser && authUser?.username === msg.fromHost
                    ? "ml-auto mb-12 bg-gray-300"
                    : "bg-blue-300 mb-12"
                }`}
              >
                <div
                  className={
                    msg?.fromHost === address?.user.username
                      ? "text-blue-500 text-right"
                      : "text-green-300"
                  }
                >
                  {msg.fromHost || "anonymous"}
                </div>
                <div
                  className={
                    msg?.fromHost === address?.user.username
                      ? "text-left"
                      : "text-right"
                  }
                >
                  {msg.text}
                </div>
              </div>
            ))
          : "no messages yet"}
      </div>
    );
  } else return null;
};

export default Feed;
