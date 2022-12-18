import {useLocation, useNavigate} from "react-router-dom";
import {useState} from "react";

export let Home = (props) => {
    let location = useLocation();
    let navigate = useNavigate();
    let from = location.state?.from?.pathname || "/";
    const [status, setStatus] = useState(false);

    return (<div id="mainPage">
        <div></div>
    </div>);
}