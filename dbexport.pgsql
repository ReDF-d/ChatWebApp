--
-- PostgreSQL database dump
--

-- Dumped from database version 11.2
-- Dumped by pg_dump version 11.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: blocked_users; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.blocked_users (
    id integer NOT NULL,
    user_id integer,
    started timestamp without time zone,
    ends timestamp without time zone
);


ALTER TABLE public.blocked_users OWNER TO root;

--
-- Name: blocked_users_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.blocked_users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.blocked_users_id_seq OWNER TO root;

--
-- Name: blocked_users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.blocked_users_id_seq OWNED BY public.blocked_users.id;


--
-- Name: friendship; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.friendship (
    id integer NOT NULL,
    user_id_1 integer,
    user_id_2 integer,
    status character varying,
    last_action integer
);


ALTER TABLE public.friendship OWNER TO root;

--
-- Name: friendship_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.friendship_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.friendship_id_seq OWNER TO root;

--
-- Name: friendship_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.friendship_id_seq OWNED BY public.friendship.id;


--
-- Name: message_ids; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.message_ids
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.message_ids OWNER TO root;

--
-- Name: messages; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.messages (
    message_text character varying(4096),
    "time" timestamp without time zone,
    message_id integer NOT NULL,
    username character varying(25),
    user_id integer,
    room_id integer
);


ALTER TABLE public.messages OWNER TO root;

--
-- Name: messages_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.messages_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.messages_id_seq OWNER TO root;

--
-- Name: messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.messages_id_seq OWNED BY public.messages.message_id;


--
-- Name: role_ids; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.role_ids
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.role_ids OWNER TO root;

--
-- Name: roles; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.roles (
    id integer DEFAULT nextval('public.role_ids'::regclass) NOT NULL,
    role character varying(45) NOT NULL
);


ALTER TABLE public.roles OWNER TO root;

--
-- Name: room_members; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.room_members (
    room_id integer,
    user_id integer
);


ALTER TABLE public.room_members OWNER TO root;

--
-- Name: rooms; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.rooms (
    id integer NOT NULL,
    room_type character varying,
    title character varying(45)
);


ALTER TABLE public.rooms OWNER TO root;

--
-- Name: rooms_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.rooms_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rooms_id_seq OWNER TO root;

--
-- Name: rooms_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.rooms_id_seq OWNED BY public.rooms.id;


--
-- Name: user_ids; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.user_ids
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_ids OWNER TO root;

--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.user_roles (
    role_id integer,
    user_id integer
);


ALTER TABLE public.user_roles OWNER TO root;

--
-- Name: users; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.users (
    login character(64) NOT NULL,
    password character(128) NOT NULL,
    username character varying(25),
    id integer DEFAULT nextval('public.user_ids'::regclass) NOT NULL,
    locked boolean
);


ALTER TABLE public.users OWNER TO root;

--
-- Name: blocked_users id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.blocked_users ALTER COLUMN id SET DEFAULT nextval('public.blocked_users_id_seq'::regclass);


--
-- Name: friendship id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.friendship ALTER COLUMN id SET DEFAULT nextval('public.friendship_id_seq'::regclass);


--
-- Name: messages message_id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.messages ALTER COLUMN message_id SET DEFAULT nextval('public.messages_id_seq'::regclass);


--
-- Name: rooms id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.rooms ALTER COLUMN id SET DEFAULT nextval('public.rooms_id_seq'::regclass);


--
-- Data for Name: blocked_users; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.blocked_users (id, user_id, started, ends) FROM stdin;
4	3	2019-12-07 12:26:00	2019-12-07 13:26:00
6	3	2019-12-10 15:12:00	2019-12-10 15:13:00
11	3	2019-12-19 15:00:00	\N
12	3	2019-12-26 12:26:00	\N
13	3	2019-12-26 12:27:00	2019-12-26 12:28:00
14	8	2020-02-03 15:17:00	2020-02-03 15:18:00
15	3	2020-02-03 17:36:00	2020-02-03 17:38:00
16	3	2020-02-03 18:11:00	2020-02-03 18:13:00
17	3	2020-02-03 18:24:00	\N
18	3	2020-02-03 18:28:00	\N
19	3	2020-02-03 18:29:00	1970-01-12 18:46:39.999
20	3	2020-02-03 18:32:00	1970-01-12 18:46:39.999
22	3	2020-02-03 18:45:00	\N
\.


--
-- Data for Name: friendship; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.friendship (id, user_id_1, user_id_2, status, last_action) FROM stdin;
9	2	3	friends	2
10	2	8	friends	2
\.


--
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.messages (message_text, "time", message_id, username, user_id, room_id) FROM stdin;
ass	2020-01-24 21:39:59.44	453	root	2	\N
s	2020-01-24 21:53:59.44	454	root	2	\N
yo	2020-01-28 01:23:16.904	458	root	2	10
123	2020-02-03 20:55:22.802	474	root	2	10
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.roles (id, role) FROM stdin;
1	USER
2	ADMIN
\.


--
-- Data for Name: room_members; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.room_members (room_id, user_id) FROM stdin;
10	2
10	3
19	3
19	2
19	8
1	8
1	9
1	2
1	3
1	11
1	12
\.


--
-- Data for Name: rooms; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.rooms (id, room_type, title) FROM stdin;
10	dialogue	\N
19	group	test, root, user
1	group	Общий чат
\.


--
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.user_roles (role_id, user_id) FROM stdin;
1	8
1	2
2	2
1	12
1	3
1	9
1	11
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.users (login, password, username, id, locked) FROM stdin;
g@g.com                                                         	$2a$10$f1gzX2QZsMCGb/FNlEU34u6IIrmONpAeO1Rgj9UwqSRnL0AIhelVC                                                                    	ggg	11	f
u@u.com                                                         	$2a$10$iMUpiirOyg5izrUDtZ8WSu1M.uG4vrg0PNHYAfNVAOpSSzCn5nJgW                                                                    	user	8	f
root@root.com                                                   	$2a$10$e4tmzuYD/sjCrruFwhHAtOgu10MSJy9q7sfyexTGosFlwR.RWxLke                                                                    	root	2	f
lol@mail.ru                                                     	$2a$10$KM3dbJN.xIR7zr/CwIa/j.YPKx/SM3PW3VORjf2IF3CyOCLZOutcm                                                                    	sendnudes	12	f
test@test.com                                                   	$2a$10$RRQOVch81IDpJUs8ZK99T.tVK0Sn2JEAcKvelVwQrx.uLpL4bgTqu                                                                    	test	3	f
a@a.com                                                         	$2a$10$rGd3DsZ.ou25pM9wEwury.5zb8WOyYXcG1YOZchMElAKM4fydPwmO                                                                    	test1	9	f
\.


--
-- Name: blocked_users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.blocked_users_id_seq', 22, true);


--
-- Name: friendship_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.friendship_id_seq', 10, true);


--
-- Name: message_ids; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.message_ids', 476, true);


--
-- Name: messages_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.messages_id_seq', 1, true);


--
-- Name: role_ids; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.role_ids', 1, false);


--
-- Name: rooms_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.rooms_id_seq', 20, true);


--
-- Name: user_ids; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.user_ids', 12, true);


--
-- Name: blocked_users blocked_users_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.blocked_users
    ADD CONSTRAINT blocked_users_pkey PRIMARY KEY (id);


--
-- Name: friendship friendship_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.friendship
    ADD CONSTRAINT friendship_pkey PRIMARY KEY (id);


--
-- Name: users login_unique; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT login_unique UNIQUE (login);


--
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (message_id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: rooms rooms_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: blocked_users blocked_users_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.blocked_users
    ADD CONSTRAINT blocked_users_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: friendship friendship_user_id_1_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.friendship
    ADD CONSTRAINT friendship_user_id_1_fkey FOREIGN KEY (user_id_1) REFERENCES public.users(id);


--
-- Name: friendship friendship_user_id_2_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.friendship
    ADD CONSTRAINT friendship_user_id_2_fkey FOREIGN KEY (user_id_2) REFERENCES public.users(id);


--
-- Name: messages messages_room_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.rooms(id) ON DELETE CASCADE;


--
-- Name: messages messages_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: room_members room_members_room_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.room_members
    ADD CONSTRAINT room_members_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.rooms(id) ON DELETE CASCADE;


--
-- Name: room_members room_members_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.room_members
    ADD CONSTRAINT room_members_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: user_roles user_roles_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: user_roles user_roles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

