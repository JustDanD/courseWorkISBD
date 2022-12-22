import {Fragment, useEffect, useState} from "react";
import {Button, Checkbox, FormControlLabel, InputAdornment, TextField} from "@mui/material";
import {AccountCircle, VpnKey} from "@mui/icons-material";
import {useLocation, useNavigate} from "react-router-dom";
import $ from "jquery";

export let SignUp = (props) => {
    let location = useLocation();
    let navigate = useNavigate();
    let from = location.state?.from?.pathname || "/";
    const [status, setStatus] = useState(false);
    const [isSeller, setIsSeller] = useState(false);

    useEffect(() => {
        $.ajax({
            url: 'http://d-pimenov.ru/api/auth/isAuthenticated',
            type: 'GET',
            beforeSend: function (xhr) { xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem("accessToken")); },
            async: true,
            success: (res) => {
                navigate("/", {replace: true});
            },
            error: function (jqXHR, textStatus, errorThrown) {
                setStatus(true);
            }
        });
    }, []);

    let handleRegister = () => {
        let userData = {
            username: document.getElementById("login").value,
            password: document.getElementById("password").value,
            role: isSeller ? "ROLE_SELLER" : "ROLE_CUSTOMER"
        }
        $.ajax({
            url: 'http://d-pimenov.ru/api/auth/register',
            type: 'POST',
            async: true,
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(userData),
            success: (res) => {
                alert("Вы успешно зарегистрировались");
                navigate("/", {replace: true});
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText);
                window.location.reload();
            }
        });
    }

    return (
        <Fragment>
            {
                status ?
                <div id="signInWrap">
                    <div id="signInForm">
                        <div>
                            <h2>Регистрация</h2>
                            <form id="registerForm" onSubmit={(e) => {
                                e.preventDefault();
                                handleRegister();
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
                                /><br/>
                                <TextField
                                    required
                                    id="password"
                                    autoComplete='new-password'
                                    label="Пароль"
                                    type="password"
                                    focused
                                    fullWidth
                                    InputProps={{
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <VpnKey color="primary"/>
                                            </InputAdornment>
                                        ),
                                    }}
                                    variant="outlined"
                                /><br/>
                                <FormControlLabel id="isSeller" control={<Checkbox value={isSeller} onChange={(e) => {
                                    setIsSeller(e.target.checked)
                                }}/>} label="Диллер?"/>
                                <Button fullWidth color="secondary" variant="contained"
                                        type="submit">Зарегистрироватсья</Button><br/>
                            </form>
                        </div>
                    </div>
                </div> : null
            }
        </Fragment>);
}