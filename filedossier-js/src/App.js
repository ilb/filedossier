import React, { Component } from 'react';
import Dossier from './Dossier';
import Example from './Example';
//import DisplayRemoteData from './DisplayRemoteData';
import './App.css';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";


function Index() {
    return (<div className="app">
        <Link to="/dossiers/123/testmodel/TEST">TEST DOSSIER</Link>
        <Link to="/dossiers/123/testmodel/TEST2">TEST DOSSIER2</Link>
    </div>
            );
}


function DossierIndex( { match }) {
    console.log(match.params);
    return (<div className="app">
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
