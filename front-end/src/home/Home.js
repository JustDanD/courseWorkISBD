import {useLocation, useNavigate} from "react-router-dom";
import {useState} from "react";
import {HomeNavbar} from "./HomeNavbar";
import {HomeSidebar} from "./HomeSidebar";
import {ProductCard} from "./ProductCard";
import {Box} from "@mui/material";

export let Home = (props) => {
    let location = useLocation();
    let navigate = useNavigate();
    let from = location.state?.from?.pathname || "/";
    const [status, setStatus] = useState(false);

    return (<div id="mainPage" style={{display: "flex"}}>
        <HomeNavbar/>
        <HomeSidebar/>
        <div
            style={{
                display: 'flex',
                flex: '1 1 auto',
                maxWidth: '100%',
                paddingTop: 92,
                paddingLeft: 320
            }}
        >
            <Box sx={{
                display: 'flex',
                flex: '1 1 auto',
                flexDirection: 'column',
                width: '100%',
                paddingTop: '2%',
                paddingLeft: '20%',
                paddingRight: '20%',
            }}>
                <ProductCard/>
                <ProductCard/>
            </Box>
        </div>
    </div>);
}