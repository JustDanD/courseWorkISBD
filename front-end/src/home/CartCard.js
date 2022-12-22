import {useEffect, useState} from "react";
import {Box, Button, Dialog, Typography} from "@mui/material";
import $ from "jquery";
import {ProductCard} from "../ProductCard/ProductCard";
import {CurrencyBitcoin} from "@mui/icons-material";

export let CartCard = (props) => {
    const [products, setProducts] = useState([]);
    const [price, setPrice] = useState([]);

    let getCart = async () => {
        $.ajax({
            url: 'http://d-pimenov.ru/api/cart/getCartContent',
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
                setProducts(res.cyberwares);
                setPrice(res.price);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                if (JSON.parse(jqXHR.responseText).message !== "Корзина пуста")
                    alert(JSON.parse(jqXHR.responseText).message);
            }
        });
    };

    let approveOrder = () => {
        $.ajax({
            url: 'http://d-pimenov.ru/api/cart/confirmOrder',
            type: 'POST',
            async: true,
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem("accessToken"));
            },
            headers: {
                'Access-Control-Allow-Credentials': true
            },
            success: (res) => {
                alert("Заказ успешно оформлен!");
                props.forceUpdate();
                handleClose();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(JSON.parse(jqXHR.responseText).message);
            }
        });
    }

    let handleClose = () => {
        props.setIsFull(false);
    }

    useEffect(() => {
        getCart();
    }, []);

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
                flexDirection: 'row',
                width: '100%',
                paddingTop: '2%',
                paddingLeft: '5%',
                paddingRight: '5%',
                backgroundColor: 'rgba(0, 0, 0, 0.5)'
            }}>
                <Box sx={{
                    display: 'flex',
                    flex: '1 1 auto',
                    flexDirection: 'column',
                    height: '5%',
                }}>
                    <Typography variant="h5">Корзина</Typography><br/>
                    {
                        products.map((product, index) => (
                            <ProductCard
                                cyberwareDetails={product}
                                forceUpdate={props.forceUpdate}
                                key={index}
                                isCart={true}
                            />
                        ))
                    }
                </Box>
                <Box sx={{
                    display: 'flex',
                    flex: '1 1 auto',
                    height:'20%',
                    width: '1%',
                    flexDirection: 'column',
                    justifyContent:'space-around',
                    marginLeft: '5%'
                }}>
                    <Typography variant="h6">Итоговая сумма: {price}<CurrencyBitcoin color='primary' fontSize="inherit"/></Typography>
                    <Button sx={{marginTop:'5%'}} size="small" variant="contained" color='secondary' onClick={approveOrder}>Подвтердить заказ</Button>
                </Box>
            </Box>
        </Dialog>
    )
}