import {useLocation, useNavigate} from "react-router-dom";
import {useState} from "react";
import {HomeNavbar} from "./HomeNavbar";
import {HomeSidebar} from "./HomeSidebar";

export let Home = (props) => {
    let location = useLocation();
    let navigate = useNavigate();
    let from = location.state?.from?.pathname || "/";
    const [status, setStatus] = useState(false);

    return (<div id="mainPage" style={{display: "flex"}}>
        <HomeNavbar/>
        <HomeSidebar/>
    </div>);
}