import React, {useEffect, useState} from 'react';
import {Observable} from "rxjs";
import { map } from "rxjs/operators";
import "./home.css";
import Header from './Header';
import Footer from "./Footer";
import { faPaw, faCat } from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";



function Home(){

    const [usernames, setUsernames] = useState([]);
    const [isGenerating, setGenerating] = useState(false);
    const pawIconsCount = 200;
    const max_visible_cards = 15;

    useEffect(() => {

        const userStream$ = new Observable(subscriber => {
            const eventSource = new EventSource("http://localhost:8084/cat-facts");

            eventSource.onmessage = (event) => {
                subscriber.next(event.data);
            };

            eventSource.onerror = (error) => {
                subscriber.error(error);
            };


            return () => {
                eventSource.close();
            };
        });


        const subscription = userStream$
            .pipe(

                map((data) => JSON.parse(data))
            )
            .subscribe({
                next: ({username, text}) => {
                    setGenerating(true);
                    setUsernames((prevUsernames) =>{

                       const newList =  [...prevUsernames, {username, text}]
                        if (newList.length > max_visible_cards) {
                            newList.shift();
                        }
                        return newList;
                    });

                },
                error: (err) => console.error("Błąd strumienia danych:", err),
            });


        return () => {
            subscription.unsubscribe();
            setGenerating(false);
        };
    }, []);




    return(

        <div>

            <Header/>
            <div className="hero">
                <div className="paw-icons">
                    {Array.from({ length: pawIconsCount }, (_, index) => (

                        <FontAwesomeIcon
                            key={index}
                            icon={faCat}
                            size="2x"
                            className={`paw-icon icon-${index % 10}`}
                        />


                ))}
            </div>
                <div className="blur-container">
                    <h2>Welcome to CatFacts App</h2>
                    <div className="typewriter">
                        <span className="typewriter-text"><p>Discover to fascinating World of Cats.</p></span>
                    </div>


                </div>

            </div>

            <div className="cat-intro-layout">

                <h3>Feline Fun Facts!
                    Get ready to learn some intriguing and surprising facts about our feline friends. <br/>Whether you're a cat lover or simply curious, we have something for everyone!</h3>
            </div>



            <div className="grid-layout">



                {usernames.map((username, index) => (
                    <div key={index} className="grid-card">
                        <p>by {username.username}</p>
                        <h3>Fact</h3>
                        <p>{username.text}</p>
                    </div>
                ))}

                {isGenerating && <p>Generating...</p>}
            </div>

            <Footer/>

        </div>

    );
}

export default Home;