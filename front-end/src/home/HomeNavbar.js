import {AppBar, IconButton, Menu, MenuItem, Toolbar, Typography} from "@mui/material";
import {AccountBalanceWallet, AccountCircle, CurrencyBitcoin, ShoppingCart} from "@mui/icons-material";
import {Fragment} from "react";

export let HomeNavbar = () => {
    return (
        <AppBar position="absolute" color="third" style={{height:'92px'}}>
            <Toolbar sx={{display: "flex", flexDirection: "row", justifyContent: "space-between"}}>
                <div>
                    <img src="Cyberpunk-2077-Logo.png"
                         style={{marginTop: '-5%'}}
                         height="100px"
                         width="300px"
                    />
                </div>
                <div>
                    <IconButton
                        size="large"
                        aria-label="account of current user"
                        aria-controls="menu-appbar"
                        aria-haspopup="true"
                        color="primary"
                        //onClick={handleMenu}
                    >
                        <Typography variant="h6" component="div" color="secondary">20000</Typography>
                        <CurrencyBitcoin/>
                    </IconButton>
                    <IconButton
                        size="large"
                        aria-label="account of current user"
                        aria-controls="menu-appbar"
                        aria-haspopup="true"
                        //onClick={handleMenu}
                        color="inherit"
                    >
                        <ShoppingCart/>
                    </IconButton>
                    <IconButton
                        size="large"
                        aria-label="account of current user"
                        aria-controls="menu-appbar"
                        aria-haspopup="true"
                        //onClick={handleMenu}
                        color="inherit"
                    >
                        <AccountCircle/>
                    </IconButton>
                </div>
            </Toolbar>
        </AppBar>
    );
}