-- public.merchants definition

-- Drop table

-- DROP TABLE public.merchants;

CREATE table if not exists public.merchants (
	id uuid NOT NULL,
	merchant_name varchar(30) NULL,
	"open" bool NULL,

	is_deleted bool NOT NULL,
	created_at timestamp(6) NOT NULL,
	deleted_at timestamp(6) NULL,
	updated_at timestamp(6) NULL,

	CONSTRAINT merchants_pkey PRIMARY KEY (id)
);


-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE if not exists public.users (
	id uuid NOT NULL,
	"password" varchar(255) NULL,
	username varchar(20) NULL,

	is_deleted bool NOT NULL,
	created_at timestamp(6) NOT NULL,
	deleted_at timestamp(6) NULL,
	updated_at timestamp(6) NULL,

	CONSTRAINT users_pkey PRIMARY KEY (id)
);


-- public.wallets definition

-- Drop table

-- DROP TABLE public.wallets;

CREATE TABLE if not exists public.wallets (
	id uuid NOT NULL,
	balance float8 NULL,
	"type" varchar(255) NULL,

	is_deleted bool NOT NULL,
	created_at timestamp(6) NOT NULL,
	deleted_at timestamp(6) NULL,
	updated_at timestamp(6) NULL,

	CONSTRAINT wallets_pkey PRIMARY KEY (id),
	CONSTRAINT wallets_type_check CHECK (((type)::text = ANY ((ARRAY['USER'::character varying, 'MERCHANT'::character varying])::text[])))
);


-- public.merchant_details definition

-- Drop table

-- DROP TABLE public.merchant_details;

CREATE TABLE if not exists public.merchant_details (
	id uuid NOT NULL,
	merchant_location varchar(255) NULL,
	merchant_type varchar(255) NULL,
	phone_number varchar(255) NULL,
	merchant_id uuid NOT NULL,
	wallet_id uuid NOT NULL,

	is_deleted bool NOT NULL,
	created_at timestamp(6) NOT NULL,
	deleted_at timestamp(6) NULL,
	updated_at timestamp(6) NULL,

	CONSTRAINT merchant_details_merchant_type_check CHECK (((merchant_type)::text = ANY ((ARRAY['REGULAR'::character varying, 'PLUS'::character varying, 'SULTAN'::character varying])::text[]))),
	CONSTRAINT merchant_details_pkey PRIMARY KEY (id),
	CONSTRAINT merchant_details_wallets_must_unique UNIQUE (wallet_id),
	CONSTRAINT merchant_details_merchants_must_unique UNIQUE (merchant_id),
	CONSTRAINT merchant_details_merchants_fkey FOREIGN KEY (merchant_id) REFERENCES public.merchants(id),
	CONSTRAINT merchant_details_wallets_fkey FOREIGN KEY (wallet_id) REFERENCES public.wallets(id)
);


-- public.orders definition

-- Drop table

-- DROP TABLE public.orders;

CREATE table if not exists public.orders (
	id uuid NOT NULL,
	completed bool NULL,
	destination_address varchar(255) NULL,
	order_time timestamp(6) NULL,
	user_id uuid NOT NULL,

	is_deleted bool NOT NULL,
	created_at timestamp(6) NOT NULL,
	deleted_at timestamp(6) NULL,
	updated_at timestamp(6) NULL,

	CONSTRAINT orders_pkey PRIMARY KEY (id),
	CONSTRAINT orders_users_fkey FOREIGN KEY (user_id) REFERENCES public.users(id)
);


-- public.products definition

-- Drop table

-- DROP TABLE public.products;

CREATE TABLE if not exists public.products (
	id uuid NOT NULL,
	price float8 NULL,
	product_name varchar(255) NULL,
	merchant_id uuid NOT NULL,

	is_deleted bool NOT NULL,
	created_at timestamp(6) NOT NULL,
	deleted_at timestamp(6) NULL,
	updated_at timestamp(6) NULL,

	CONSTRAINT products_pkey PRIMARY KEY (id),
	CONSTRAINT products_merchants_fkey FOREIGN KEY (merchant_id) REFERENCES public.merchants(id)
);


-- public.user_details definition

-- Drop table

-- DROP TABLE public.user_details;

CREATE TABLE if not exists public.user_details (
	id uuid NOT NULL,
	email_address varchar(255) NULL,
	is_verified bool NULL,
	phone_number varchar(255) NULL,
	user_id uuid NOT NULL,
	wallet_id uuid NOT NULL,

	is_deleted bool NOT NULL,
	created_at timestamp(6) NOT NULL,
	deleted_at timestamp(6) NULL,
	updated_at timestamp(6) NULL,

	CONSTRAINT user_details_wallets_must_unique UNIQUE (wallet_id),
	CONSTRAINT user_details_users_must_unique UNIQUE (user_id),
	CONSTRAINT user_details_pkey PRIMARY KEY (id),
	CONSTRAINT user_details_users_fkey FOREIGN KEY (user_id) REFERENCES public.users(id),
	CONSTRAINT user_details_wallets_fkey FOREIGN KEY (wallet_id) REFERENCES public.wallets(id)
);


-- public.order_details definition

-- Drop table

-- DROP TABLE public.order_details;

CREATE TABLE if not exists public.order_details (
	id uuid NOT NULL,
	quantity int4 NULL,
	total_price float8 NULL,
	order_id uuid NOT NULL,
	product_id uuid NOT NULL,

	is_deleted bool NOT NULL,
	created_at timestamp(6) NOT NULL,
	deleted_at timestamp(6) NULL,
	updated_at timestamp(6) NULL,

	CONSTRAINT order_details_pkey PRIMARY KEY (id),
	CONSTRAINT order_details_products_fkey FOREIGN KEY (product_id) REFERENCES public.products(id),
	CONSTRAINT order_details_orders_fkey FOREIGN KEY (order_id) REFERENCES public.orders(id)
);