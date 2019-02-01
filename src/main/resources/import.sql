
insert into luckydrive.point(address, lat, lng, radius) values ('улица Программистов 3, Минск', 53.9246919, 27.6954545, 0);
insert into luckydrive.organization(name) values ('IBA');
insert into organization_point_connect(organization_id, point_id) values (1,1);

-- PW for anna.kurnikova@iba.group: 123_
-- PW for eva.derev@iba.group: 542345ouy
-- PW for sergei.vasilyev@iba.group: erete123
-- PW for anton.veresov@iba.group: qwerty
-- PW for polina.loseva@iba.group: 123sdsgf
-- PW for konstantin.kaverin@iba.group: qwerty_ 
-- PW for elizaveta.vereskova@iba.group: dar123 
-- PW for oleg.nevedov@iba.group: olezha
-- PW for vera.abaeva@iba.group: 354gh_
-- PW for anatoly.zhivchik@iba.group: qwerty_
-- PW for elena.semchenko@iba.group: dar123
-- PW for vitaly.rudov@iba.group: vitalya_
insert into luckydrive.user(email, password, name, surname, organization_id, user_mode) values ('anna.kurnikova@iba.group', '$2a$11$e/7Eop3kChVEut12JA/1DOtWuy6YnQi99JV3DSQKZxeJ3j0TJUM0S', 'Anna', 'Kurnikova', 1, 'DRIVER');
insert into luckydrive.user(email, password, name, surname, organization_id, user_mode) values ('sergei.vasilyev@iba.group', '$2a$11$pPZyfob5MI.uL96npIaUWuIZkZoWMQUrKEo54prBjJ2UncAvFKfMW', 'Sergei', 'Vasilyev', 1, 'DRIVER');
insert into luckydrive.user(email, password, name, surname, organization_id, user_mode) values ('eva.derev@iba.group', '$2a$11$hga9i6ecmQWLAOy/hBQs6uJm6CxB4sHqpTxwLG1RYEju4yl.eu0bS', 'Eva', 'Derev', 1, 'PASSENGER');
insert into luckydrive.user(email, password, name, surname, organization_id, user_mode) values ('anton.veresov@iba.group', '$2a$11$86eWdH9anD55k16S5K/HR.CdX/jRV2n/m8Krd0zLjhFNtf30I4/86', 'Anton', 'Veresov', 1, 'PASSENGER');
insert into luckydrive.user(email, password, name, surname, organization_id, user_mode) values ('polina.loseva@iba.group', '$2a$11$8wSFlEpGQfxJnjwCh4Dije8jFL6kaf.YSY2L6sH0RHlk0S1pRyfQO', 'Polina', 'Loseva', 1, 'DRIVER');
insert into luckydrive.user(email, password, name, surname, organization_id, user_mode) values ('konstantin.kaverin@iba.group', '$2a$11$IgRoZgq5Vi0PeBnwyeKcB.0gOzuJKhHAYZgJP88O410MSDHlS3DPu', 'Konstantin', 'Kaverin', 1, 'PASSENGER');
insert into luckydrive.user(email, password, name, surname, organization_id, user_mode) values ('elizaveta.vereskova@iba.group', '$2a$11$aN1zYQVL76dMrxhOnozHgOs2pY4LIoVM3GABjc9NoDkoia/QUIAoK', 'Elizaveta', 'Vereskova', 1, 'DRIVER');
insert into luckydrive.user(email, password, name, surname, organization_id, user_mode) values ('oleg.nevedov@iba.group', '$2a$11$e1XplrHOkx0PXsVyA6isquV3yR/UMiF3bzfT8nLRfz.XZjiAoHHx6', 'Oleg', 'Nevedov', 1, 'PASSENGER');
insert into luckydrive.user(email, password, name, surname, organization_id, user_mode) values ('vera.abaeva@iba.group', '$2a$11$rarx3aUmsNdVVCO9fUYcMOlndchJ7dHg5KnDiIDChr43aCZMtWWa2', 'Vera', 'Abaeva', 1, 'DRIVER');
insert into luckydrive.user(email, password, name, surname, organization_id, user_mode) values ('anatoly.zhivchik@iba.group', '$2a$11$djZ7XQq4pfvc9PQhTZ.JsOCY2.pl5xKcTxWKDfwg6FKOPnwOUyyZ2', 'Anatoly', 'Zhivchik', 1, 'PASSENGER');
insert into luckydrive.user(email, password, name, surname, organization_id, user_mode) values ('elena.semchenko@iba.group', '$2a$11$e6pjTcYj5F1ej72KCJcIPOymuk0TeGBCxKTWOsp6/q94qgo/HZefK', 'Elena', 'Semchenko', 1, 'PASSENGER');
insert into luckydrive.user(email, password, name, surname, organization_id, user_mode) values ('vitaly.rudov@iba.group', '$2a$11$63lWBU769TDNS7ga6YZjy.fdq2jqn1XC5W3TDELLE6AwGTHTprhmC', 'Vitaly', 'Rudov', 1, 'DRIVER');

insert into luckydrive.point(address, lat, lng, radius) values ('Могилёвская', 53.861592, 27.674687, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Автозаводская', 53.869888, 27.647786, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Партизанская', 53.875151, 27.629463, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Тракторный завод', 53.889163, 27.614926, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Пролетарская', 53.889616, 27.585613, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Первомайская', 53.893828, 27.570511, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Купаловская', 53.901360, 27.560862, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Немига', 53.905665, 27.553820, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Фрунзенская', 53.905250, 27.539073, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Молодёжная', 55.740148, 37.418239, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Пушкинская', 53.908941, 27.490091, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Спортивная', 53.908364, 27.481928, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Кунцевщина', 53.906615, 27.454331, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Каменная Горка', 53.906725, 27.437702, 0);

insert into luckydrive.point(address, lat, lng, radius) values ('Уручье', 53.945101, 27.688183, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Борисовский тракт', 53.938417, 27.666320, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Восток', 53.934355, 27.651449, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Московская', 53.927851, 27.627906, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Парк Челюскинцев', 53.924490, 27.615354, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Академия наук', 53.922034, 27.600529, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Площадь Якуба Коласа', 53.915478, 27.583166, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Площадь Победы', 53.908123, 27.575439, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Октябрьская', 53.901761, 27.560927, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Площадь Ленина', 53.894895, 27.547928, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Институт культуры', 53.885236, 27.539472, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Грушевка', 53.886678, 27.514658, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Михалово', 53.876944, 27.497112, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Петровщина', 53.864064, 27.485376, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Малиновка', 53.849434, 27.474933, 0);

insert into luckydrive.point(address, lat, lng, radius) values ('Белинского 11 здание 77/73, Минск, 220113, Минск', 53.934860, 27.603523, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Советский район, Минск', 53.938894, 27.598775, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('ул. Можайского 3, Минск', 53.940348, 27.584865, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('ул. Неждановой 5, Минск', 53.942489, 27.577486, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('ул. Гамарника 20, Минск', 53.957257, 27.612569, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('ул. Седых 6, Минск', 53.952957, 27.622641, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('улица Столетова 7, Минск', 53.907346, 27.624805, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('2-й пер. Багратиона 36, Минск', 53.910153, 27.625348, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Партизанский просп. 88, Минск', 53.868758, 27.645422, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('Партизанский просп. 58, Минск', 53.873726, 27.632241, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('улица Шишкина 28, Минск', 53.859181, 27.632713, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('проезд Подшипниковый 9, Минск', 53.865110, 27.627989, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('ул. Алеся Гаруна 24, Минск', 53.922898, 27.446482, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('ул. Алеся Гаруна 30, Минск', 53.924623, 27.446922, 0);
insert into luckydrive.point(address, lat, lng, radius) values ('улица Кунцевщина 44, Минск', 53.915513, 27.439153, 0);

insert into luckydrive.tag(tag_name, tag_point_id) values ('Mogilevskaya', 2);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Autozavodskaya', 3);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Partizanskaya', 4);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Tractorny zavod', 5);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Proletarskaya', 6);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Pervomayskaya', 7);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Kupalovskaya', 8);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Nemiga', 9);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Frunzenskaya', 10);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Molodzezhnaya', 11);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Pushkinskaya', 12);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Sportivnaya', 13);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Kuncevshchina', 14);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Kammennaya Gorka', 15);

insert into luckydrive.tag(tag_name, tag_point_id) values ('Uruchye', 16);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Borisovsky trakt', 17);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Vostok', 18);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Moskovskaya', 19);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Park Chelyuskincev', 20);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Akademia Nauk', 21);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Ploshchad Yakuba Kolasa', 22);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Ploshchad Pobedy', 23);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Oktyabrskaya', 24);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Ploshchad Lenina', 25);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Insitut kultury', 26);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Grushevka', 27);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Mihalovo', 28);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Petrovshchina', 29);
insert into luckydrive.tag(tag_name, tag_point_id) values ('Malinovka', 30);

insert into luckydrive.trip(driver_id, start_point_id, end_point_id, date_of_departure, departure_time_lower_bound, departure_time_upper_bound, limit_number_of_passengers, status) values (1, 1, 37, '2019-02-02', '18:30:00', '19:30:00', 5, 'ACTIVE');
insert into luckydrive.trip(driver_id, start_point_id, end_point_id, date_of_departure, departure_time_lower_bound, departure_time_upper_bound, limit_number_of_passengers, status) values (2, 1, 39, '2019-02-02', '17:00:00', '17:30:00', 5, 'ACTIVE');
insert into luckydrive.trip(driver_id, start_point_id, end_point_id, date_of_departure, departure_time_lower_bound, departure_time_upper_bound, limit_number_of_passengers, status) values (5, 1, 45, '2019-02-02', '16:00:00', '16:05:00', 5, 'ACTIVE');
insert into luckydrive.trip(driver_id, start_point_id, end_point_id, date_of_departure, departure_time_lower_bound, departure_time_upper_bound, limit_number_of_passengers, status) values (7, 1, 42, '2019-02-02', '18:00:00', '18:15:00', 5, 'ACTIVE');
insert into luckydrive.trip(driver_id, start_point_id, end_point_id, date_of_departure, departure_time_lower_bound, departure_time_upper_bound, limit_number_of_passengers, status) values (9, 1, 43, '2019-02-02', '19:55:00', '20:00:00', 5, 'ACTIVE');
insert into luckydrive.trip(driver_id, start_point_id, end_point_id, date_of_departure, departure_time_lower_bound, departure_time_upper_bound, limit_number_of_passengers, status) values (12, 1, 44, '2019-02-02', '21:00:00', '21:05:00', 5, 'ACTIVE');

insert into luckydrive.trip_tag_connect(trip_id, tag_id) values (1, 19);
insert into luckydrive.trip_tag_connect(trip_id, tag_id) values (1, 20);

insert into luckydrive.trip_tag_connect(trip_id, tag_id) values (2, 15);
insert into luckydrive.trip_tag_connect(trip_id, tag_id) values (2, 3);
insert into luckydrive.trip_tag_connect(trip_id, tag_id) values (2, 8);


insert into luckydrive.trip_tag_connect(trip_id, tag_id) values (3, 13);
insert into luckydrive.trip_tag_connect(trip_id, tag_id) values (3, 15);

insert into luckydrive.trip_tag_connect(trip_id, tag_id) values (4, 3);
insert into luckydrive.trip_tag_connect(trip_id, tag_id) values (4, 15);

insert into luckydrive.trip_tag_connect(trip_id, tag_id) values (5, 15);













