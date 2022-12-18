import {Fragment, useState} from "react";
import {Button, Checkbox, FormControlLabel, InputAdornment, TextField} from "@mui/material";
import {AccountCircle, AlternateEmail, VpnKey} from "@mui/icons-material";
import {
    useLocation,
    useNavigate
} from "react-router-dom";

export let SignUp = (props) => {
    let location = useLocation();
    let navigate = useNavigate();
    let from = location.state?.from?.pathname || "/";
    const [status, setStatus] = useState(false);

    return (
        <div id="signInWrap">
            <div id="signInForm">
                <div>
                    <h2>Регистрация</h2>
                    <form id="registerForm" onSubmit={(e) => {
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
                        /><br />
                        <FormControlLabel id="isSeller" control={<Checkbox defaultChecked />} label="Диллер?" />
                        <Button  fullWidth color="secondary" variant="contained" onClick={() => { navigate("/register"); }}>Зарегистрироватсья</Button><br />
                    </form>
                </div>
            </div>
        </div>);
}