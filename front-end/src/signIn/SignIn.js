import {Fragment, useEffect, useState} from "react";
import {Button, InputAdornment, TextField} from "@mui/material";
import {AccountCircle, VpnKey} from "@mui/icons-material";
import {useLocation, useNavigate} from "react-router-dom";
import $ from "jquery";

export let SignIn = (props) => {
    let location = useLocation();
    let navigate = useNavigate();
    let from = location.state?.from?.pathname || "/";
    const [status, setStatus] = useState(false);

    useEffect(() => {
        $.ajax({
            url: 'http://d-pimenov.ru/auth/isAuthenticated',
            type: 'POST',
            async: true,
            success: (res) => {
                navigate("/", {replace: true});
            },
            error: function (jqXHR, textStatus, errorThrown) {
                setStatus(true);
            }
        });
    }, []);

    let handleSignIn = () => {
        let userData = {
            username: document.getElementById("login").value,
            password: document.getElementById("password").value,
        }
        $.ajax({
            url: 'http://d-pimenov.ru/auth/signIn',
            type: 'POST',
            headers: {
                'Access-Control-Allow-Credentials': true
            },
            async: true,
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(userData),
            success: (res) => {
                navigate("/", {replace: true});
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(JSON.parse(jqXHR.responseText).message);
                window.location.reload();
            }
        });
    };

    return (
        <Fragment>
            {
                status ?
                    <div id="signInWrap">
                        <div id="signInForm">
                            <div>
                                <h2>Вход</h2>
                                <form id="loginForm" onSubmit={(e) => {
                                    e.preventDefault();
                                    handleSignIn();
                                }}>
                                    <TextField
                                        style={{marginBottom: "24px"}}
                                        id="login"
                                        type='text'
                                        label="Login"
                                        focused
                                        fullWidth
                                        InputProps={{
                                            startAdornment: (
                                                <InputAdornment position="start">
                                                    <AccountCircle color="primary"/>
                                                </InputAdornment>
                                            ),
                                        }}
                                        variant="outlined"
                                        required
                                    /><br />
                                    <TextField
                                        required
                                        style={{ marginBottom: "24px" }}
                                        id="password"
                                        autoComplete='new-password'
                                        label="Пароль"
                                        type="password"
                                        fullWidth
                                        focused
                                        InputProps={{
                                            startAdornment: (
                                                <InputAdornment position="start">
                                                    <VpnKey color="primary"/>
                                                </InputAdornment>
                                            ),
                                        }}
                                        variant="outlined"
                                    /><br/>
                                    <Button variant="contained" color="secondary" type='submit'>Войти</Button>
                                    <Button style={{marginLeft: "25px"}} color="secondary" variant="contained"
                                            onClick={() => {
                                                navigate("/signUp");
                                            }}>Зарегистрироватсья</Button><br/>
                                </form>
                            </div>
                        </div>
                    </div> : null
            }
        </Fragment>);
}