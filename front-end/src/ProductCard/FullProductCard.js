import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Box,
    Button,
    Dialog,
    List,
    ListItem,
    Paper, Rating,
    TextField,
    Typography
} from "@mui/material";
import {CurrencyBitcoin, Grade, StarBorder} from "@mui/icons-material";
import $ from "jquery";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import {Fragment, useEffect, useState} from "react";

let ReviewCard = (props) => {
    const details = props.details;

    return (
        <Paper sx={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "space-between",
            width: '100%',
            backgroundColor: 'rgba(255, 17, 17, 0.4)',
            paddingLeft: '5%',
            paddingRight: '5%'
        }}>
            <Box sx={{
                display: "flex",
                flexDirection: "row",
                justifyContent: "space-between",
                alignItems: 'center',
                width: '100%'
            }}>
                <Typography variant="subtitle1">{details.author}</Typography>
                <Typography variant="subtitle1" color='secondary'>{details.rating} <Grade color='secondary'
                                                                                          fontSize="inherit"/></Typography>
            </Box>
            <Typography variant="body1">{details.review}</Typography>
        </Paper>
    )
};

// let PostReviewCard = (props) => {
//     let handleClose = () => {
//         props.setIsPostReview(false);
//     }
//
//     return (<Dialog
//         open={props.open}
//         onClose={handleClose}
//         PaperProps={{
//             sx: {
//                 backgroundColor: 'rgba(255, 17, 17,)',
//                 marginBottom: '3%',
//                 // height: '30%',
//                 display: "flex",
//                 flexDirection: "row",
//                 paddingBottom: '1%',
//                 paddingTop: '1%',
//                 paddingRight: '3%',
//                 paddingLeft: '3%',
//                 minWidth: '800px'
//             },
//             component: 'div'
//         }
//         }
//     >
//         <DialogTitle>Оставить отзыв</DialogTitle>
//     </Dialog>);
// };


export let FullProductCard = (props) => {
    const [reviews, setReviews] = useState([]);
    const [isPostReview, setIsPostReview] = useState(false);
    const [rating, setRating] = useState(0);
    const [reviewText, setText] = useState("");

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

    let handleClose = () => {
        props.setIsFull(false);
    }

    let getReviews = async () => {
        $.ajax({
            url: '/api/cyberware/getReviews',
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
                console.log(res);
                setReviews(res);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(JSON.parse(jqXHR.responseText).message);
            }
        });
    }

    let postReview = () => {
        $.ajax({
            url: '/api/cyberware/postReview',
            type: 'POST',
            async: true,
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem("accessToken"));
            },
            headers: {
                'Access-Control-Allow-Credentials': true
            },
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({
                id: details.storageElementId,
                rating: rating,
                reviewText: reviewText
            }),
            success: (res) => {
                alert("Спасибо за отзыв!");
                setIsPostReview(false);
                getReviews();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(JSON.parse(jqXHR.responseText).message);
            }
        });
    };

    useEffect(() => {
        getReviews();
    }, []);

    return (
        <Dialog
            open={props.open}
            onClose={handleClose}
            PaperProps={{
                sx: {
                    backgroundColor: 'rgba(0, 0, 0, 0.7)',
                    marginBottom: '3%',
                    // height: '30%',
                    display: "flex",
                    flexDirection: "row",
                    paddingBottom: '1%',
                    paddingTop: '1%',
                    paddingRight: '3%',
                    paddingLeft: '3%',
                    minWidth: '800px'
                },
                component: 'div'
            }
            }
        >
            <Box sx={{
                marginLeft: "5%",
                display: "flex",
                flexDirection: "column",
                justifyContent: "space-between",
                width: '100%',
            }}>
                <Box sx={{
                    display: "flex",
                    flexDirection: "row",
                    justifyContent: "space-between",
                }}>
                    <Box sx={{
                        display: "flex",
                        flexDirection: "column",
                    }}>
                        <Box sx={{
                            display: "flex",
                            flexDirection: "row",
                            alignItems: "center",
                            minWidth: '400px'
                        }}>
                            <Typography variant="h5">{details.cyberwareEntity.name}</Typography>
                            <Typography sx={{marginLeft: "3%"}} variant="h6" color='secondary'>{details.rating}
                                <Grade
                                    color='secondary'
                                    fontSize="inherit"/></Typography>
                        </Box>
                        <Typography variant="subtitle1">Описание:</Typography>
                        <Typography variant="body1">{details.cyberwareEntity.description}</Typography>
                    </Box>
                    <img style={{maxHeight: "100px", marginLeft: '3%'}} src={'' + details.cyberwareEntity.type.id + '.png'}/>
                </Box>
                <Box sx={{
                    display: "flex",
                    flexDirection: "row",
                    justifyContent: "space-between",
                    marginTop: "2%",
                    width: '100%',
                }}>
                    <Typography variant="caption">Место
                        установки: {details.cyberwareEntity.type.typeName}</Typography>
                    <Typography variant="caption">Редкость: <span
                        style={{color: details.cyberwareEntity.rarity.color}}>{details.cyberwareEntity.rarity.name}</span></Typography>
                    <Typography variant="caption">Точка продажи: {details.sellingPointEntityName}</Typography>
                </Box>
                <Box sx={{
                    width: '100%',
                    display: "flex",
                    flexDirection: "row",
                    justifyContent: "space-between",
                    alignItems: "center",
                    marginTop: "2%"
                }}>
                    <Typography variant="h5" color='secondary'>
                        {details.price} <CurrencyBitcoin color='primary' fontSize="inherit"/>
                    </Typography>
                    {
                        props.isProfile ?
                            <Typography variant="subtitle1" color='secondary'>
                                Статус: {props.isInstalled ?  "Установлен" : "Не установлен"}
                            </Typography> :
                            <Typography variant="subtitle1" color='secondary'>
                                Количество: {details.count}
                            </Typography>
                    }
                    {props.isCart ? null :
                        <Button size="large" variant="contained" color='secondary'
                                onClick={addToCart}>Купить</Button>}
                </Box>
                <Box sx={{
                    display: "flex",
                    flexDirection: "row",
                    justifyContent: "space-between",
                    alignItems: "center",
                    marginTop: "2%"
                }}>
                    <Accordion
                        disabled={isPostReview}
                        sx={{backgroundColor: 'rgba(255, 17, 17, 0)', width: '100%'}}
                    >
                        <Box sx={{
                            paddingLeft: '0%',
                            display: 'flex',
                            flexDirection: "row",
                            justifyContent: "space-between",
                            alignItems: 'center',
                        }}>
                            <AccordionSummary
                                sx={{
                                    paddingLeft: '0%',
                                    display: 'flex',
                                    flexDirection: "row",
                                    justifyContent: "space-between",
                                }}
                                expandIcon={<ExpandMoreIcon sx={{color: '#02d7f2'}}/>}
                                aria-controls="panel1a-content"
                                id="panel1a-header"
                            >
                                <span><h4>Отзывы:</h4></span>
                            </AccordionSummary>
                            <Button sx={{height: '50%'}} size="small" variant="contained" color='secondary'
                                    disabled={isPostReview}
                                    onClick={() => {
                                        setIsPostReview(true);
                                    }}>
                                Оставить отзыв
                            </Button>
                        </Box>

                        <AccordionDetails sx={{padding: '0px'}}>
                            <List>
                                {
                                    reviews.map((review) => (
                                        <ListItem key={review.reviewId} sx={{paddingLeft: '0px', paddingRight: '0px'}}>
                                            <ReviewCard details={review}/>
                                        </ListItem>
                                    ))
                                }
                            </List>
                        </AccordionDetails>
                    </Accordion>
                </Box>
                {isPostReview ?
                    <Fragment>
                        <Rating
                            emptyIcon={<StarBorder fontSize="inherit" sx={{color: '#f2e900'}}/>}
                            sx={{color:'#2471D3'}}
                            max={5}
                            precision={0.5}
                            value={rating}
                            onChange={(e) => {
                                setRating(Number(e.target.value));
                            }}
                            name="customized-10"
                            color='secondary'/>
                        <br/>
                        <TextField
                            multiline
                            focused
                            label='Текст отзыва'
                            value={reviewText}
                            onChange={(e) => {
                                setText(e.target.value);
                            }}
                            sx={{
                                width: '100%'
                            }}
                        />
                        <Box sx={{
                            display: "flex",
                            flexDirection: "row",
                            justifyContent: "space-around",
                            alignItems: "center",
                            marginTop: '3%'
                        }}>
                            <Button sx={{marginRight: '2.5%'}}
                                    fullWidth size="small"
                                    variant="contained"
                                    color='secondary'
                                    onClick={() => {
                                        setIsPostReview(false);
                                    }}
                            >
                                Отмена
                            </Button>
                            <Button sx={{marginLeft: '2.5%'}}
                                    fullWidth size="small"
                                    variant="contained"
                                    color='secondary'
                                    onClick={postReview}
                            >
                                Отправить отзыв
                            </Button>
                        </Box>
                    </Fragment>
                    : null}
            </Box>
        </Dialog>
    )
}