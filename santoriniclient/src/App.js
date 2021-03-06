import logo from './logo.svg';
import './App.css';
import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";

function App() {
  let stompClient = null;

  const setConnected = (connected) => {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility
        = connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML = '';
  }

  const connect = () => {
    var socket = new SockJS('http://localhost:8080/api/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
      setConnected(true);
      console.log('Connected: ' + frame);
      stompClient.subscribe('/topic/messages', function(messageOutput) {
        console.log("Received message: " + messageOutput);
        showMessageOutput(JSON.parse(messageOutput.body));
      });
    });
  }

  const disconnect = () => {
    if(stompClient != null) {
      stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
  }

  const sendMessage = () => {
    var from = document.getElementById('from').value;
    var text = document.getElementById('text').value;

    console.log(stompClient);
    stompClient.send("/api/app/chat", {},
        JSON.stringify({'from':from, 'text':text}));
  }

  const showMessageOutput = (messageOutput) =>  {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(messageOutput.from + ": "
        + messageOutput.text + " (" + messageOutput.time + ")"));
    response.appendChild(p);
  }

  return (
    <div className="App">
      <body onLoad="disconnect()">
      <div>
        <div>
          <input type="text" id="from" placeholder="Choose a nickname"/>
        </div>
        <br/>
        <div>
          <button id="connect" onClick={connect}>Connect</button>
          <button id="disconnect" disabled="disabled" onClick={disconnect}>
            Disconnect
          </button>
        </div>
        <br/>
        <div id="conversationDiv">
          <input type="text" id="text" placeholder="Write a message..."/>
          <button id="sendMessage" onClick={sendMessage}>Send</button>
          <p id="response"></p>
        </div>
      </div>

      </body>
    </div>
  );
}

export default App;
