import {useEffect, useState} from "react";
import {Avatar, Box, Dialog, Typography} from "@mui/material";
import {CurrencyBitcoin} from "@mui/icons-material";
import $ from "jquery";
import {ProductCard} from "../ProductCard/ProductCard";

export let Profile = (props) => {
    const [ownedCyberwares, setOwnedCyberwares] = useState([]);
    const [i, setI] = useState(0);

    const userDetails = props.userDetails;

    let handleClose = () => {
        props.setIsFull(false);
    }

    let forceUpdate = () => {
        getOwnedCyberwares();
    }
    let getOwnedCyberwares = async () => {
        $.ajax({
            url: 'http://d-pimenov.ru/api/profile/getOwnedCyberwares',
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
                console.log(res);
                setOwnedCyberwares(res);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(JSON.parse(jqXHR.responseText).message);
            }
        });
    }

    let stringAvatar = (name) => {
        let avatarString = name.split(' ');
        return {
            sx: {
                bgcolor: stringToColor(name),
                width: 52,
                height: 52
            },
            children: `${avatarString[0][0]}`,
        };
    };

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

    useEffect(() => {
        getOwnedCyberwares();
    }, [])

    return (
        <Dialog
            open={props.open}
            onClose={handleClose}
            PaperProps={{
                sx: {
                    display: 'flex',
                    flex: '1 1 auto',
                    maxWidth: '80%',
                    maxHeight: '60%',
                    backgroundColor: 'transparent',
                    "::-webkit-scrollbar": {
                        display: "none"
                    }
                },
                component: 'div'
            }
            }
        >
            <Box sx={{
                display: 'flex',
                flex: '1 1 auto',
                flexDirection: 'column',
                width: '100%',
                paddingTop: '2%',
                paddingLeft: '5%',
                paddingRight: '5%',
                backgroundColor: 'rgba(0, 0, 0, 0.5)'
            }}>
                <Box sx={{
                    display: 'flex',
                    flex: '1 1 auto',
                    flexDirection: 'row',
                    justifyContent: 'space-between',
                    height: '5%',
                }}>
                    <Typography variant="h5">{userDetails.name}</Typography>
                    <Typography variant="h5">Баланс:{userDetails.balance}<CurrencyBitcoin/></Typography>
                </Box>
                <Box key={i} sx={{
                    display: 'flex',
                    flex: '1 1 auto',
                    flexDirection: 'column',
                    height: '5%',
                    marginTop:'2%',
                }}>
                    <Typography variant="h5">Инвентарь</Typography><br/>
                    {
                        ownedCyberwares.map((product, index) => (
                            <ProductCard
                                cyberwareDetails={product}
                                forceUpdate={forceUpdate}
                                key={index}
                                isCart={true}
                                isProfile={true}
                                isInstalled={product.installed}
                            />
                        ))
                    }
                </Box>
            </Box>
        </Dialog>
    )
}