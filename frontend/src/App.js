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
import EditExperienceForm from './components/EditExperienceForm.js';
import EditUserProfileForm from './components/EditUserProfileForm.js';
import ChangePasswordForm from './components/ChangePasswordForm.js';
import ExperienceDetails from './components/ExperienceDetails.js';

function App() {
    return (
      <AuthProvider>
        <div className='App'>
                <Router>
                    <Header />
                    <Routes>
                        <Route path="/" element={<Navigate to="/experiences" />} />
                        <Route path='/experiences' element={
                          <div className="user-profile">
                            <ExperienceList />
                          </div>
                        }></Route>
                        <Route path='/register' Component={RegistrationForm}></Route>
                        <Route path='/login' Component={LoginForm}></Route>
                        <Route element={<ProtectedRoute />}>
                            <Route path="/add-experience" element={<AddExperienceForm />} />
                        </Route>
                        <Route path="/users/:id" element={<UserProfile />} />
                        <Route path="/experiences/edit/:id" element={<EditExperienceForm />} />
                        <Route path="/users/:id/edit" element={<EditUserProfileForm />} /> {/* Ruta za izmenu korisničkih podataka */}
                        <Route path="/change-password" element={<ChangePasswordForm />} />
                        <Route path="/experiences/:id" element={<div className="user-profile">
                            <ExperienceDetails />
                          </div>} />
                    </Routes>
                </Router>
        </div>
        </AuthProvider>
    );
}

export default App;
