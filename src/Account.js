import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';

const Account = ({
  allowExplicitContent,
  setAllowExplicitContent,
  age,
  setIsAgeEntered,
  handleClearData,
  handleLogout,
  playClickSound
}) => {
  // local state to track baby mode preference
  const [enableBabyMode, setEnableBabyMode] = useState(false);

  useEffect(() => {
    // load 'baby mode' preference from local storage
    const savedBabyModePreference = localStorage.getItem('babyMode');
    if (savedBabyModePreference) {
      setEnableBabyMode(JSON.parse(savedBabyModePreference));
    }
  }, []);

  const handleExplicitContentToggle = () => {
    setAllowExplicitContent(prevState => {
      const newState = !prevState;
      localStorage.setItem('allowExplicitContent', newState);
      return newState;
    });
    playClickSound(); 
  };

  const handleBabyModeToggle = () => {
    setEnableBabyMode(prevState => {
      const newState = !prevState;
      localStorage.setItem('babyMode', newState);
      return newState;
    });
    playClickSound();
  };

  const handleClearDataWrapper = () => {
    handleClearData(); // execute the passed down clear function
    setIsAgeEntered(false); // reset age entered status
  };

  return (
    <div>
      <p><b>Account Preferences</b></p>
      <p>★・・・・・・・・・・・・・・・・・・・・・・・・・・★</p>
      <div className="toggle-explicit">
        <label>
          <input
            type="checkbox"
            checked={allowExplicitContent}
            onChange={handleExplicitContentToggle}
            disabled={age < 18}
            style={{ opacity: age < 18 ? 0.5 : 1 }}
            aria-label="Allow Explicit Content"
          />
          Allow Explicit Content
        </label>
      </div>
      <div className="toggle-baby-mode" style={{ marginTop: '10px' }}>
        <label>
          <input
            type="checkbox"
            checked={enableBabyMode}
            onChange={handleBabyModeToggle}
            aria-label="Enable Baby Mode"
          />
          Enable Baby Mode
        </label>
      </div>
      <button className="clear-data-button" onClick={handleClearDataWrapper}>Clear Local Data</button>
      <button className="logout-button" onClick={handleLogout}>Logout</button>
    </div>
  );
};

Account.propTypes = {
  allowExplicitContent: PropTypes.bool.isRequired,
  setAllowExplicitContent: PropTypes.func.isRequired,
  age: PropTypes.number.isRequired,
  setIsAgeEntered: PropTypes.func.isRequired,
  handleClearData: PropTypes.func.isRequired,
  handleLogout: PropTypes.func.isRequired,
  playClickSound: PropTypes.func
};

export default Account;

