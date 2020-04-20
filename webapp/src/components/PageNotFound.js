import React, {Component} from 'react';
import {Link} from "react-router-dom";

export class PageNotFound extends Component {

    render() {
        return (
            <div className="p-grid">
                <div className="p-col-12">
                    <div className="card">
                        <h1>404 - Not found!</h1>
                        <p>La página a la que quizo acceder no es válida.</p>
                        <Link to={'/'}>Volver</Link>
                    </div>
                </div>
            </div>
        );
    }
}
