import React, { useState, useEffect } from 'react';
import './App.css';
import spotifyLogo from './visual/spotify_kigo.png'
import clickNoise from './audio/click-noise.wav';
import Account from './Account';

function App() {
  // declare functions and variables 
  const [step, setStep] = useState(0); // current step in the user flow
  const [showWelcomeText, setShowWelcomeText] = useState(true); // controls welcome page visibility
  const [showContactInfo, setShowContactInfo] = useState(false); // controls contact info page visibility
  const [showAccount, setShowAccount] = useState(false); // controls account info page visibility
  const [allowExplicitContent, setAllowExplicitContent] = useState(false); // user preference for explicit content
  const [showAbout, setShowAbout] = useState(false); // controls about page visibility
  const [showHome, setShowHome] = useState(true); // controls homepage visibility 
  const [isSpotifyConnected, setIsSpotifyConnected] = useState(false); // state to track if spotify is connected
  const [displayName, setDisplayName] = useState(''); // state to store spotify account name 
  const [profileImage, setProfileImage] = useState(''); // state to store spotify profile image 
  const [currentHaiku, setCurrentHaiku] = useState(null); // state to store current haiku to be displayed
  
  // get stored data from user 
  useEffect(() => {	
	const savedPreference = localStorage.getItem('allowExplicitContent');
	if (savedPreference) {
		setAllowExplicitContent(JSON.parse(savedPreference));
	}
	
	// extract spotify account name
	const params = new URLSearchParams(window.location.search);
	const name = params.get('displayName');
	if (name) {
		setDisplayName(name);
		setShowWelcomeText(false);
		setIsSpotifyConnected(true);
		setStep(2); // after authenticating, move to haiku generation page 
	}
	
	const picture = params.get('profilePicture');
	if (picture) {
	   setProfileImage(picture);
	 }
	
	// extract spotify profile image 
	const image = params.get('profileImage');
	
  }, []);
  
  // function for when user clicks on the "home" button
  const handleHomeClick = () => {
	playClickSound();
	// show welcome page, hide others 
	setShowWelcomeText(true);
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
	setShowContactInfo(false);
	setShowHome(false);
	setStep(0);
  };
  
  // handles button for user to clear their local data
  const handleClearData = () => {
    localStorage.clear(); 
    setStep(0); 
	setAllowExplicitContent(false);
    setShowWelcomeText(true);
    alert("All local data has been cleared!");
  };
  
  // function for when user clicks on the "ok" button
  const handleOkClick = () => {
  	playClickSound();
    setStep(1); // show spotify prompt
	// clear other pages 
    setShowWelcomeText(false); 
    setShowContactInfo(false);
	setShowHome(false);
	if (isSpotifyConnected){
		setStep(2);
	}
  };
  
  // function for when user clicks on the "connect spotify" button
  const handleConnectSpotifyClick = () => {
	setStep(2);
	setShowWelcomeText(false);
	setDisplayName(displayName);
	setIsSpotifyConnected(true);
	
	playClickSound();
	setIsSpotifyConnected(true);
	window.location.href = 'https://kigo-app.glitch.me/login'; // redirect to /login route
  };

  // function for when the user clicks on the "generate haiku" button
  const handleGenerateHaikuClick = async () => {
    playClickSound();

    // for debugging 
    try {
    const response = await fetch('https://kigo-app.glitch.me/generate-haiku');
      if (response.ok) {
        const data = await response.json(); // parse the JSON response
        if (data.success) {
          console.log(data.message);
		  await handleFetchAndDisplayHaiku();
		  setStep(3);
        } else {
          console.error(`Backend error: ${data.message}`);
        }
      } else {
        console.error('Failed to generate haikus. HTTP status:', response.status);
      }
    } catch (error) {
      console.error('Error connecting to the backend:', error);
    }
  };
  
  // function to fetch haikus from backend to be displayed
  const handleFetchAndDisplayHaiku = async () => {
    try {
      const response = await fetch('https://kigo-app.glitch.me/get-haikus');
      if (response.ok) {
        const data = await response.json();
        if (data.success) {
          const haikusArray = Object.values(data.haikus); // convert haikus object to an array
          if (haikusArray.length > 0) {
            // select a random haiku from the array
            const randomHaiku = haikusArray[Math.floor(Math.random() * haikusArray.length)];
            setCurrentHaiku(randomHaiku);
          } else {
            alert('No haikus available. Generate haikus first!');
          }
        } else {
          alert('Failed to fetch haikus.');
        }
      } else {
        alert('Error connecting to the backend.');
      }
    } catch (error) {
      console.error('Error fetching haikus:', error);
      alert('Error connecting to the backend.');
    }
  };

  // function for when the user logs out of their account 
  const handleLogout = () => {
	playClickSound();
	// reset all variables and display welcome text 
	setShowWelcomeText(true);
	setShowContactInfo(false);
  };

  // function for when the user clicks on the "about" button
  const handleAboutClick = () => {
	playClickSound();
	setStep(0); 
	// show about page, hide others 
	setShowAbout(true);
    setShowWelcomeText(false);
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
						handleClearData={handleClearData}
						handleLogout={handleLogout}
						playClickSound={playClickSound}
						profileImage={profileImage}
						displayName={displayName}
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
					{step === 1 && (
					  <div className="spotify-prompt">
					  <p><b>Let's get started!</b></p>
					  <p> Please click the button below to authenticate your Spotify account </p>
					    <img src={spotifyLogo} alt="Connect Spotify" className="spotify-image" />
						<p>   </p>
						<button className="connect-spotify-button" onClick={handleConnectSpotifyClick}>
						  Connect Spotify
						</button>
					  </div>
					)}
					{step === 2 && (
						<div className="spotify-confirmation-message">
							<p>Thank you for connecting your Spotify account, <b>{displayName}!</b></p>
							<p> </p>
							<p>We've now gathered your most-listened songs. All you have to do is click the button below to start haiku generation!!</p>
							<button className="generate-haiku-button" onClick={handleGenerateHaikuClick}>
							Generate Haiku
							</button>
						</div>
					)}
					{step === 3 && (
						<div className="haiku-display">
							<pre className="haiku-text">{currentHaiku}</pre>
						<button className="generate-another-button" onClick={handleGenerateHaikuClick}>
						Generate Another
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
							<p>At the moment, Kigo is only compatible with Spotify and is designed exclusively for desktop use. Our team plans to expand its functionality by integrating with other music streaming services, such as SoundCloud and Apple Music, and implementing responsive web design to ensure compatibility across devices in the near future.</p>
							<p>Kigo is open-source software licensed under the MIT License. This means you are free to view, modify, and distribute the source code with minimal restrictions, as long as the original copyright and permission notice are included in all copies or substantial portions of the software. If you wish to explore the code, contribute, or use Kigo in your projects, please visit our GitHub repository to view our contribution guidelines.</p>
							<p>Lastly, if you have suggestions for improvement, you are welcome to create an issue within our GitHub repository. For the link to our repository as well other methods to contact us, please click the <b>"Contact Us"</b> button down below!</p>	
						</div>
					)}
				  </>
				)}
              </div>
            </div>
            <div className="smallbox"> 
              <button className="small-button0" onClick={handleAboutClick}>About</button>
              <button className="small-button1" onClick={handleContactClick}>Contact Us</button>
            </div>
          </div>
          <div className="box box2"> 
            <button className="big-button1" onClick={handleHomeClick}>Home ⭑.ᐟ</button>
            <button className="big-button2" onClick={handleAccountClick}>Account ⭑.ᐟ</button>
            <button className="big-button3" onClick={playClickSound}>Haiku Gallery ⭑.ᐟ</button>
          </div>
        </div>
      </main>
    </div>
  );
}

export default App;
