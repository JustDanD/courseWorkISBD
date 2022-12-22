import {Fragment, useEffect, useState} from "react";
import $ from "jquery";
import {useLocation, useNavigate} from "react-router-dom";

export let AuthLayout = (props) => {
    const [isLogged, setIsLogged] = useState(false);
    let location = useLocation();
    let navigate = useNavigate();
    let from = location.state?.from?.pathname || "/";

    useEffect(() => {
        $.ajax({
            url: 'http://d-pimenov.ru/api/auth/isAuthenticated',
            type: 'GET',
            async: true,
            beforeSend: function (xhr) { xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem("accessToken")); },
            success: (res) => {
                setIsLogged(true);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                navigate("/signIn", {replace: true});
            }
        });
    }, []);
    return (
        <Fragment>
            {
                isLogged ? props.component : null
            }
        </Fragment>
    )
}