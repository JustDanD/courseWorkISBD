INSERT INTO roles(name) VALUES('ROLE_SELLER');
INSERT INTO roles(name) VALUES('ROLE_CUSTOMER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

INSERT INTO cyberware_rarity (name, color, priority) VALUES ('Обычный', '#D6CFCF', 0),
                                                            ('Редкое', '#2471D3', 1),
                                                            ('Необычное', '#1DEC82', 2),
                                                            ('Эпическое', '#9C2BF3', 3),
                                                            ('Легендарное', '#FB922E', 4);

INSERT INTO cyberware_type (type_name) VALUES ('Лобная доля'),
                                              ('Оптическая система'),
                                              ('Операционная система'),
                                              ('Кровеносная система'),
                                              ('Иммунная система'),
                                              ('Нервная система'),
                                              ('Кожа'),
                                              ('Скелет'),
                                              ('Ладони'),
                                              ('Руки'),
                                              ('Ноги');

INSERT INTO order_status (status_name) VALUES ('Оформлен'),
                                              ('Выполнен');


CREATE OR REPLACE FUNCTION updateRating() RETURNS trigger LANGUAGE plpgsql AS
$$
BEGIN
    UPDATE storage_element SET rating =
                                   (SELECT avg(rating) FROM review WHERE storage_element = NEW.storage_element)
    WHERE id = NEW.storage_element;
    RETURN NEW;
END
$$;

CREATE TRIGGER updateRatingTrigger
    AFTER INSERT OR DELETE OR UPDATE
    ON review
    FOR EACH ROW
EXECUTE PROCEDURE updateRating();

DROP TRIGGER updateRatingTrigger ON review;