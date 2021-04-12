import logo from './logo.svg';
import './App.css';
import MainNavbar from './navBar.jsx';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'semantic-ui-css/semantic.min.css';
import { Input, Button, Icon } from 'semantic-ui-react';
import Image from 'react-bootstrap/Image';
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import React, { useState } from "react";

function App() {

    const [startDate, setStartDate] = useState(new Date());
    const [startDate2, setStartDate2] = useState(new Date());


  return (
    <div className="App">
      <MainNavbar />
      <Image src="http://content.delta.com/content/dam/delta-tnt/homepage/hero/9/peaceful-valley-ak-1600.jpg" fluid />
          <br /><br />
          <div className="ui search">
            <Input label="FROM:" placeholder='Enter City...' />

            <Input label="TO:" placeholder='Enter City...' />

            <Button icon labelPosition='right'>
                  Search Flights
                  <Icon name='right arrow' />
                </Button><br /><br />
                Depart: <DatePicker selected={startDate} onChange={date => setStartDate(date)} />
                Return: <DatePicker selected={startDate2} onChange={date => setStartDate2(date)} />
                <br /><br />
            </div>
    </div>
  );
}

export default App;
