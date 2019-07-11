import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import Dossier from './Dossier';
import Example from './Example';
//import DisplayRemoteData from './DisplayRemoteData';
import './App.css';
import './Config';


function Index() {
    return (
        <div className="app">
            <Link to="/dossiers/teststorekey/testmodel/TEST">TEST DOSSIER</Link>
            <Link to="/dossiers/teststorekey/testmodel/TEST2">TEST DOSSIER2</Link>
        </div>
    );
}


function DossierIndex( { match }) {
    console.log(match.params);
    return (
        <div className="app">
            <Index/>
            <Dossier dossierKey={match.params.dossierKey} dossierPackage={match.params.dossierPackage} dossierCode={match.params.dossierCode}/>
        </div>
    );
}

class App extends Component {
    render() {
        return (
            <Router>
                <div>
                    <Route path="/" exact component={Index} />
                    <Route path="/dossiers/:dossierKey/:dossierPackage/:dossierCode" component={DossierIndex} />
                </div>
            </Router>
        );
    }
}

export default App;
