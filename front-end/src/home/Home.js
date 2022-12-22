import {useLocation, useNavigate} from "react-router-dom";
import {useEffect, useReducer, useState} from "react";
import {HomeNavbar} from "./HomeNavbar";
import {HomeSidebar} from "./HomeSidebar";
import {ProductCard} from "../ProductCard/ProductCard";
import {Box, Button} from "@mui/material";
import $ from "jquery";
import {FullProductCard} from "../ProductCard/FullProductCard";
import {CartCard} from "./CartCard";
import {Profile} from "./Profile";

export let Home = (props) => {
    let location = useLocation();
    let navigate = useNavigate();
    let from = location.state?.from?.pathname || "/";
    const [products, setProducts] = useState([]);
    const [filters, setFilters] = useState({
        rarity: [],
        types: [],
        startPrice: 0,
        endPrice: -1
    });
    const [navbarKey, setKey] = useState(0);
    const [isCart, setIsCart] = useState(false);
    const [isProfile, setIsProfile] = useState(false);
    const [userDetails, setUserDetails] = useState({
        name: "test",
        balance: "0"
    });

    let getUserDetails = async () => {
        $.ajax({
            url: '/api/main/getUserDetails',
            type: 'GET',
            async: true,
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem("accessToken"));
            },
            headers: {
                'Access-Control-Allow-Credentials': true
            },
            dataType: "json",
            success: (res) => {
                if (userDetails.role === 2) {
                    console.log("Войдите, пожалуйста, через клиент продавца");
                    localStorage.removeItem("accessToken");
                    window.location.reload();
                }
                setUserDetails(res);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(JSON.parse(jqXHR.responseText).message);
            }
        });
    };

    let forceRerender = () => {
        setKey(navbarKey + 1);
        getUserDetails();
    }

    let getCyberwares = async (startPosition) => {
        let data = JSON.stringify({
            startPosition: startPosition,
            size: 7,
            rarity: filters.rarity,
            type: filters.types,
            startPrice: filters.startPrice,
            endPrice: filters.endPrice
        });

        $.ajax({
            url: '/api/main/getCyberwares',
            type: 'POST',
            async: true,
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem("accessToken"));
            },
            headers: {
                'Access-Control-Allow-Credentials': true
            },
            contentType: 'application/json; charset=utf-8',
            dataType: "json",
            data: data,
            success: (res) => {
                console.log(res);
                let newProducts;
                if (startPosition >= products.length)
                    newProducts = [...products, ...(res)];
                else
                    newProducts = res;
                setProducts(newProducts);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(JSON.parse(jqXHR.responseText).message);
            }
        });
    };

    let getMoreCyberwares = () => {
        getCyberwares(products.length);
    }

    let openCart = () => {
        setIsCart(true);
    }

    let openProfile = () => {
        setIsProfile(true);
    }

    useEffect(() => {
        getUserDetails();
        getCyberwares(0);
    }, []);

    useEffect(() => {
        getCyberwares(0);
    }, [filters]);

    return (<div id="mainPage" style={{display: "flex"}}>
        <HomeNavbar openProfile={openProfile} openCart={openCart} key={navbarKey} userDetails={userDetails}/>
        <HomeSidebar setFilters={setFilters}/>
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
                backgroundColor: 'rgba(0, 0, 0, 0.5)',
                overflow: 'scroll',
                "::-webkit-scrollbar": {
                    display: "none"
                }
            }}>
                {
                    products.map((product, index) => (
                        <ProductCard
                            cyberwareDetails={product}
                            key={index}
                            forceUpdate={forceRerender}
                            isCart={false}
                        />
                    ))
                }
                {
                    products.length < 7 ? null :
                        <div style={{display: 'flex', justifyContent: 'center'}}>
                            <Button
                                sx={{width: '30%'}}
                                variant="contained"
                                color='secondary'
                                onClick={getMoreCyberwares}
                            >
                                Показать ещё
                            </Button>
                        </div>}
            </Box>
            <CartCard
                key={navbarKey}
                open={isCart}
                forceUpdate={forceRerender}
                setIsFull={setIsCart}
            />
            <Profile
                open={isProfile}
                forceUpdate={forceRerender}
                setIsFull={setIsProfile}
                userDetails={userDetails}
            />
        </div>
    </div>);
}