import React from 'react';

const Header = () => {
    return (
        <header style={{ display: 'flex', justifyContent: 'space-between', padding: '10px 20px', backgroundColor: '#f5f5f5' }}>
            <div>
                <img src="/logo.png" alt="Site Logo" style={{ height: '40px' }} />
            </div>
            <div>
                <button style={{ margin: '0 10px' }}>Dodaj Iskustvo</button>
                <button style={{ margin: '0 10px' }}>Prijava</button>
                <button style={{ margin: '0 10px' }}>Registracija</button>
            </div>
        </header>
    );
};

export default Header;