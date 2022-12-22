import {Box, Button, IconButton, Paper, Typography} from "@mui/material";
import {CurrencyBitcoin, Delete, Grade} from "@mui/icons-material";
import $ from "jquery";
import {FullProductCard} from "./FullProductCard";
import {Fragment, useState} from "react";

export let ProductCard = (props) => {
    const [isFull, setIsFull] = useState(false);

    const details = props.cyberwareDetails;

    let addToCart = () => {
        $.ajax({
            url: '/api/main/addToCart',
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
            data: JSON.stringify({storageElementId: details.storageElementId}),
            success: (res) => {
                props.forceUpdate();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(JSON.parse(jqXHR.responseText).message);
            }
        });
    }

    let removeFromCart = () => {
        $.ajax({
            url: '/api/cart/removeFromCart',
            type: 'POST',
            async: true,
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem("accessToken"));
            },
            headers: {
                'Access-Control-Allow-Credentials': true
            },
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({storageElementId: details.storageElementId}),
            success: (res) => {
                props.forceUpdate();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(JSON.parse(jqXHR.responseText).message);
            }
        });
    }

    let setStatus = () => {
        $.ajax({
            url: '/api/profile/setCyberware',
            type: 'POST',
            async: true,
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem("accessToken"));
            },
            headers: {
                'Access-Control-Allow-Credentials': true
            },
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({cyberwareId: details.cyberwareEntity.id, installed: !props.isInstalled}),
            success: (res) => {
                props.forceUpdate();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(JSON.parse(jqXHR.responseText).message);
            }
        });
    }

    return (
        <Fragment>
            <Paper
                sx={{
                    backgroundColor: 'rgba(255, 17, 17, 0.4)',
                    marginBottom: '3%',
                    height: '10%',
                    display: "flex",
                    flexDirection: "row",
                    paddingBottom: '1%',
                    paddingTop: '1%',
                    paddingRight: '3%',
                    paddingLeft: '3%',
                }}
                component='div'
            >
                <img
                    style={{
                        maxHeight: '100px',
                        maxWidth: '100px'
                    }}
                    onClick={() => {
                        setIsFull(true)
                    }}
                    alt
                    src={'' + details.cyberwareEntity.type.id + '.png'}/>
                <Box
                    onClick={() => {
                        setIsFull(true)
                    }}
                    sx={{
                        marginLeft: "5%",
                        display: "flex",
                        flexDirection: "column",
                        justifyContent: "space-between",
                        width: '80%'
                    }}>
                    <Box sx={{
                        display: "flex",
                        flexDirection: "row",
                        justifyContent: "space-between"
                    }}>
                        <Typography variant="h5">{details.cyberwareEntity.name}</Typography>
                        <Typography variant="h5" color='secondary'>
                            {details.price} <CurrencyBitcoin color='primary' fontSize="inherit"/>
                        </Typography>
                    </Box>
                    <Box sx={{
                        display: "flex",
                        flexDirection: "row",
                        justifyContent: "space-between"
                    }}>
                        <Typography variant="caption">Место
                            установки: {details.cyberwareEntity.type.typeName}</Typography>
                        <Typography variant="caption">Редкость: <span
                            style={{color: details.cyberwareEntity.rarity.color}}>{details.cyberwareEntity.rarity.name}</span></Typography>
                        <Typography variant="caption">Точка продажи: {details.sellingPointEntityName}</Typography>
                        <Typography variant="caption" color='secondary'>{Math.floor(details.rating * 10) / 10} <Grade
                            color='secondary'
                            fontSize="inherit"/></Typography>
                        {
                            props.isProfile ?
                                <Typography variant="caption" color='secondary'>
                                    Статус: {props.isInstalled ?  "Установлен" : "Не установлен"}
                                </Typography> :
                                <Typography variant="caption" color='secondary'>
                                    Количество: {details.count}
                                </Typography>
                        }
                    </Box>
                </Box>
                <Box sx={{
                    marginLeft: "5%",
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "center"
                }}>
                    {
                        props.isProfile ?
                            (
                                props.isInstalled ?
                                    <Button size="small" fullWidth variant="contained" color='primary'
                                            onClick={setStatus}>Снять</Button>
                            :
                            <Button size="small" fullWidth variant="contained" color='success'
                                    onClick={setStatus}>Установить</Button>
                            )
                            :
                            (props.isCart ?
                            <IconButton
                                size="large"
                                aria-label="account of current user"
                                aria-controls="menu-appbar"
                                aria-haspopup="true"
                                onClick={removeFromCart}
                                color="inherit"
                            >
                                <Delete color='secondary'/>
                            </IconButton>
                            :
                            <Button size="small" fullWidth variant="contained" color='secondary'
                                    onClick={addToCart}>Купить</Button>)
                    }
                </Box>
            </Paper>
            <FullProductCard
                cyberwareDetails={details}
                key={1}
                forceUpdate={props.forceUpdate}
                isCart={props.isCart}
                open={isFull}
                setIsFull={setIsFull}
                isProfile={props.isProfile}
                isInstall={props.isInstalled}
            />
        </Fragment>
    )
}