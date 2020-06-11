import React, { Component } from "react";
import Form from "./Form";
import { wsHost, httpHost, port } from "../urls";

const FORM_TYPES = ["time range", "target place"];

class App extends Component {
  state = {
    name: "kek"
  };
  socket;
  postUrl = `${httpHost}:${port}`;

  postRequest = type => text => {
    console.log(type);
    console.log(text);
    fetch(this.postUrl, {
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json"
      },
      method: "POST",
      body: JSON.stringify({ [type]: text })
    }).then(response => console.log(response));
  };
  wsRequest = type => text => {
    const { socket } = this;
    socket.send(JSON.stringify({ [type]: text }));
  };

  componentDidMount() {
    this.socket = new WebSocket(`${wsHost}:${port}`);
    const { socket } = this;
    socket.addEventListener("open", function(event) {
      socket.send("Hello Server!");
    });

    // Listen for messages
    socket.addEventListener("message", function(event) {
      console.log("Message from server ", event.data);
    });
  }

  componentWillUnmount() {
    WebSocket.close(1000);
  }

  render() {
    return (
      <div className="App">
        {FORM_TYPES.map(type => (
          <Form
            key={type}
            name={type}
            postRequest={this.postRequest(type)}
            wsRequest={this.wsRequest(type)}
          />
        ))}
      </div>
    );
  }
}

export default App;
