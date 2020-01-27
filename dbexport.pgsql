--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.19
-- Dumped by pg_dump version 9.6.15

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


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
    status character varying
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
    user_id integer
);


ALTER TABLE public.messages OWNER TO root;

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
-- Data for Name: blocked_users; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.blocked_users (id, user_id, started, ends) FROM stdin;
4	3	2019-12-07 12:26:00	2019-12-07 13:26:00
6	3	2019-12-10 15:12:00	2019-12-10 15:13:00
7	4	2019-12-10 15:56:00	2019-12-10 15:58:00
8	4	2019-12-10 16:48:00	2019-12-10 16:49:00
9	4	2019-12-12 16:03:00	2019-12-12 16:03:00
10	4	2019-12-12 17:15:00	2019-12-12 17:17:00
\.


--
-- Name: blocked_users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.blocked_users_id_seq', 10, true);


--
-- Data for Name: friendship; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.friendship (id, user_id_1, user_id_2, status) FROM stdin;
\.


--
-- Name: friendship_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.friendship_id_seq', 1, false);


--
-- Name: message_ids; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.message_ids', 443, true);


--
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.messages (message_text, "time", message_id, username, user_id) FROM stdin;
test	2019-10-24 23:29:29.974	429	test	3
21	2019-12-05 16:21:19.085	442	root	2
1	2019-10-25 00:07:58.932	437	root	2
456	2019-10-25 00:03:47.604	432	root	2
1	2019-10-25 00:12:51.337	440	root	2
10	2019-10-25 00:06:40.815	435	root	2
1	2019-10-25 00:06:40.815	436	root	2
2	2019-10-25 00:12:51.337	441	root	2
123	2019-10-25 00:02:56.216	430	root	2
9	2019-10-25 00:06:35.568	434	root	2
1	2019-10-25 00:08:03.552	438	root	2
1	2019-10-25 00:12:51.337	439	root	2
78	2019-10-25 00:03:56.243	433	root	2
321	2019-10-25 00:03:00.161	431	root	2
o	2019-12-10 15:55:12.735	443	test2	4
\.


--
-- Name: role_ids; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.role_ids', 1, false);


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.roles (id, role) FROM stdin;
2	ADMIN
1	USER
\.


--
-- Name: user_ids; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.user_ids', 4, true);


--
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.user_roles (role_id, user_id) FROM stdin;
1	2
2	2
1	3
1	4
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.users (login, password, username, id, locked) FROM stdin;
root@root.com                                                   	$2a$10$PPqJv4ZJtoA69JGqVb0xbOX48iCnU7W.VLuGliXOIPMrv4ZzEg4.G                                                                    	root	2	f
test2@test.com                                                  	$2a$10$1VeSBHU9wT7RTDocP0Ej7.BNt8xnk7NxESzH20811tCurwge5lnqG                                                                    	test2	4	f
test@test.com                                                   	$2a$10$RRQOVch81IDpJUs8ZK99T.tVK0Sn2JEAcKvelVwQrx.uLpL4bgTqu                                                                    	test	3	f
\.


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
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: blocked_users blocked_users_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.blocked_users
    ADD CONSTRAINT blocked_users_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


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
-- Name: messages messages_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


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
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

