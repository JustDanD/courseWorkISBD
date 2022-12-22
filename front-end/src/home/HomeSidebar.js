import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Box,
    Button,
    Checkbox,
    Drawer,
    List,
    ListItem,
    ListItemText,
    TextField
} from "@mui/material";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import {useState} from "react";

const rarities = [
    {
        name: "Обычное",
        color: "#D6CFCF",
        id: "common"
    },
    {
        name: "Необычное",
        color: "#1DEC82",
        id: "notcommon"
    },
    {
        name: "Редкое",
        color: "#2471D3",
        id: "rare"
    },
    {
        name: "Эпическое",
        color: "#9C2BF3",
        id: "epic"
    },
    {
        name: "Легендарное",
        color: "#FB922E",
        id: "legend"
    }
];

const types = [
    {
        name: "Лобная доля",
        id: "head"
    },
    {
        name: "Оптическая система",
        id: "eyes"
    },
    {
        name: "Операционная система",
        id: "os"
    },
    {
        name: "Кровеносная система",
        id: "blood"
    },
    {
        name: "Иммунная система",
        id: "immune"
    },
    {
        name: "Нервная система",
        id: "nervous"
    },
    {
        name: "Кожа",
        id: "skin"
    },
    {
        name: "Скелет",
        id: "skeleton"
    },
    {
        name: "Ладони",
        id: "palms"
    },
    {
        name: "Руки",
        id: "arms"
    },
    {
        name: "Ноги",
        id: "legs"
    }
];
export let HomeSidebar = (props) => {
    const [priceFrom, setPriceFrom] = useState(0);
    const [priceTo, setPriceTo] = useState(0);
    const [typesFilter, setTypes] = useState([]);
    const [raritiesFilter, setRarities] = useState([]);

    let handleFilters = () => {
        let filters = {
            startPrice: priceFrom,
            endPrice: priceTo !== 0 ? priceTo : -1,
            types: typesFilter,
            rarity: raritiesFilter
        }
        props.setFilters(filters);
    };

    let resetFilters = () => {
        setPriceFrom(0);
        setPriceTo(0);
        setTypes([]);
        setRarities([]);
        props.setFilters({
            startPrice: 0,
            endPrice: -1,
            types: [],
            rarity: []
        });
    }

    let selectType = (key) => {
        let newTypes = typesFilter.slice();
        if (newTypes.includes(key)) {
            newTypes.splice(newTypes.indexOf(key), 1);
        } else {
            newTypes.push(key);
        }
        setTypes(newTypes);
    };

    let selectRarity = (key) => {
        let newRarities = raritiesFilter.slice();
        if (newRarities.includes(key)) {
            newRarities.splice(newRarities.indexOf(key), 1);
        } else {
            newRarities.push(key);
        }
        setRarities(newRarities);
    };

    return (
        <Drawer
            anchor="left"
            open
            PaperProps={{
                sx: {
                    backgroundColor: 'rgba(255, 17, 17, 0.3)',
                    border: "none",
                    color: 'primary',
                    height: 'calc(100% - 92px)',
                    marginTop: '92px',
                    width: 320,
                    "::-webkit-scrollbar": {
                        display: "none"
                    }
                }
            }}
            variant="permanent"
        >
                <Box
                    sx={{
                        display: 'flex',
                        flexDirection: 'column',
                        height: '100px'
                    }}
                >
                </Box>
                <Box
                    sx={{
                        display: 'flex',
                        flexDirection: 'column',
                        justifyContent: "start",
                        alignItems: "start",
                        height: '100%',
                        marginTop: '-10%',
                        paddingLeft: '10%'
                    }}
                >
                    <h4>Цена:</h4>
                    <div style={{
                        display: 'flex',
                        flexDirection: 'row',
                        maxWidth: '250px'
                    }}>
                        <TextField
                            variant="outlined"
                            focused
                            value={priceFrom}
                            onChange={(e) => {
                                setPriceFrom(e.target.value);
                            }
                            }
                            type="number"
                            label="От"
                            sx={{
                                width: '45%',
                            }}
                            InputProps={
                                {
                                    style: {
                                        color: "#02d7f2"
                                    }
                                }
                            }
                        />
                        <TextField
                            variant="outlined"
                            focused
                            label="До"
                            type="number"
                            value={priceTo}
                            onChange={(e) => {
                                setPriceTo(e.target.value);
                            }}
                            sx={{
                                width: '45%',
                                marginLeft: '5%'
                            }}
                            InputProps={
                                {
                                    style: {
                                        color: "#02d7f2"
                                    }
                                }
                            }
                        />
                    </div>
                    <Accordion sx={{backgroundColor: 'rgba(255, 17, 17, 0)'}}>
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon sx={{color: '#02d7f2'}}/>}
                            aria-controls="panel1a-content"
                            id="panel1a-header"
                        >
                            <h4>Тип:</h4>
                        </AccordionSummary>
                        <AccordionDetails>
                            <List sx={{maxWidth: '250px'}}>
                                {
                                    types.map((rarity) => (
                                        <ListItem key={rarity.name} id={rarity.name} sx={{marginLeft: '-10%'}}>
                                            <Checkbox
                                                id={rarity.name}
                                                checked={typesFilter.includes(rarity.name)}
                                                onChange={(e) => {
                                                    selectType(e.target.id);
                                                }}
                                            />
                                            <ListItemText primary={rarity.name}/>
                                        </ListItem>
                                    ))
                                }
                            </List>
                        </AccordionDetails>
                    </Accordion>
                    <Accordion sx={{backgroundColor: 'rgba(255, 17, 17, 0)'}}>
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon sx={{color: '#02d7f2'}}/>}
                            aria-controls="panel1a-content"
                            id="panel1a-header"
                        >
                            <h4>Редкость:</h4>
                        </AccordionSummary>
                        <AccordionDetails>
                            <List sx={{width: '250px'}}>
                                {
                                    rarities.map((rarity, index) => (
                                        <ListItem key={rarity.name} id={rarity.name} sx={{color: rarity.color}}
                                                  sx={{marginLeft: '-15%'}}>
                                            <Checkbox
                                                sx={{color: rarity.color}}
                                                id={rarity.name}
                                                checked={raritiesFilter.includes(rarity.name)}
                                                onChange={(e) => {
                                                    selectRarity(rarity.name);
                                                }}
                                            />
                                            <ListItemText sx={{color: rarity.color}} primary={rarity.name}/>
                                        </ListItem>
                                    ))
                                }
                            </List>
                        </AccordionDetails>
                    </Accordion>
                    <br/>
                    <div style={{
                        display: 'flex',
                        flexDirection: "row",
                        justifyContent: "space-between",
                        width: "250px",
                    }}>
                        <Button
                            fullWidth
                            size="large"
                            variant="contained"
                            color="secondary"
                            onClick={handleFilters}
                        >
                            Применить
                        </Button>
                        <Button
                            style={{marginLeft: '5%'}}
                            fullWidth
                            size="large "
                            variant="contained"
                            color="secondary"
                            onClick={resetFilters}
                        >
                            Сбросить
                        </Button>
                    </div>
                </Box>
        </Drawer>
    );
}