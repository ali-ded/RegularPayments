create schema if not exists payments;
set search_path=payments;

create table if not exists person
(
    id bigserial primary key,
    name varchar(50) not null,
    surname varchar(50) not null,
    patronymic varchar(50),
    phone_number varchar(15) not null unique,
    inn bigint check (inn > 0)
    )
;

create sequence if not exists bank_account_seq minvalue 100000000000 maxvalue 999999999999;

create table if not exists bank_account
(
    id bigint primary key default nextval('bank_account_seq'),
    sum_of_money money not null,
    person_id bigint not null references person on delete cascade,
    constraint valid_bank_account_range check (id >= 100000000000 and id <= 999999999999)
    )
;

alter sequence bank_account_seq owned by bank_account.id;

create table if not exists payment
(
    id bigserial primary key,
    credit_account bigint not null references bank_account on delete cascade,
    debit_account bigint not null references bank_account on delete cascade,
    transaction_amount money not null,
    constraint check_for_identical_accounts check (credit_account != debit_account)
    )
;

create table if not exists journal_entry
(
    id bigserial primary key,
    payment_id bigint not null references payment on delete cascade,
    payment_result boolean not null,
    payment_date timestamp not null default current_timestamp
)
;
