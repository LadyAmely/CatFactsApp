import React, {useEffect, useState} from 'react';
import {Observable} from "rxjs";
import { map } from "rxjs/operators";
import "./home.css";


function Home(){

    const [usernames, setUsernames] = useState([]);

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
        <div className="grid-layout">

            {usernames.map((username, index) => (
                <div key={index} className="grid-card">
                    <p>by {username.username}</p>
                </div>
            ))}


            <div className="grid-card">

                <h1>Hello</h1>
            </div>


        </div>
    );
}

export default Home;