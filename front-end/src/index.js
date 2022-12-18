import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import {colors, createTheme, CssBaseline, GlobalStyles, ThemeProvider} from "@mui/material";
import "./index.css"

const theme = createTheme({
    palette: {
        text: {
            primary: '#f2e900',
            secondary: '#02d7f2'
        },
        primary: {
            main: '#f2e900',
        },
        secondary: {
            main: '#02d7f2',
        },
        third: {
            main: "rgba(255, 17, 17, 0.5)"
        },
        error: {
            main: colors.red.A400,
        },
        background: {
            default: '#fff',
        },
    },
    typography: {
        button: {
            textTransform: 'none',
        }
    },
    MuiTypography: {
        variantMapping: {
            h1: 'h2',
            h2: 'h2',
            h3: 'h2',
            h4: 'h2',
            h5: 'h2',
            h6: 'h2',
            subtitle1: 'h2',
            subtitle2: 'h2',
            body1: 'span',
            body2: 'span',
        },
    },
});

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <ThemeProvider theme={theme}>
            <CssBaseline/>
            <App/>
        </ThemeProvider>
    </React.StrictMode>
);