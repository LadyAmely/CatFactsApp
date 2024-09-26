import React, {useEffect, useState} from 'react';
import {Observable} from "rxjs";
import { map } from "rxjs/operators";
import "./home.css";
import Header from './Header';
import Footer from "./Footer";
import { faPaw } from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";


function Home(){

    const [usernames, setUsernames] = useState([]);
    const pawIconsCount = 200;

    useEffect(() => {

        const userStream$ = new Observable(subscriber => {
            const eventSource = new EventSource("http://localhost:8084/user/stream");

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
                next: (username) => {
                    setUsernames((prevUsernames) => [...prevUsernames, username]);
                },
                error: (err) => console.error("Błąd strumienia danych:", err),
            });


        return () => {
            subscription.unsubscribe();
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
                            icon={faPaw}
                            size="2x"
                            className={`paw-icon icon-${index % 10}`}
                        />
                    ))}
                </div>
                <div className="blur-container">
                    <h2>Welcome to CatFacts App</h2>
                    <p>Discover to fascinating World of Cats.</p>


                </div>

            </div>
            <div className="grid-layout">

                {usernames.map((username, index) => (
                    <div key={index} className="grid-card">
                        <p>by {username.username}</p>
                    </div>
                ))}




            </div>

            <Footer/>

        </div>

    );
}

export default Home;