import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import ExperienceList from './components/ExperienceList.js';
import Header from './components/Header.js';
import RegistrationForm from './components/RegistrationForm.js';
import LoginForm from './components/LoginForm.js';
import AddExperienceForm from './components/AddExperienceForm.js';
import ProtectedRoute from './components/ProtectedRoute.js';
import { AuthProvider } from './contexts/AuthContextProvider.js';
import UserProfile from './components/UserProfile.js';

function App() {
    return (
      <AuthProvider>
        <div className='App'>
                <Router>
                    <Header />
                    <Routes>
                        <Route path="/" element={<Navigate to="/experiences" />} />
                        <Route path='/experiences' Component={ExperienceList}></Route>
                        <Route path='/register' Component={RegistrationForm}></Route>
                        <Route path='/login' Component={LoginForm}></Route>
                        <Route element={<ProtectedRoute />}>
                            <Route path="/add-experience" element={<AddExperienceForm />} />
                        </Route>
                        <Route path="/users/:id" element={<UserProfile />} />
                    </Routes>
                </Router>
        </div>
        </AuthProvider>
    );
}

export default App;
