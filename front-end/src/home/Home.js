import {Button, Checkbox, FormControlLabel, InputAdornment, TextField} from "@mui/material";
import {AccountCircle, VpnKey} from "@mui/icons-material";
import {useLocation, useNavigate} from "react-router-dom";
import {useState} from "react";
import {HomeNavbar} from "./HomeNavbar";

export let Home = (props) => {
    let location = useLocation();
    let navigate = useNavigate();
    let from = location.state?.from?.pathname || "/";
    const [status, setStatus] = useState(false);

    return (
        <div id="mainPage">
            <HomeNavbar/>
        </div>
    );
}