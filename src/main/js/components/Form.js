import React, { Component } from "react";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";

class Form extends Component {
  state = {
    value: ""
  };
  handleChange = event => {
    this.setState({
      value: event.target.value
    });
  };
  render() {
    const { value } = this.state;
    return (
      <div className="form">
        <TextField
          id="name"
          label={this.props.name}
          value={this.state.value}
          onChange={this.handleChange}
          margin="normal"
        />
        <div className="button">
          <Button
            className={"button"}
            variant="contained"
            onClick={() => this.props.postRequest(value)}
          >
            POST
          </Button>
        </div>
        <div className="button">
          <Button
            className={"button"}
            variant="contained"
            color="primary"
            onClick={() => this.props.wsRequest(value)}
          >
            Websocket send
          </Button>
        </div>
      </div>
    );
  }
}

export default Form;
