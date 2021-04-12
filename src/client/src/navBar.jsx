import React from 'react';
import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Form from 'react-bootstrap/Form';
import FormControl from 'react-bootstrap/FormControl';
import Button from 'react-bootstrap/Button';

class MainNavbar extends React.Component {

    render() {

    return (
    <div className="TopNav">
               <Navbar bg="light" expand="lg">
  <Navbar.Brand href="/">Utopia Airlines</Navbar.Brand>
  <Navbar.Toggle aria-controls="basic-navbar-nav" />
  <Navbar.Collapse id="basic-navbar-nav">
    <Nav className="mr-auto">
      <Nav.Link href="/">Book Trip</Nav.Link>
      <Nav.Link href="/what-is-mutual-aid">Check-In</Nav.Link>
      <Nav.Link href="/resources">My Trips</Nav.Link>
      <Nav.Link href="/organizers">Flight Status</Nav.Link>
    </Nav>
    <Nav>
        <Nav.Link href="/login">Login</Nav.Link>
        <Nav.Link href="/signup">Register</Nav.Link>
    </Nav>
  </Navbar.Collapse>
</Navbar>
    </div>
    )
    }
}
export default MainNavbar;