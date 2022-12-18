import {Fragment, useState} from "react";
import {Button, InputAdornment, TextField} from "@mui/material";
import {AccountCircle, AlternateEmail, VpnKey} from "@mui/icons-material";
import {
    useLocation,
    useNavigate
} from "react-router-dom";

export let SignIn = (props) => {
    let location = useLocation();
    let navigate = useNavigate();
    let from = location.state?.from?.pathname || "/";
    const [status, setStatus] = useState(false);

    return (
        <div id="signInWrap">
            <div id="signInForm">
                <div>
                    <h2>Вход</h2>
                    <form id="loginForm" onSubmit={(e) => {
                        e.preventDefault();
                    }}>
                        <TextField
                            style={{ marginBottom: "24px" }}
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
                        /><br />
                        <Button  variant="contained" color="secondary" type='submit'>Войти</Button>
                        <Button  style={{ marginLeft: "25px" }} color="secondary" variant="contained" onClick={() => { navigate("/signUp"); }}>Зарегистрироватсья</Button><br />
                    </form>
                </div>
            </div>
        </div>);
}