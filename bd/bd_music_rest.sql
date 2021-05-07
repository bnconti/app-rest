--
-- PostgreSQL database dump
--

-- Dumped from database version 13.2
-- Dumped by pg_dump version 13.2

-- Started on 2021-05-07 12:22:03

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 203 (class 1259 OID 16524)
-- Name: playlists; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.playlists (
    id integer NOT NULL,
    user_id integer NOT NULL,
    name text NOT NULL
);


ALTER TABLE public.playlists OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 16522)
-- Name: playlists_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.playlists_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.playlists_id_seq OWNER TO postgres;

--
-- TOC entry 3029 (class 0 OID 0)
-- Dependencies: 202
-- Name: playlists_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.playlists_id_seq OWNED BY public.playlists.id;


--
-- TOC entry 207 (class 1259 OID 16551)
-- Name: playlists_songs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.playlists_songs (
    id integer NOT NULL,
    playlist_id integer NOT NULL,
    song_id integer NOT NULL
);


ALTER TABLE public.playlists_songs OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 16549)
-- Name: playlists_songs_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.playlists_songs_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.playlists_songs_id_seq OWNER TO postgres;

--
-- TOC entry 3030 (class 0 OID 0)
-- Dependencies: 206
-- Name: playlists_songs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.playlists_songs_id_seq OWNED BY public.playlists_songs.id;


--
-- TOC entry 205 (class 1259 OID 16540)
-- Name: songs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.songs (
    id integer NOT NULL,
    name text NOT NULL,
    author text NOT NULL,
    genre text NOT NULL
);


ALTER TABLE public.songs OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 16538)
-- Name: songs_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.songs_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.songs_id_seq OWNER TO postgres;

--
-- TOC entry 3031 (class 0 OID 0)
-- Dependencies: 204
-- Name: songs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.songs_id_seq OWNED BY public.songs.id;


--
-- TOC entry 201 (class 1259 OID 16513)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    email text NOT NULL,
    password text NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 16511)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO postgres;

--
-- TOC entry 3032 (class 0 OID 0)
-- Dependencies: 200
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 2872 (class 2604 OID 16527)
-- Name: playlists id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.playlists ALTER COLUMN id SET DEFAULT nextval('public.playlists_id_seq'::regclass);


--
-- TOC entry 2874 (class 2604 OID 16554)
-- Name: playlists_songs id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.playlists_songs ALTER COLUMN id SET DEFAULT nextval('public.playlists_songs_id_seq'::regclass);


--
-- TOC entry 2873 (class 2604 OID 16543)
-- Name: songs id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.songs ALTER COLUMN id SET DEFAULT nextval('public.songs_id_seq'::regclass);


--
-- TOC entry 2871 (class 2604 OID 16516)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 3019 (class 0 OID 16524)
-- Dependencies: 203
-- Data for Name: playlists; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.playlists (id, user_id, name) FROM stdin;
1	1	Lista de Bruno
\.


--
-- TOC entry 3023 (class 0 OID 16551)
-- Dependencies: 207
-- Data for Name: playlists_songs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.playlists_songs (id, playlist_id, song_id) FROM stdin;
1	1	1
2	1	2
3	1	3
\.


--
-- TOC entry 3021 (class 0 OID 16540)
-- Dependencies: 205
-- Data for Name: songs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.songs (id, name, author, genre) FROM stdin;
2	Pibe cantina	Yerba brava	Cumbia
3	I Shoot The Sheriff	Bob Marley	Reggae
1	El 38	Divididos	Rock
\.


--
-- TOC entry 3017 (class 0 OID 16513)
-- Dependencies: 201
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, email, password) FROM stdin;
1	bruno@yahoo.com	3
\.


--
-- TOC entry 3033 (class 0 OID 0)
-- Dependencies: 202
-- Name: playlists_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.playlists_id_seq', 1, true);


--
-- TOC entry 3034 (class 0 OID 0)
-- Dependencies: 206
-- Name: playlists_songs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.playlists_songs_id_seq', 3, true);


--
-- TOC entry 3035 (class 0 OID 0)
-- Dependencies: 204
-- Name: songs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.songs_id_seq', 3, true);


--
-- TOC entry 3036 (class 0 OID 0)
-- Dependencies: 200
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 1, true);


--
-- TOC entry 2878 (class 2606 OID 16532)
-- Name: playlists playlists_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.playlists
    ADD CONSTRAINT playlists_pkey PRIMARY KEY (id);


--
-- TOC entry 2882 (class 2606 OID 16556)
-- Name: playlists_songs playlists_songs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.playlists_songs
    ADD CONSTRAINT playlists_songs_pkey PRIMARY KEY (id);


--
-- TOC entry 2880 (class 2606 OID 16548)
-- Name: songs songs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.songs
    ADD CONSTRAINT songs_pkey PRIMARY KEY (id);


--
-- TOC entry 2876 (class 2606 OID 16521)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 2884 (class 2606 OID 16557)
-- Name: playlists_songs playlists_songs_playlist_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.playlists_songs
    ADD CONSTRAINT playlists_songs_playlist_id_fkey FOREIGN KEY (playlist_id) REFERENCES public.playlists(id);


--
-- TOC entry 2885 (class 2606 OID 16562)
-- Name: playlists_songs playlists_songs_song_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.playlists_songs
    ADD CONSTRAINT playlists_songs_song_id_fkey FOREIGN KEY (song_id) REFERENCES public.songs(id);


--
-- TOC entry 2883 (class 2606 OID 16533)
-- Name: playlists playlists_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.playlists
    ADD CONSTRAINT playlists_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


-- Completed on 2021-05-07 12:22:03

--
-- PostgreSQL database dump complete
--

