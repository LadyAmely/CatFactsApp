import React from 'react';
import "./header.css";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCat } from '@fortawesome/free-solid-svg-icons';

function Header(){


    return (

        <header>

            <div className="logo">CatFacts <FontAwesomeIcon icon={faCat} size="1x" /></div>


        </header>
    );

}

export default Header;