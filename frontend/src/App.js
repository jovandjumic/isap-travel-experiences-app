import React from 'react';
import { BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import ExperiencesPage from './ExperiencePage.js';
import ExperienceList from './components/ExperienceList.js';

function App() {
    return (
    <div className='App'>
      <Router>
        <Routes>
            <Route path='/experiences' Component={ExperienceList}></Route>
        </Routes>
      </Router>
    </div>
    );
}

export default App;