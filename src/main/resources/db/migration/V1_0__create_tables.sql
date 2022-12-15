create table person
(
    id              bigserial   primary key,
    name            varchar(50) not null,
    surname         varchar(50) not null,
    patronymic      varchar(50),
    phone_number    varchar(17) not null unique,
    inn             bigint      unique check (inn > 0)
)
;

create sequence bank_account_seq minvalue 100000000000 maxvalue 999999999999;

create table bank_account
(
    id              bigint          primary key default nextval('bank_account_seq'),
    sum_of_money    numeric(14,2)   not null,
    person_id       bigint          not null references person on delete cascade,
    constraint valid_bank_account_range check (id >= 100000000000 and id <= 999999999999)
)
;

alter sequence bank_account_seq owned by bank_account.id;

create table payment
(
    id                  bigserial       primary key,
    credit_account      bigint          not null references bank_account on delete cascade,
    debit_account       bigint          not null references bank_account on delete cascade,
    transaction_amount  numeric(14,2)   not null check (transaction_amount > 0),
    write_off_period    bigint          not null, --период списания в минутах
    constraint check_for_identical_accounts check (credit_account != debit_account)
)
;

create table journal_entry
(
    id              bigserial   primary key,
    payment_id      bigint      not null references payment on delete cascade,
    payment_result  boolean     not null,
    payment_date    timestamp   not null default current_timestamp
)
;
