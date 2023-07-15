import { Stomp } from "@stomp/stompjs";
import { useEffect } from "react";


const useWebSocket = () => {
    var stompClient : any = null;
    useEffect(() => {
      const socket = new WebSocket('ws://localhost:8090/chat');
      stompClient = Stomp.over(socket);
      stompClient.connect({}, () => {
        console.log("CONNECT");
        stompClient.subscribe("http://localhost:8090/topic/messages");
        
        alert("hello");
      })
    }, []);
    return stompClient;
}

export default useWebSocket;