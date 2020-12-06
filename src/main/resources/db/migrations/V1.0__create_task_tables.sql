-- Author: Vladislav Valitsky

-- Step #1: Creating stm schema
create schema if not exists stm;

-- Step #2: Creating process_task
create table stm.process_task
(
    id bigserial not null,
    name varchar(256),
    primary key (id)
);

-- Step #3: Creating process_task_state
create table stm.process_task_state
(
    id bigserial not null,
    process_task_id bigint not null,
    state varchar(256) not null,
    state_context bytea not null,
    primary key (id),
    foreign key (process_task_id) references stm.process_task (id)
);


