import React from 'react';
import { BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import ExperienceList from './components/ExperienceList.js';
import Header from './components/Header.js';
import RegistrationForm from './components/RegistrationForm.js';
import LoginForm from './components/LoginForm.js';

function App() {
    return (
    <div className='App'>
      <Router>
      <Header />
        <Routes>
            <Route path='/experiences' Component={ExperienceList}></Route>
            <Route path='/register' Component={RegistrationForm}></Route>
            <Route path='/login' Component={LoginForm}></Route>
        </Routes>
      </Router>
    </div>
    );
}

export default App;