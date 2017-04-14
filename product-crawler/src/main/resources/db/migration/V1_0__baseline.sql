
create table product
(
	uuid uuid not null
		constraint product_pkey
			primary key,
	name text not null,
	brand text,
	model text,
	description text,
	price real,
	category text,
	url text not null
		constraint product_url_key
			unique,
	update_time timestamp default now() not null
)
;

create function record_price_history() returns trigger
	language plpgsql
as $$
BEGIN
        IF NEW.price <> OLD.price THEN
        	INSERT INTO price_history (uuid, price, time) VALUES (OLD.uuid, OLD.price, OLD.update_time);
        END IF;

    	NEW.update_time = current_timestamp;
    	RETURN NEW;
	END;
$$
;

create trigger on_price_update
	before update
	on product
	for each row
	execute procedure record_price_history()
;

create table price_history
(
	uuid uuid not null
		constraint price_history_uuid_fkey
			references product
				on update cascade on delete cascade,
	price real,
	time timestamp default now()
)
;

create unique index price_history_uuid_date_pk
	on price_history (uuid, time)
;


