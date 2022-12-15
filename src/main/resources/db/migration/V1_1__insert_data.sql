insert into person(surname, name, patronymic, phone_number, inn)
values
    ('Бочаров', 'Владислав', 'Глебович', '+380996540532', 694210568726),
    ('Гончарова', 'Арина', 'Константиновна', '+380670291574', 659401245060),
    ('Гусев', 'Борис', null, '+380933261502', 912502364872),
    ('Королева', 'Ксения', 'Александровна', '+380503252204', 206544879102),
    ('Петров', 'Роман', 'Максимович', '+380633026054', 540326985212),
    ('Постникова', 'Алина', null, '+380978540263', 740256319582),
    ('Семенов', 'Лев', 'Тимофеевич', '+380995920604', 632014807865),
    ('Сорокин', 'Даниил', 'Максимович', '+380679843587', 315048069715),
    ('Степанова', 'Полина', 'Игоревна', '+380569453087', 845102360984),
    ('Яковлева', 'Татьяна', 'Артёмовна', '+380442305796', 426058931483)
;

insert into bank_account(sum_of_money, person_id)
values
    (123.82, (select id from person
              where phone_number = '+380996540532')),
    (0, (select id from person
         where phone_number = '+380996540532')),
    (1000, (select id from person
            where phone_number = '+380933261502')),
    (8294.90, (select id from person
               where phone_number = '+380503252204')),
    (-10000, (select id from person
              where phone_number = '+380633026054')),
    (100500, (select id from person
              where phone_number = '+380633026054')),
    (0, (select id from person
         where phone_number = '+380633026054')),
    (8023.01, (select id from person
               where phone_number = '+380679843587')),
    (-100.75, (select id from person
               where phone_number = '+380679843587')),
    (0, (select id from person
         where phone_number = '+380569453087'))
;

insert into payment(credit_account, debit_account, transaction_amount, write_off_period)
values
    (
        (select id from bank_account limit 1 offset 5),
        (select id from bank_account limit 1 offset 7),
        234,
        3
    ),
    (
        (select id from bank_account limit 1 offset 9),
        (select id from bank_account limit 1),
        5294.38,
        1
    ),
    (
        (select id from bank_account limit 1 offset 1),
        (select id from bank_account limit 1 offset 4),
        10000,
        4
    )
;