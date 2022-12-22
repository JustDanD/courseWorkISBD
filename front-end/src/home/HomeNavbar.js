import {AppBar, Avatar, Badge, Box, IconButton, Menu, MenuItem, Toolbar, Typography} from "@mui/material";
import {CurrencyBitcoin, ShoppingCart} from "@mui/icons-material";
import {useEffect, useState} from "react";
import $ from "jquery";

export let HomeNavbar = (props) => {
    const [cartSize, setCartSize] = useState(0);
    const [anchorEl, setAnchorEl] = useState(null);
    const userDetails = props.userDetails;

    const handleMenu = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const logOut = () => {
        localStorage.removeItem("accessToken");
        window.location.reload();
    }

    let stringToColor = (string) => {
        let hash = 0;
        let i;

        /* eslint-disable no-bitwise */
        for (i = 0; i < string.length; i += 1) {
            hash = string.charCodeAt(i) + ((hash << 5) - hash);
        }

        let color = '#';

        for (i = 0; i < 3; i += 1) {
            const value = (hash >> (i * 8)) & 0xff;
            color += `00${value.toString(16)}`.slice(-2);
        }
        return color;
    };

    let stringAvatar = (name) => {
        let avatarString = name.split(' ');
        return {
            onClick: {handleMenu},
            sx: {
                bgcolor: stringToColor(name),
            },
            children: `${avatarString[0][0]}`,
        };
    };


    let getCartSize = async () => {
        $.ajax({
            url: '/api/main/getCartSize',
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
                setCartSize(res.size);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(JSON.parse(jqXHR.responseText).message);
            }
        });
    };

    useEffect(() => {
        getCartSize();
    }, []);

    return (
        <AppBar position="absolute" color="third" style={{height: '92px'}}>
            <Toolbar sx={{display: "flex", flexDirection: "row", justifyContent: "space-between"}}>
                <Box>
                    <img src="Cyberpunk-2077-Logo.png"
                         style={{marginTop: '-5%'}}
                         height="100px"
                         width="300px"
                    />
                </Box>
                <Box
                    sx={{display: "flex", flexDirection: "row", justifyContent: "space-between", alignItems: 'center'}}>
                    <Box>
                        <IconButton
                            size="large"
                            aria-label="account of current user"
                            aria-controls="menu-appbar"
                            aria-haspopup="true"
                            color="primary"
                            //onClick={handleMenu}
                        >
                            <Typography variant="h6" component="div"
                                        color="secondary">{userDetails.balance}</Typography>
                            <CurrencyBitcoin/>
                        </IconButton>
                        <IconButton
                            size="large"
                            aria-label="account of current user"
                            aria-controls="menu-appbar"
                            aria-haspopup="true"
                            //onClick={handleMenu}
                            color="inherit"
                            onClick={props.openCart}
                        >
                            <Badge badgeContent={cartSize} color="secondary">
                                <ShoppingCart/>
                            </Badge>
                        </IconButton>
                    </Box>
                    <IconButton
                        size="large"
                        aria-label="account of current user"
                        aria-controls="menu-appbar"
                        aria-haspopup="true"
                        onClick={handleMenu}
                        color="inherit"
                    >
                        <Avatar
                            onClick={() => {handleMenu()}}
                            style={{
                                marginLeft: '15px'
                            }}
                            {...stringAvatar(userDetails.name)}
                        />
                    </IconButton>
                    <Menu
                        id="menu-appbar"
                        anchorEl={anchorEl}
                        sx={{
                            ".MuiMenu-paper": {
                                backgroundColor: "rgba(255, 17, 17, 0.6)"
                            }
                        }}
                        anchorOrigin={{
                            vertical: 'top',
                            horizontal: 'right',
                        }}
                        keepMounted
                        transformOrigin={{
                            vertical: 'top',
                            horizontal: 'right',
                        }}
                        open={Boolean(anchorEl)}
                        onClose={handleClose}
                    >
                        <MenuItem sx={{color: '#02d7f2'}} onClick={() => {
                            props.openProfile();
                            handleClose();
                        }}>Профиль</MenuItem>
                        <MenuItem sx={{color: '#02d7f2'}} onClick={logOut}>Выйти</MenuItem>
                    </Menu>
                </Box>
            </Toolbar>
        </AppBar>
    );
}