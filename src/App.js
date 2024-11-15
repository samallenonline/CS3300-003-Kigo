import React, { useState, useEffect } from 'react';
import './App.css';
import spotifyLogo from './visual/spotify_kigo.png'
import clickNoise from './audio/click-noise.wav';
import Account from './Account';

function App() {
  // declare functions and variables 
  const [step, setStep] = useState(0); // current step in the user flow
  const [age, setAge] = useState('');  // user's age as a string
  const [showAgeInput, setShowAgeInput] = useState(false); // controls age input page visibility
  const [showWelcomeText, setShowWelcomeText] = useState(true); // controls welcome page visibility
  const [showContactInfo, setShowContactInfo] = useState(false); // controls contact info page visibility
  const [showAccount, setShowAccount] = useState(false); // controls account info page visibility
  const [allowExplicitContent, setAllowExplicitContent] = useState(false); // user preference for explicit content
  const [showAbout, setShowAbout] = useState(false); // controls about page visibility
  const [showHome, setShowHome] = useState(true); // controls homepage visibility 
  const [isAgeEntered, setIsAgeEntered] = useState(false);
  const [isSpotifyConnected, setIsSpotifyConnected] = useState(false);
  
  // get stored data from user 
  useEffect(() => {
	const storedAge = localStorage.getItem('userAge');
	if (storedAge) {
		setAge(storedAge);
	}
	
	const savedPreference = localStorage.getItem('allowExplicitContent');
	if (savedPreference) {
		setAllowExplicitContent(JSON.parse(savedPreference));
	}
  }, []);
  
  // function for when user clicks on the "home" button
  const handleHomeClick = () => {
	playClickSound();
	// show welcome page, hide others 
	setShowWelcomeText(true);
	setShowAgeInput(false);
	setShowContactInfo(false);
	setShowAccount(false);
	setShowAbout(false);
	setStep(0);
  }
  
  // function for when user clicks on the "account" button
  const handleAccountClick = () => {
	playClickSound();
	// show account page, hide others
	setShowAccount(true);
	setShowWelcomeText(false);
	setShowAgeInput(false);
	setShowContactInfo(false);
	setShowHome(false);
	setStep(0);
  };
  
  // handles button for user to clear their local data
  const handleClearData = () => {
    localStorage.clear(); 
    setAge('');
    setStep(0); 
	setAllowExplicitContent(false);
	setIsAgeEntered(false);
    setShowAgeInput(false);
    setShowWelcomeText(true);
    alert("All local data has been cleared!");
  };
  
  // function for when user clicks on the "ok" button
  const handleOkClick = () => {
  	playClickSound();
	if (isAgeEntered) {
	    setStep(2);
		setShowAgeInput(false);
		setShowWelcomeText(false); 
		setShowContactInfo(false);
		setShowHome(false);
	}
	else {
    setStep(1); // show age input
    setShowAgeInput(true);
	// clear other pages 
    setShowWelcomeText(false); 
    setShowContactInfo(false);
	setShowHome(false);
	}
  };
  
  const handleAgeSubmitClick = () => {
	// validate user age 
	const parsedAge = parseInt(age, 10);
	if (isNaN(parsedAge) || parsedAge < 1 || parsedAge > 120) {
	    alert("Please enter a valid age between 1 and 120.");
	    return; // do not proceed if age is invalid 
	}
	setStep(2); // display confirmation message 
	setShowAgeInput(false);
	setIsAgeEntered(true);
	localStorage.setItem('userAge', age); // store user age in local storage
  }
  
  const handleConnectSpotifyClick = () => {
	alert("Now you can connect your Spotify account!"); // prompt user to connect spotify account
  }

  // function for when the user logs out of their account 
  const handleLogout = () => {
	// reset all variables and display welcome text 
	localStorage.removeItem('userAge');
	setAge('');
	setShowWelcomeText(true);
	setShowAgeInput(false);
	setShowContactInfo(false);
  };
  
  const handleAgeSubmit = (e) => {
    setAge(e.target.value);
  };

  // function for when the user clicks on the "about" button
  const handleAboutClick = () => {
	playClickSound();
	setStep(0); 
	// show about page, hide others 
	setShowAbout(true);
    setShowWelcomeText(false);
    setShowAgeInput(false);
	setShowAccount(false);
	setShowContactInfo(false);
  };
  
  // function for when the user clicks on the "contact" button
  const handleContactClick = () => {
	playClickSound();
	setStep(0);
	// show contact page, hide others 
	setShowContactInfo(true);
	setShowWelcomeText(false);
	setShowAgeInput(false);
	setShowAccount(false);
	setShowAbout(false);	
  };
  
  const clickSound = new Audio(clickNoise);

  // function that plays sound whenever the user clicks on a certain component 
  const playClickSound = () => {
    clickSound.currentTime = 0; // reset  to beginning of audio file 
    clickSound.play().catch((error) => {
      console.error("Audio playback failed:", error); // log playback errors if exists 
    });
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Kigo</h1> {/* Main title */}
      </header>
      <main>
        <div className="box-container">
          <div className="box-group">
            <div className="box box1">
              <div className="inner-box">
			  	{showAccount ? (
					<Account 
						allowExplicitContent={allowExplicitContent}
						setAllowExplicitContent={setAllowExplicitContent}
						age={age}
						setIsAgeEntered={setIsAgeEntered}
						handleClearData={handleClearData}
						handleLogout={handleLogout}
						playClickSound={playClickSound}
					/>
				) : (
				<>
	                <div className="title">
	                  {showWelcomeText && (
	                    <p>.𖥔 ݁ ˖๋ ࣭ ⭑ <b>Welcome to Kigo!</b> .𖥔 ݁ ˖๋ ࣭ ⭑</p>
	                  )}
	                </div>
	                {showWelcomeText && (
	                  <div className="text-block1">
	                    <p>Kigo is a haiku generator that creates haikus from the lyrics of your most-listened songs. All you have to do is sign in with your Spotify account!</p>
						<p><b>How does it work?</b></p>
						<p>After you sign into your Spotify account, Kigo will use the Spotify API to gather information from your listening history and identify your most-listened songs. The Genius API is then used to gather lyrical content from these songs. From there, Kigo uses several algorithms to parse these lyrics and generate a fun and personalized haiku!</p>
					  </div>
	                )}
					{step === 0 && showWelcomeText && (
					  <button className="ok-button" onClick={handleOkClick}>
					    OK
					  </button>
					)}
					{step === 1 && showAgeInput && (
						<div className="age-input">
						  <p><b>Please enter your age:</b></p>
						<p>Kigo requires that you enter you age so that we can manage explicit content. If you are under 18, lyrics that contains explicit content will not be used in haiku generation. Otherwise, you will be allowed to toggle the "Allow explicit content" setting in the <b>"Preferences"</b> menu.</p>
						  <input
						    type="text"
						    value={age}
						    onChange={handleAgeSubmit}
						  className="age-input-box"
						  />
						  <p>   </p>
						  <button className="submit-age-button" onClick={handleAgeSubmitClick}>
						    Submit Age
						  </button>
						</div>
					)}
					{step === 2 && (
					  <div className="confirmation-message">
					    <p>Thank you for entering your age!</p>
					    <img src={spotifyLogo} alt="Connect Spotify" className="spotify-image" />
						<p>   </p>
						<button className="connect-spotify-button" onClick={handleConnectSpotifyClick}>
						  Connect Spotify
						</button>
					  </div>
					)}
					{showContactInfo && (
						<div className="contact-info-title">
						<p><b>Contact Us</b></p>
						<p>★・・・・・・・・・・・・・・・・・・・・・・・・・・★</p>
						<p>If you wish to reach out to the Kigo team, feel free to email us at pulsar3k@gmail.com.</p>
						<p>Our <a href="https://github.com/samallenonline/CS3300-003-Kigo" target="_blank" rel="noopener noreferrer">GitHub repository</a>  contains our source code, contribution guidelines, and comprehensive documentation to help you get started with Kigo or contribute to our project</p>
						<p>If you have any feature requests or suggestions for improvement, please create an issue on our GitHub page. We welcome all feedback and appreciate your support in making Kigo better!</p>
						</div>
					)}
					{showAbout && (
						<div>
						<p><b>About Kigo</b></p>
						<p>★・・・・・・・・・・・・・・・・・・・・・・・・・・★</p>
							<p>At the moment, Kigo is only compatible with Spotify. Our team may consider integrating functionality with other music streaming services such as SoundCloud and Apple Music sometime in the future.</p>
							<p>Kigo is open-source software licensed under the GNU General Public License (GPL). This means you are free to view, modify, and distribute the source code, provided that any modifications or redistributed versions also remain under the GPL. If you wish to explore the code, contribute, or use Kigo in your projects, please visit our GitHub repository to view our contribution guidelines.</p>
							<p>Lastly, if you have suggestions for improvement, you are welcome to create an issue within our GitHub repository. For the link to our repository as well other methods to contact us, please click the <b>"Contact Us"</b> button down below!</p>	
						</div>
					)}
				  </>
				)}
              </div>
            </div>
            <div className="smallbox"> 
			  <button className="small-button0" onClick={handleHomeClick}>Home</button>
              <button className="small-button1" onClick={handleAboutClick}>About</button>
              <button className="small-button2" onClick={handleContactClick}>Contact Us</button>
            </div>
          </div>
          <div className="box box2"> 
            <button className="big-button1" onClick={handleAccountClick}>Account ⭑.ᐟ</button>
            <button className="big-button2" onClick={playClickSound}>Haiku Gallery ⭑.ᐟ</button>
            <button className="big-button3" onClick={playClickSound}>Other ⭑.ᐟ</button>
          </div>
        </div>
      </main>
    </div>
  );
}

export default App;
