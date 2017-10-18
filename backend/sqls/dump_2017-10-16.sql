--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.5
-- Dumped by pg_dump version 9.5.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
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


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: auth_group; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE auth_group (
    id integer NOT NULL,
    name character varying(80) NOT NULL
);


ALTER TABLE auth_group OWNER TO ben;

--
-- Name: auth_group_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE auth_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE auth_group_id_seq OWNER TO ben;

--
-- Name: auth_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE auth_group_id_seq OWNED BY auth_group.id;


--
-- Name: auth_group_permissions; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE auth_group_permissions (
    id integer NOT NULL,
    group_id integer NOT NULL,
    permission_id integer NOT NULL
);


ALTER TABLE auth_group_permissions OWNER TO ben;

--
-- Name: auth_group_permissions_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE auth_group_permissions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE auth_group_permissions_id_seq OWNER TO ben;

--
-- Name: auth_group_permissions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE auth_group_permissions_id_seq OWNED BY auth_group_permissions.id;


--
-- Name: auth_permission; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE auth_permission (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    content_type_id integer NOT NULL,
    codename character varying(100) NOT NULL
);


ALTER TABLE auth_permission OWNER TO ben;

--
-- Name: auth_permission_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE auth_permission_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE auth_permission_id_seq OWNER TO ben;

--
-- Name: auth_permission_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE auth_permission_id_seq OWNED BY auth_permission.id;


--
-- Name: auth_user; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE auth_user (
    id integer NOT NULL,
    password character varying(128) NOT NULL,
    last_login timestamp with time zone,
    is_superuser boolean NOT NULL,
    username character varying(150) NOT NULL,
    first_name character varying(30) NOT NULL,
    last_name character varying(30) NOT NULL,
    email character varying(254) NOT NULL,
    is_staff boolean NOT NULL,
    is_active boolean NOT NULL,
    date_joined timestamp with time zone NOT NULL
);


ALTER TABLE auth_user OWNER TO ben;

--
-- Name: auth_user_groups; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE auth_user_groups (
    id integer NOT NULL,
    user_id integer NOT NULL,
    group_id integer NOT NULL
);


ALTER TABLE auth_user_groups OWNER TO ben;

--
-- Name: auth_user_groups_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE auth_user_groups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE auth_user_groups_id_seq OWNER TO ben;

--
-- Name: auth_user_groups_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE auth_user_groups_id_seq OWNED BY auth_user_groups.id;


--
-- Name: auth_user_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE auth_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE auth_user_id_seq OWNER TO ben;

--
-- Name: auth_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE auth_user_id_seq OWNED BY auth_user.id;


--
-- Name: auth_user_user_permissions; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE auth_user_user_permissions (
    id integer NOT NULL,
    user_id integer NOT NULL,
    permission_id integer NOT NULL
);


ALTER TABLE auth_user_user_permissions OWNER TO ben;

--
-- Name: auth_user_user_permissions_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE auth_user_user_permissions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE auth_user_user_permissions_id_seq OWNER TO ben;

--
-- Name: auth_user_user_permissions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE auth_user_user_permissions_id_seq OWNED BY auth_user_user_permissions.id;


--
-- Name: authtoken_token; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE authtoken_token (
    key character varying(40) NOT NULL,
    created timestamp with time zone NOT NULL,
    user_id integer NOT NULL
);


ALTER TABLE authtoken_token OWNER TO ben;

--
-- Name: babycare_appinfo; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE babycare_appinfo (
    id integer NOT NULL,
    version_name character varying(50) NOT NULL,
    version_code integer NOT NULL,
    version_type integer NOT NULL,
    update_info text NOT NULL,
    app_file character varying(100) NOT NULL,
    datetime timestamp with time zone
);


ALTER TABLE babycare_appinfo OWNER TO ben;

--
-- Name: babycare_appinfo_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE babycare_appinfo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE babycare_appinfo_id_seq OWNER TO ben;

--
-- Name: babycare_appinfo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE babycare_appinfo_id_seq OWNED BY babycare_appinfo.id;


--
-- Name: babycare_babyuser; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE babycare_babyuser (
    id integer NOT NULL,
    baby_name character varying(100),
    phone character varying(30),
    gender integer NOT NULL,
    profile character varying(200),
    type integer NOT NULL,
    region character varying(100),
    locale character varying(10),
    whats_up character varying(200),
    zone character varying(50),
    birth date,
    hobbies text,
    highlighted text,
    created timestamp with time zone,
    modified timestamp with time zone,
    is_email_activate boolean NOT NULL,
    is_phone_activate boolean NOT NULL,
    user_id integer NOT NULL
);


ALTER TABLE babycare_babyuser OWNER TO ben;

--
-- Name: babycare_babyuser_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE babycare_babyuser_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE babycare_babyuser_id_seq OWNER TO ben;

--
-- Name: babycare_babyuser_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE babycare_babyuser_id_seq OWNED BY babycare_babyuser.id;


--
-- Name: babycare_comment; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE babycare_comment (
    id integer NOT NULL,
    text text NOT NULL,
    datetime timestamp with time zone,
    baby_id integer NOT NULL,
    event_id integer NOT NULL,
    source_comment_id integer
);


ALTER TABLE babycare_comment OWNER TO ben;

--
-- Name: babycare_comment_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE babycare_comment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE babycare_comment_id_seq OWNER TO ben;

--
-- Name: babycare_comment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE babycare_comment_id_seq OWNED BY babycare_comment.id;


--
-- Name: babycare_event; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE babycare_event (
    id integer NOT NULL,
    type integer NOT NULL,
    title character varying(100),
    content text,
    image1 character varying(200),
    image2 character varying(200),
    image3 character varying(200),
    image4 character varying(200),
    image5 character varying(200),
    image6 character varying(200),
    image7 character varying(200),
    image8 character varying(200),
    image9 character varying(200),
    video_url character varying(200),
    video_width integer,
    video_height integer,
    video_thumbnail character varying(200),
    created timestamp with time zone,
    modified timestamp with time zone,
    baby_id integer NOT NULL
);


ALTER TABLE babycare_event OWNER TO ben;

--
-- Name: babycare_event_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE babycare_event_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE babycare_event_id_seq OWNER TO ben;

--
-- Name: babycare_event_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE babycare_event_id_seq OWNED BY babycare_event.id;


--
-- Name: babycare_feedback; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE babycare_feedback (
    id integer NOT NULL,
    description text NOT NULL,
    image1 character varying(200),
    image2 character varying(200),
    image3 character varying(200),
    image4 character varying(200),
    image5 character varying(200),
    image6 character varying(200),
    image7 character varying(200),
    image8 character varying(200),
    image9 character varying(200),
    created timestamp with time zone,
    modified timestamp with time zone,
    baby_id integer NOT NULL
);


ALTER TABLE babycare_feedback OWNER TO ben;

--
-- Name: babycare_feedback_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE babycare_feedback_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE babycare_feedback_id_seq OWNER TO ben;

--
-- Name: babycare_feedback_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE babycare_feedback_id_seq OWNED BY babycare_feedback.id;


--
-- Name: babycare_iaer; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE babycare_iaer (
    id integer NOT NULL,
    money integer NOT NULL,
    category character varying(30) NOT NULL,
    money_type integer NOT NULL,
    remark character varying(100),
    created timestamp with time zone,
    type integer NOT NULL,
    chart_type integer NOT NULL,
    format character varying(50),
    datetime timestamp with time zone,
    description text,
    timing character varying(100),
    user_id integer NOT NULL
);


ALTER TABLE babycare_iaer OWNER TO ben;

--
-- Name: babycare_iaer_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE babycare_iaer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE babycare_iaer_id_seq OWNER TO ben;

--
-- Name: babycare_iaer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE babycare_iaer_id_seq OWNED BY babycare_iaer.id;


--
-- Name: babycare_like; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE babycare_like (
    id integer NOT NULL,
    datetime timestamp with time zone,
    baby_id integer NOT NULL,
    event_id integer NOT NULL
);


ALTER TABLE babycare_like OWNER TO ben;

--
-- Name: babycare_like_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE babycare_like_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE babycare_like_id_seq OWNER TO ben;

--
-- Name: babycare_like_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE babycare_like_id_seq OWNED BY babycare_like.id;


--
-- Name: babycare_loginlog; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE babycare_loginlog (
    id integer NOT NULL,
    system_type character varying(10) NOT NULL,
    system_version character varying(30) NOT NULL,
    phone_model character varying(30) NOT NULL,
    country character varying(30) NOT NULL,
    state character varying(30) NOT NULL,
    city character varying(30) NOT NULL,
    created timestamp with time zone,
    baby_id integer NOT NULL
);


ALTER TABLE babycare_loginlog OWNER TO ben;

--
-- Name: babycare_loginlog_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE babycare_loginlog_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE babycare_loginlog_id_seq OWNER TO ben;

--
-- Name: babycare_loginlog_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE babycare_loginlog_id_seq OWNED BY babycare_loginlog.id;


--
-- Name: babycare_redenvelope; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE babycare_redenvelope (
    id integer NOT NULL,
    money character varying(10),
    money_type integer NOT NULL,
    money_from character varying(100),
    remark character varying(100),
    created timestamp with time zone,
    baby_id integer NOT NULL
);


ALTER TABLE babycare_redenvelope OWNER TO ben;

--
-- Name: babycare_redenvelope_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE babycare_redenvelope_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE babycare_redenvelope_id_seq OWNER TO ben;

--
-- Name: babycare_redenvelope_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE babycare_redenvelope_id_seq OWNED BY babycare_redenvelope.id;


--
-- Name: babycare_verify; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE babycare_verify (
    id integer NOT NULL,
    email_verify_code character varying(10),
    phone_verify_code character varying(10),
    created timestamp with time zone,
    baby_id integer NOT NULL
);


ALTER TABLE babycare_verify OWNER TO ben;

--
-- Name: babycare_verify_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE babycare_verify_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE babycare_verify_id_seq OWNER TO ben;

--
-- Name: babycare_verify_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE babycare_verify_id_seq OWNED BY babycare_verify.id;


--
-- Name: django_admin_log; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE django_admin_log (
    id integer NOT NULL,
    action_time timestamp with time zone NOT NULL,
    object_id text,
    object_repr character varying(200) NOT NULL,
    action_flag smallint NOT NULL,
    change_message text NOT NULL,
    content_type_id integer,
    user_id integer NOT NULL,
    CONSTRAINT django_admin_log_action_flag_check CHECK ((action_flag >= 0))
);


ALTER TABLE django_admin_log OWNER TO ben;

--
-- Name: django_admin_log_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE django_admin_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE django_admin_log_id_seq OWNER TO ben;

--
-- Name: django_admin_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE django_admin_log_id_seq OWNED BY django_admin_log.id;


--
-- Name: django_content_type; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE django_content_type (
    id integer NOT NULL,
    app_label character varying(100) NOT NULL,
    model character varying(100) NOT NULL
);


ALTER TABLE django_content_type OWNER TO ben;

--
-- Name: django_content_type_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE django_content_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE django_content_type_id_seq OWNER TO ben;

--
-- Name: django_content_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE django_content_type_id_seq OWNED BY django_content_type.id;


--
-- Name: django_migrations; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE django_migrations (
    id integer NOT NULL,
    app character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    applied timestamp with time zone NOT NULL
);


ALTER TABLE django_migrations OWNER TO ben;

--
-- Name: django_migrations_id_seq; Type: SEQUENCE; Schema: public; Owner: ben
--

CREATE SEQUENCE django_migrations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE django_migrations_id_seq OWNER TO ben;

--
-- Name: django_migrations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ben
--

ALTER SEQUENCE django_migrations_id_seq OWNED BY django_migrations.id;


--
-- Name: django_session; Type: TABLE; Schema: public; Owner: ben
--

CREATE TABLE django_session (
    session_key character varying(40) NOT NULL,
    session_data text NOT NULL,
    expire_date timestamp with time zone NOT NULL
);


ALTER TABLE django_session OWNER TO ben;

--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_group ALTER COLUMN id SET DEFAULT nextval('auth_group_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_group_permissions ALTER COLUMN id SET DEFAULT nextval('auth_group_permissions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_permission ALTER COLUMN id SET DEFAULT nextval('auth_permission_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_user ALTER COLUMN id SET DEFAULT nextval('auth_user_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_user_groups ALTER COLUMN id SET DEFAULT nextval('auth_user_groups_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_user_user_permissions ALTER COLUMN id SET DEFAULT nextval('auth_user_user_permissions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_appinfo ALTER COLUMN id SET DEFAULT nextval('babycare_appinfo_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_babyuser ALTER COLUMN id SET DEFAULT nextval('babycare_babyuser_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_comment ALTER COLUMN id SET DEFAULT nextval('babycare_comment_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_event ALTER COLUMN id SET DEFAULT nextval('babycare_event_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_feedback ALTER COLUMN id SET DEFAULT nextval('babycare_feedback_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_iaer ALTER COLUMN id SET DEFAULT nextval('babycare_iaer_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_like ALTER COLUMN id SET DEFAULT nextval('babycare_like_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_loginlog ALTER COLUMN id SET DEFAULT nextval('babycare_loginlog_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_redenvelope ALTER COLUMN id SET DEFAULT nextval('babycare_redenvelope_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_verify ALTER COLUMN id SET DEFAULT nextval('babycare_verify_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY django_admin_log ALTER COLUMN id SET DEFAULT nextval('django_admin_log_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY django_content_type ALTER COLUMN id SET DEFAULT nextval('django_content_type_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ben
--

ALTER TABLE ONLY django_migrations ALTER COLUMN id SET DEFAULT nextval('django_migrations_id_seq'::regclass);


--
-- Data for Name: auth_group; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY auth_group (id, name) FROM stdin;
\.


--
-- Name: auth_group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('auth_group_id_seq', 1, false);


--
-- Data for Name: auth_group_permissions; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY auth_group_permissions (id, group_id, permission_id) FROM stdin;
\.


--
-- Name: auth_group_permissions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('auth_group_permissions_id_seq', 1, false);


--
-- Data for Name: auth_permission; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY auth_permission (id, name, content_type_id, codename) FROM stdin;
1	Can add log entry	1	add_logentry
2	Can change log entry	1	change_logentry
3	Can delete log entry	1	delete_logentry
4	Can add permission	2	add_permission
5	Can change permission	2	change_permission
6	Can delete permission	2	delete_permission
7	Can add user	3	add_user
8	Can change user	3	change_user
9	Can delete user	3	delete_user
10	Can add group	4	add_group
11	Can change group	4	change_group
12	Can delete group	4	delete_group
13	Can add content type	5	add_contenttype
14	Can change content type	5	change_contenttype
15	Can delete content type	5	delete_contenttype
16	Can add session	6	add_session
17	Can change session	6	change_session
18	Can delete session	6	delete_session
19	Can add Token	7	add_token
20	Can change Token	7	change_token
21	Can delete Token	7	delete_token
22	Can add app info	8	add_appinfo
23	Can change app info	8	change_appinfo
24	Can delete app info	8	delete_appinfo
25	Can add like	9	add_like
26	Can change like	9	change_like
27	Can delete like	9	delete_like
28	Can add event	10	add_event
29	Can change event	10	change_event
30	Can delete event	10	delete_event
31	Can add baby user	11	add_babyuser
32	Can change baby user	11	change_babyuser
33	Can delete baby user	11	delete_babyuser
34	Can add login log	12	add_loginlog
35	Can change login log	12	change_loginlog
36	Can delete login log	12	delete_loginlog
37	Can add verify	13	add_verify
38	Can change verify	13	change_verify
39	Can delete verify	13	delete_verify
40	Can add feedback	14	add_feedback
41	Can change feedback	14	change_feedback
42	Can delete feedback	14	delete_feedback
43	Can add comment	15	add_comment
44	Can change comment	15	change_comment
45	Can delete comment	15	delete_comment
46	Can add red envelope	16	add_redenvelope
47	Can change red envelope	16	change_redenvelope
48	Can delete red envelope	16	delete_redenvelope
52	Can add iaer	18	add_iaer
53	Can change iaer	18	change_iaer
54	Can delete iaer	18	delete_iaer
\.


--
-- Name: auth_permission_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('auth_permission_id_seq', 54, true);


--
-- Data for Name: auth_user; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY auth_user (id, password, last_login, is_superuser, username, first_name, last_name, email, is_staff, is_active, date_joined) FROM stdin;
2	pbkdf2_sha256$30000$nzAqq8taQAaE$y9K4AWOxm2sEaWHmdfOjPjldhpx90H6oNNflUo3NGPk=	\N	f	babycare			babycare.ben@gmail.com	t	t	2017-05-26 16:29:52.490171+08
5	pbkdf2_sha256$30000$eDvdYVfHriZq$RY0V8QCr7dfy2CFl68I4xmDc0Hzt3j4piRNB++6C9oM=	\N	f	lisq			455677560@qq.com	t	t	2017-08-04 15:56:20.326447+08
7	pbkdf2_sha256$30000$vcGxU0Ybu44n$koWcE/Z7M8vBR+Kivs5WoZxZi1WChIW7Ax6jVG98kO0=	\N	f	Emily			616897923@qq.com	t	t	2017-10-14 17:11:48.133299+08
1	pbkdf2_sha256$30000$2j8nisBGCA4o$HKfUAjblumCQYZ+li0fVoy3/MDRteQjG+oDojkVAHto=	2017-10-14 20:33:54.206372+08	t	ben			benying1988@gmail.com	t	t	2017-05-26 16:28:41.571397+08
\.


--
-- Data for Name: auth_user_groups; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY auth_user_groups (id, user_id, group_id) FROM stdin;
\.


--
-- Name: auth_user_groups_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('auth_user_groups_id_seq', 1, false);


--
-- Name: auth_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('auth_user_id_seq', 7, true);


--
-- Data for Name: auth_user_user_permissions; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY auth_user_user_permissions (id, user_id, permission_id) FROM stdin;
\.


--
-- Name: auth_user_user_permissions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('auth_user_user_permissions_id_seq', 1, false);


--
-- Data for Name: authtoken_token; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY authtoken_token (key, created, user_id) FROM stdin;
1272dc0fe06c52383c7a9bdfef33255b940c195b	2017-05-26 16:29:52.530902+08	2
d87fd853962ab5cb95a22d063367c24476a3a4ec	2017-08-04 15:56:20.607313+08	5
beefdcf9ef7061c505bda152e5613a7e2eca549f	2017-10-14 17:11:48.17069+08	7
\.


--
-- Data for Name: babycare_appinfo; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY babycare_appinfo (id, version_name, version_code, version_type, update_info, app_file, datetime) FROM stdin;
5	0.2	2	0	1. 修复红包最后一个不显示\r\n2. 修复用户信息后面2个标签页不能移动	apk/2017-06-19 16:30:09/BabyCare0.2.apk	2017-06-19 16:34:06.348313+08
6	0.3	3	0	修复首页动态不能删除的bug	apk/2017-06-24 08:49:51/BabyCare0.3.apk	2017-06-24 08:49:51.623847+08
\.


--
-- Name: babycare_appinfo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('babycare_appinfo_id_seq', 6, true);


--
-- Data for Name: babycare_babyuser; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY babycare_babyuser (id, baby_name, phone, gender, profile, type, region, locale, whats_up, zone, birth, hobbies, highlighted, created, modified, is_email_activate, is_phone_activate, user_id) FROM stdin;
1	嘟嘟	\N	0	https://bensbabycare.oss-cn-hangzhou.aliyuncs.com/babycare/profile/babycare20170616163521_profile.jpg	0	\N	\N	\N	Asia/Shanghai	\N	\N	\N	\N	2017-06-16 16:35:21.743079+08	f	f	2
4	test	\N	0	\N	0	\N	\N	\N	Asia/Shanghai	\N	\N	\N	\N	2017-08-04 15:56:20.591056+08	f	f	5
6	Emily	\N	0	\N	0	\N	\N	\N	Asia/Shanghai	\N	\N	\N	\N	2017-10-14 17:11:48.16766+08	f	f	7
\.


--
-- Name: babycare_babyuser_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('babycare_babyuser_id_seq', 6, true);


--
-- Data for Name: babycare_comment; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY babycare_comment (id, text, datetime, baby_id, event_id, source_comment_id) FROM stdin;
\.


--
-- Name: babycare_comment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('babycare_comment_id_seq', 1, true);


--
-- Data for Name: babycare_event; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY babycare_event (id, type, title, content, image1, image2, image3, image4, image5, image6, image7, image8, image9, video_url, video_width, video_height, video_thumbnail, created, modified, baby_id) FROM stdin;
2	0	\N	9点16分，经历255天的等待，24小时的镇痛，终于在这个666（6月10号，周六，五月十六）的日子迎来了宝宝。\n伟大的妈妈！	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2017-06-10 21:33:55.351245+08	2017-06-10 21:33:55.351683+08	1
4	0		回家咯	https://bensbabycare.oss-cn-hangzhou.aliyuncs.com/babycare/image/babycare20170614100000_event.jpg										\N	\N		2017-06-14 10:00:00.824409+08	2017-06-16 16:46:45.69651+08	1
3	0		谁家的小女婿啊？	https://bensbabycare.oss-cn-hangzhou.aliyuncs.com/babycare/image/babycare20170611073837_event.jpg										\N	\N		2017-06-11 07:38:37.875067+08	2017-06-16 16:47:05.613294+08	1
1	0		人生中最开心的住院和手术就应该是这个了吧！\r\n愿一切顺利！	https://bensbabycare.oss-cn-hangzhou.aliyuncs.com/babycare/image/babycare20170610190210_event.jpg										\N	\N		2017-06-10 19:02:10.336044+08	2017-06-16 16:47:23.995288+08	1
19	0	\N	\N	https://bensbabycare.oss-cn-hangzhou.aliyuncs.com/babycare/image/babycare20170628145543_event.jpg	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2017-06-28 14:55:43.060458+08	2017-06-28 14:55:43.17013+08	1
20	0	\N	宝宝睡觉发出各种怪声，各种伸手和弹腿，原来是胀气。抚摸肚子，然后发了几个P就好多了	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2017-06-29 03:23:13.254431+08	2017-06-29 03:23:13.254843+08	1
21	0	\N	哭作宝满月啦！	https://bensbabycare.oss-cn-hangzhou.aliyuncs.com/babycare/image/babycare20170710092543_event.jpg	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2017-07-10 09:25:43.242209+08	2017-07-10 09:25:43.524517+08	1
29	0	\N	十一第一次翻身	https://bensbabycare.oss-cn-hangzhou.aliyuncs.com/babycare/image/babycare20171001150329_event.jpg	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2017-10-01 15:03:29.550188+08	2017-10-01 15:03:29.782307+08	1
\.


--
-- Name: babycare_event_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('babycare_event_id_seq', 29, true);


--
-- Data for Name: babycare_feedback; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY babycare_feedback (id, description, image1, image2, image3, image4, image5, image6, image7, image8, image9, created, modified, baby_id) FROM stdin;
\.


--
-- Name: babycare_feedback_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('babycare_feedback_id_seq', 3, true);


--
-- Data for Name: babycare_iaer; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY babycare_iaer (id, money, category, money_type, remark, created, type, chart_type, format, datetime, description, timing, user_id) FROM stdin;
\.


--
-- Name: babycare_iaer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('babycare_iaer_id_seq', 1, true);


--
-- Data for Name: babycare_like; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY babycare_like (id, datetime, baby_id, event_id) FROM stdin;
\.


--
-- Name: babycare_like_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('babycare_like_id_seq', 5, true);


--
-- Data for Name: babycare_loginlog; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY babycare_loginlog (id, system_type, system_version, phone_model, country, state, city, created, baby_id) FROM stdin;
\.


--
-- Name: babycare_loginlog_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('babycare_loginlog_id_seq', 1, false);


--
-- Data for Name: babycare_redenvelope; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY babycare_redenvelope (id, money, money_type, money_from, remark, created, baby_id) FROM stdin;
2	880	0	磊艳姐	出生红包	2017-06-18 20:23:48.382078+08	1
3	188	0	小强	出生红包	2017-06-18 20:24:04.21992+08	1
4	1000	0	宁波嬷嬷	出生红包	2017-06-18 20:24:32.295148+08	1
5	800	0	咸祥嬷嬷	出生红包	2017-06-18 20:27:53.33798+08	1
6	600	0	咸祥哥哥	出生红包	2017-06-18 20:28:40.339832+08	1
7	2000	0	大姐姐	出生红包	2017-06-18 20:29:12.58767+08	1
8	10000	0	丈母娘	出生红包	2017-06-18 20:29:42.928428+08	1
9	1500	0	丈母娘	催生红包	2017-06-18 20:30:00.026331+08	1
10	1200	0	江陆嬷嬷	出生红包	2017-06-18 20:30:46.200995+08	1
11	800	0	江陆大哥哥	出生红包	2017-06-18 20:31:04.54314+08	1
12	800	0	江陆小哥哥	出生红包	2017-06-18 20:31:22.459012+08	1
13	600	0	大伯伯	出生红包	2017-06-18 20:31:43.151531+08	1
14	1000	0	二伯伯	出生红包	2017-06-18 20:32:04.609331+08	1
15	1000	0	三姐姐	出生红包	2017-06-18 20:32:27.740332+08	1
16	600	0	舅舅	出生红包	2017-06-18 20:32:39.499006+08	1
17	1200	0	外婆	出生红包	2017-06-18 20:32:55.949876+08	1
18	1600	0	阿姨	出生红包	2017-06-18 20:33:08.480811+08	1
24	1600	0	管江阿姨	出生红包	2017-06-19 15:48:12.146769+08	1
25	1000	0	上海嬷嬷	出生红包	2017-06-19 15:56:08.231509+08	1
26	13000	0	妈妈	出生红包	2017-06-19 15:56:24.576739+08	1
27	1200	0	奶奶	出生红包	2017-06-20 09:23:52.182672+08	1
28	1200	0	沙村嬷嬷	出生红包	2017-06-21 08:08:29.253738+08	1
29	1200	0	沙村大阿姆	出生红包	2017-06-21 08:08:55.263453+08	1
30	1200	0	沙村小阿姆	出生红包	2017-06-21 08:09:14.296094+08	1
31	5600	0	红霞	出生红包	2017-06-22 13:57:16.857961+08	1
32	3600	0	五姐	出生红包	2017-06-22 13:58:18.451322+08	1
33	1200	0	六姐	出生红包	2017-06-22 13:58:51.728138+08	1
34	1600	0	二姐	出生红包	2017-06-22 13:59:31.027873+08	1
36	1000	0	建华哥哥	出生红包	2017-06-25 09:46:07.657558+08	1
38	600	0	小龙	出生红包	2017-07-01 19:09:59.361237+08	1
39	1200	0	塘溪舅舅	出生红包	2017-07-01 19:10:21.219857+08	1
40	1000	0	建辉哥	出生红包	2017-07-09 16:10:01.194495+08	1
42	300	0	沙村小阿姆	见面红包	2017-08-05 09:34:24.315802+08	1
43	2800	0	丈母娘	见面红包	2017-08-05 09:34:45.029717+08	1
44	1600	0	奶奶	见面红包	2017-08-05 09:35:04.622308+08	1
45	2000	0	老婆老板	出生红包	2017-08-05 09:38:17.605805+08	1
139	-66	0	餐饮	很久	2017-09-20 11:17:45.92158+08	1
140	-28	0	服饰	但	2017-09-20 13:39:26.054889+08	1
141	-66	0	收入	不会	2017-09-20 14:12:17.883001+08	1
142	-669	0	服饰	黄家驹	2017-09-20 14:12:29.268107+08	1
\.


--
-- Name: babycare_redenvelope_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('babycare_redenvelope_id_seq', 142, true);


--
-- Data for Name: babycare_verify; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY babycare_verify (id, email_verify_code, phone_verify_code, created, baby_id) FROM stdin;
2	289457	\N	2017-06-24 07:31:49.886294+08	1
\.


--
-- Name: babycare_verify_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('babycare_verify_id_seq', 2, true);


--
-- Data for Name: django_admin_log; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY django_admin_log (id, action_time, object_id, object_repr, action_flag, change_message, content_type_id, user_id) FROM stdin;
1	2017-05-26 16:29:35.88564+08	1	1.0(2)	1	[{"added": {}}]	8	1
2	2017-06-07 10:13:54.015758+08	1	1.0(2)	3		8	1
3	2017-06-07 13:19:16.277786+08	2	0.2(2)	1	[{"added": {}}]	8	1
4	2017-06-07 13:22:26.862825+08	3	0.3(3)	1	[{"added": {}}]	8	1
5	2017-06-07 13:50:16.218849+08	3	0.3(3)	3		8	1
6	2017-06-07 13:51:16.218915+08	2	0.2(2)	2	[{"changed": {"fields": ["app_file"]}}]	8	1
7	2017-06-16 16:46:45.700181+08	4	babycare	2	[{"changed": {"fields": ["image1"]}}]	10	1
8	2017-06-16 16:47:05.61688+08	3	babycare	2	[{"changed": {"fields": ["image1"]}}]	10	1
9	2017-06-16 16:47:23.998842+08	1	babycare	2	[{"changed": {"fields": ["content", "image1"]}}]	10	1
10	2017-06-18 20:21:29.81507+08	2	0.2(2)	3		8	1
11	2017-06-18 20:55:51.331649+08	20	RedEnvelope object	3		16	1
12	2017-06-18 20:55:51.33483+08	19	RedEnvelope object	3		16	1
13	2017-06-19 16:16:51.504696+08	4	0.2(2)	1	[{"added": {}}]	8	1
14	2017-06-19 16:28:21.909536+08	4	0.2(2)	3		8	1
15	2017-06-19 16:32:19.754351+08	5	0.2(1)	1	[{"added": {}}]	8	1
16	2017-06-19 16:36:16.77356+08	5	0.2(2)	2	[{"changed": {"fields": ["version_code"]}}]	8	1
17	2017-06-24 08:42:31.037762+08	2	test1	3		9	1
18	2017-06-24 08:42:31.040515+08	1	test1	3		9	1
19	2017-06-24 08:43:25.854956+08	5	test1	3		9	1
20	2017-06-24 08:43:25.857721+08	4	test1	3		9	1
21	2017-06-24 08:43:25.859454+08	3	test1	3		9	1
22	2017-06-24 08:52:02.217776+08	6	0.3(3)	1	[{"added": {}}]	8	1
23	2017-07-27 15:38:27.82901+08	41	RedEnvelope object	1	[{"added": {}}]	16	1
24	2017-08-21 13:31:59.485781+08	46	RedEnvelope object	1	[{"added": {}}]	16	1
25	2017-08-23 14:58:08.202641+08	97	RedEnvelope object	3		16	1
26	2017-08-23 14:58:08.206172+08	96	RedEnvelope object	3		16	1
27	2017-08-23 14:58:08.208256+08	95	RedEnvelope object	3		16	1
28	2017-08-23 14:58:08.209938+08	94	RedEnvelope object	3		16	1
29	2017-08-23 14:58:08.211589+08	93	RedEnvelope object	3		16	1
30	2017-08-23 14:58:08.213114+08	92	RedEnvelope object	3		16	1
31	2017-08-23 14:58:08.215016+08	91	RedEnvelope object	3		16	1
32	2017-08-23 14:58:08.216583+08	90	RedEnvelope object	3		16	1
33	2017-08-23 14:58:08.218283+08	89	RedEnvelope object	3		16	1
34	2017-08-23 14:58:08.219796+08	88	RedEnvelope object	3		16	1
35	2017-08-23 14:58:08.221245+08	87	RedEnvelope object	3		16	1
36	2017-08-23 14:58:08.222787+08	86	RedEnvelope object	3		16	1
37	2017-08-23 14:58:08.224362+08	85	RedEnvelope object	3		16	1
38	2017-08-23 14:58:08.226055+08	84	RedEnvelope object	3		16	1
39	2017-08-23 14:58:08.227646+08	83	RedEnvelope object	3		16	1
40	2017-08-23 14:58:08.229217+08	82	RedEnvelope object	3		16	1
41	2017-08-23 14:58:08.230787+08	81	RedEnvelope object	3		16	1
42	2017-08-23 14:58:08.232276+08	80	RedEnvelope object	3		16	1
43	2017-08-23 14:58:08.233765+08	79	RedEnvelope object	3		16	1
44	2017-08-23 14:58:08.235608+08	77	RedEnvelope object	3		16	1
45	2017-08-23 14:58:08.237262+08	76	RedEnvelope object	3		16	1
46	2017-08-31 15:30:10.217083+08	123	RedEnvelope object	3		16	1
47	2017-08-31 15:30:10.220766+08	122	RedEnvelope object	3		16	1
48	2017-08-31 15:30:10.22288+08	121	RedEnvelope object	3		16	1
49	2017-08-31 15:30:10.224696+08	120	RedEnvelope object	3		16	1
50	2017-08-31 15:30:10.226632+08	119	RedEnvelope object	3		16	1
51	2017-08-31 15:30:10.228458+08	118	RedEnvelope object	3		16	1
52	2017-08-31 15:30:10.230206+08	117	RedEnvelope object	3		16	1
53	2017-08-31 15:30:10.231798+08	116	RedEnvelope object	3		16	1
54	2017-08-31 15:30:10.233732+08	115	RedEnvelope object	3		16	1
55	2017-08-31 15:30:10.235395+08	114	RedEnvelope object	3		16	1
56	2017-08-31 15:30:10.237017+08	113	RedEnvelope object	3		16	1
57	2017-08-31 15:30:10.238969+08	112	RedEnvelope object	3		16	1
58	2017-08-31 15:30:10.240626+08	111	RedEnvelope object	3		16	1
59	2017-08-31 15:30:10.242688+08	110	RedEnvelope object	3		16	1
60	2017-08-31 15:30:10.244341+08	109	RedEnvelope object	3		16	1
61	2017-08-31 15:30:10.245932+08	108	RedEnvelope object	3		16	1
62	2017-08-31 15:30:10.247775+08	107	RedEnvelope object	3		16	1
63	2017-08-31 15:30:10.249324+08	106	RedEnvelope object	3		16	1
64	2017-08-31 15:30:10.250978+08	105	RedEnvelope object	3		16	1
65	2017-08-31 15:30:10.252487+08	104	RedEnvelope object	3		16	1
66	2017-08-31 15:30:10.254006+08	103	RedEnvelope object	3		16	1
67	2017-08-31 15:30:10.255783+08	102	RedEnvelope object	3		16	1
68	2017-08-31 15:30:10.258824+08	101	RedEnvelope object	3		16	1
69	2017-08-31 15:30:10.260561+08	100	RedEnvelope object	3		16	1
70	2017-08-31 15:30:10.262246+08	99	RedEnvelope object	3		16	1
71	2017-08-31 15:30:10.264499+08	98	RedEnvelope object	3		16	1
72	2017-08-31 15:30:10.266488+08	75	RedEnvelope object	3		16	1
73	2017-08-31 15:30:10.26807+08	74	RedEnvelope object	3		16	1
74	2017-08-31 15:30:10.26966+08	73	RedEnvelope object	3		16	1
75	2017-08-31 15:30:10.271408+08	72	RedEnvelope object	3		16	1
76	2017-08-31 15:30:10.272929+08	71	RedEnvelope object	3		16	1
77	2017-08-31 15:30:10.274624+08	70	RedEnvelope object	3		16	1
78	2017-08-31 15:30:10.276305+08	69	RedEnvelope object	3		16	1
79	2017-08-31 15:30:10.278726+08	68	RedEnvelope object	3		16	1
80	2017-08-31 15:30:10.280409+08	67	RedEnvelope object	3		16	1
81	2017-08-31 15:30:10.282064+08	66	RedEnvelope object	3		16	1
82	2017-08-31 15:30:10.283787+08	65	RedEnvelope object	3		16	1
83	2017-08-31 15:30:10.285628+08	64	RedEnvelope object	3		16	1
84	2017-08-31 15:30:10.28744+08	63	RedEnvelope object	3		16	1
85	2017-08-31 15:30:10.289201+08	62	RedEnvelope object	3		16	1
86	2017-08-31 15:30:10.290777+08	61	RedEnvelope object	3		16	1
87	2017-08-31 15:30:10.292448+08	60	RedEnvelope object	3		16	1
88	2017-08-31 15:30:10.293983+08	59	RedEnvelope object	3		16	1
89	2017-08-31 15:30:10.295651+08	58	RedEnvelope object	3		16	1
90	2017-08-31 15:30:10.297287+08	57	RedEnvelope object	3		16	1
91	2017-08-31 15:30:10.298978+08	56	RedEnvelope object	3		16	1
92	2017-08-31 15:30:10.30069+08	55	RedEnvelope object	3		16	1
93	2017-08-31 15:30:10.302363+08	54	RedEnvelope object	3		16	1
94	2017-08-31 15:30:10.304275+08	53	RedEnvelope object	3		16	1
95	2017-08-31 15:30:10.305972+08	52	RedEnvelope object	3		16	1
96	2017-08-31 15:30:10.307713+08	51	RedEnvelope object	3		16	1
97	2017-08-31 15:30:10.309381+08	50	RedEnvelope object	3		16	1
98	2017-08-31 15:30:10.311056+08	49	RedEnvelope object	3		16	1
99	2017-08-31 15:30:10.31288+08	48	RedEnvelope object	3		16	1
100	2017-08-31 15:30:10.314712+08	47	RedEnvelope object	3		16	1
101	2017-08-31 15:30:10.31638+08	46	RedEnvelope object	3		16	1
102	2017-08-31 16:57:54.717053+08	126	RedEnvelope object	3		16	1
103	2017-08-31 16:57:54.72014+08	125	RedEnvelope object	3		16	1
104	2017-08-31 16:57:54.721941+08	124	RedEnvelope object	3		16	1
105	2017-09-14 10:31:33.335609+08	132	RedEnvelope object	3		16	1
106	2017-09-14 10:44:56.096296+08	133	RedEnvelope object	3		16	1
107	2017-09-14 10:44:56.099591+08	131	RedEnvelope object	3		16	1
108	2017-09-14 10:44:56.101454+08	130	RedEnvelope object	3		16	1
109	2017-09-14 10:44:56.103229+08	129	RedEnvelope object	3		16	1
110	2017-09-14 10:44:56.104928+08	128	RedEnvelope object	3		16	1
111	2017-09-14 10:44:56.10734+08	127	RedEnvelope object	3		16	1
112	2017-09-20 11:15:51.478485+08	138	RedEnvelope object	3		16	1
113	2017-09-20 11:15:51.481903+08	137	RedEnvelope object	3		16	1
114	2017-09-20 11:15:51.483657+08	136	RedEnvelope object	3		16	1
115	2017-09-20 11:15:51.485515+08	135	RedEnvelope object	3		16	1
116	2017-09-20 11:15:51.487167+08	134	RedEnvelope object	3		16	1
117	2017-10-14 17:01:19.43668+08	3	test1@gmail.com(test1)	3		11	1
118	2017-10-14 17:01:19.439851+08	2	test@gmail.com(test)	3		11	1
119	2017-10-14 17:02:21.549428+08	3	test	3		3	1
120	2017-10-14 17:02:21.551878+08	4	test1	3		3	1
121	2017-10-14 17:05:02.738415+08	0a73b72c5a93a493a19fc37770c4fda4a48bc9c9	0a73b72c5a93a493a19fc37770c4fda4a48bc9c9	3		7	1
122	2017-10-14 17:08:38.879222+08	6	11	2	[{"changed": {"fields": ["username", "is_staff"]}}]	3	1
123	2017-10-14 17:08:50.216404+08	6	11	3		3	1
124	2017-10-14 20:35:23.488495+08	1	Iaer object	1	[{"added": {}}]	18	1
125	2017-10-14 20:35:44.123929+08	1	Iaer object	3		18	1
\.


--
-- Name: django_admin_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('django_admin_log_id_seq', 125, true);


--
-- Data for Name: django_content_type; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY django_content_type (id, app_label, model) FROM stdin;
1	admin	logentry
2	auth	permission
3	auth	user
4	auth	group
5	contenttypes	contenttype
6	sessions	session
7	authtoken	token
8	babycare	appinfo
9	babycare	like
10	babycare	event
11	babycare	babyuser
12	babycare	loginlog
13	babycare	verify
14	babycare	feedback
15	babycare	comment
16	babycare	redenvelope
18	babycare	iaer
\.


--
-- Name: django_content_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('django_content_type_id_seq', 18, true);


--
-- Data for Name: django_migrations; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY django_migrations (id, app, name, applied) FROM stdin;
1	contenttypes	0001_initial	2017-05-26 16:28:33.264639+08
2	auth	0001_initial	2017-05-26 16:28:33.372451+08
3	admin	0001_initial	2017-05-26 16:28:33.440142+08
4	admin	0002_logentry_remove_auto_add	2017-05-26 16:28:33.466668+08
5	contenttypes	0002_remove_content_type_name	2017-05-26 16:28:33.511678+08
6	auth	0002_alter_permission_name_max_length	2017-05-26 16:28:33.529278+08
7	auth	0003_alter_user_email_max_length	2017-05-26 16:28:33.547924+08
8	auth	0004_alter_user_username_opts	2017-05-26 16:28:33.561652+08
9	auth	0005_alter_user_last_login_null	2017-05-26 16:28:33.577062+08
10	auth	0006_require_contenttypes_0002	2017-05-26 16:28:33.579685+08
11	auth	0007_alter_validators_add_error_messages	2017-05-26 16:28:33.593712+08
12	auth	0008_alter_user_username_max_length	2017-05-26 16:28:33.624266+08
13	authtoken	0001_initial	2017-05-26 16:28:33.658024+08
14	authtoken	0002_auto_20160226_1747	2017-05-26 16:28:33.733906+08
15	sessions	0001_initial	2017-05-26 16:28:33.75598+08
16	babycare	0001_initial	2017-05-26 16:28:36.003975+08
17	babycare	0002_redenvelope	2017-06-18 19:53:55.098877+08
18	babycare	0002_iaer	2017-10-14 16:55:24.146335+08
\.


--
-- Name: django_migrations_id_seq; Type: SEQUENCE SET; Schema: public; Owner: ben
--

SELECT pg_catalog.setval('django_migrations_id_seq', 18, true);


--
-- Data for Name: django_session; Type: TABLE DATA; Schema: public; Owner: ben
--

COPY django_session (session_key, session_data, expire_date) FROM stdin;
8c4oc1fcqlbq2sr99gjcf2pd2a6r4h5x	Zjg4NTE3MzlhNmMzNTJhNzYwZDEzZjZiNmRjN2Q4NjI1ZWRjMTMwNjp7Il9hdXRoX3VzZXJfaGFzaCI6ImFjZTNjZmYwNzY0NDA2NzdjNDdkODhjMDBhNTU1MzJjYjZiN2IwYzkiLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJkamFuZ28uY29udHJpYi5hdXRoLmJhY2tlbmRzLk1vZGVsQmFja2VuZCIsIl9hdXRoX3VzZXJfaWQiOiIxIn0=	2017-06-09 16:29:06.48297+08
dnhen4gikyi9oe9sb6lp07du1pnm0n32	Zjg4NTE3MzlhNmMzNTJhNzYwZDEzZjZiNmRjN2Q4NjI1ZWRjMTMwNjp7Il9hdXRoX3VzZXJfaGFzaCI6ImFjZTNjZmYwNzY0NDA2NzdjNDdkODhjMDBhNTU1MzJjYjZiN2IwYzkiLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJkamFuZ28uY29udHJpYi5hdXRoLmJhY2tlbmRzLk1vZGVsQmFja2VuZCIsIl9hdXRoX3VzZXJfaWQiOiIxIn0=	2017-06-21 10:13:41.79168+08
ulz6iawp9xn3tni60k5vys1odwospek4	Zjg4NTE3MzlhNmMzNTJhNzYwZDEzZjZiNmRjN2Q4NjI1ZWRjMTMwNjp7Il9hdXRoX3VzZXJfaGFzaCI6ImFjZTNjZmYwNzY0NDA2NzdjNDdkODhjMDBhNTU1MzJjYjZiN2IwYzkiLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJkamFuZ28uY29udHJpYi5hdXRoLmJhY2tlbmRzLk1vZGVsQmFja2VuZCIsIl9hdXRoX3VzZXJfaWQiOiIxIn0=	2017-07-08 08:42:21.434107+08
j14n98mmry821nf03eppdri3a6ks7iyr	Zjg4NTE3MzlhNmMzNTJhNzYwZDEzZjZiNmRjN2Q4NjI1ZWRjMTMwNjp7Il9hdXRoX3VzZXJfaGFzaCI6ImFjZTNjZmYwNzY0NDA2NzdjNDdkODhjMDBhNTU1MzJjYjZiN2IwYzkiLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJkamFuZ28uY29udHJpYi5hdXRoLmJhY2tlbmRzLk1vZGVsQmFja2VuZCIsIl9hdXRoX3VzZXJfaWQiOiIxIn0=	2017-08-10 15:37:16.033751+08
x4b63ba5bp2bziolw2nuy6nsspqs5u7n	Zjg4NTE3MzlhNmMzNTJhNzYwZDEzZjZiNmRjN2Q4NjI1ZWRjMTMwNjp7Il9hdXRoX3VzZXJfaGFzaCI6ImFjZTNjZmYwNzY0NDA2NzdjNDdkODhjMDBhNTU1MzJjYjZiN2IwYzkiLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJkamFuZ28uY29udHJpYi5hdXRoLmJhY2tlbmRzLk1vZGVsQmFja2VuZCIsIl9hdXRoX3VzZXJfaWQiOiIxIn0=	2017-08-19 09:28:49.979641+08
cjlt2bqnxa5xr2xv4rbygafam9u0esv3	Zjg4NTE3MzlhNmMzNTJhNzYwZDEzZjZiNmRjN2Q4NjI1ZWRjMTMwNjp7Il9hdXRoX3VzZXJfaGFzaCI6ImFjZTNjZmYwNzY0NDA2NzdjNDdkODhjMDBhNTU1MzJjYjZiN2IwYzkiLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJkamFuZ28uY29udHJpYi5hdXRoLmJhY2tlbmRzLk1vZGVsQmFja2VuZCIsIl9hdXRoX3VzZXJfaWQiOiIxIn0=	2017-08-30 13:09:10.742477+08
28edmtoyi6m8p1mwju1lxkl18bb3r3cy	Zjg4NTE3MzlhNmMzNTJhNzYwZDEzZjZiNmRjN2Q4NjI1ZWRjMTMwNjp7Il9hdXRoX3VzZXJfaGFzaCI6ImFjZTNjZmYwNzY0NDA2NzdjNDdkODhjMDBhNTU1MzJjYjZiN2IwYzkiLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJkamFuZ28uY29udHJpYi5hdXRoLmJhY2tlbmRzLk1vZGVsQmFja2VuZCIsIl9hdXRoX3VzZXJfaWQiOiIxIn0=	2017-09-05 14:14:17.821626+08
v1t6buldu4a1ef0nopp5fxn4bbl3aiho	Zjg4NTE3MzlhNmMzNTJhNzYwZDEzZjZiNmRjN2Q4NjI1ZWRjMTMwNjp7Il9hdXRoX3VzZXJfaGFzaCI6ImFjZTNjZmYwNzY0NDA2NzdjNDdkODhjMDBhNTU1MzJjYjZiN2IwYzkiLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJkamFuZ28uY29udHJpYi5hdXRoLmJhY2tlbmRzLk1vZGVsQmFja2VuZCIsIl9hdXRoX3VzZXJfaWQiOiIxIn0=	2017-09-14 15:28:37.988596+08
z8qc1p2dj5pdsy7e9hx8xpebesoi20lk	Zjg4NTE3MzlhNmMzNTJhNzYwZDEzZjZiNmRjN2Q4NjI1ZWRjMTMwNjp7Il9hdXRoX3VzZXJfaGFzaCI6ImFjZTNjZmYwNzY0NDA2NzdjNDdkODhjMDBhNTU1MzJjYjZiN2IwYzkiLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJkamFuZ28uY29udHJpYi5hdXRoLmJhY2tlbmRzLk1vZGVsQmFja2VuZCIsIl9hdXRoX3VzZXJfaWQiOiIxIn0=	2017-10-02 09:57:10.098971+08
uaeafsod2n7upli016z6vcwbxhxemzq2	Zjg4NTE3MzlhNmMzNTJhNzYwZDEzZjZiNmRjN2Q4NjI1ZWRjMTMwNjp7Il9hdXRoX3VzZXJfaGFzaCI6ImFjZTNjZmYwNzY0NDA2NzdjNDdkODhjMDBhNTU1MzJjYjZiN2IwYzkiLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJkamFuZ28uY29udHJpYi5hdXRoLmJhY2tlbmRzLk1vZGVsQmFja2VuZCIsIl9hdXRoX3VzZXJfaWQiOiIxIn0=	2017-10-09 11:41:17.277341+08
cogq40tq5k45mtzsrmm84qruab6li2jh	Zjg4NTE3MzlhNmMzNTJhNzYwZDEzZjZiNmRjN2Q4NjI1ZWRjMTMwNjp7Il9hdXRoX3VzZXJfaGFzaCI6ImFjZTNjZmYwNzY0NDA2NzdjNDdkODhjMDBhNTU1MzJjYjZiN2IwYzkiLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJkamFuZ28uY29udHJpYi5hdXRoLmJhY2tlbmRzLk1vZGVsQmFja2VuZCIsIl9hdXRoX3VzZXJfaWQiOiIxIn0=	2017-10-09 13:32:26.982427+08
96fakcfwmcuukg0otp9wk3g3at99oepg	Zjg4NTE3MzlhNmMzNTJhNzYwZDEzZjZiNmRjN2Q4NjI1ZWRjMTMwNjp7Il9hdXRoX3VzZXJfaGFzaCI6ImFjZTNjZmYwNzY0NDA2NzdjNDdkODhjMDBhNTU1MzJjYjZiN2IwYzkiLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJkamFuZ28uY29udHJpYi5hdXRoLmJhY2tlbmRzLk1vZGVsQmFja2VuZCIsIl9hdXRoX3VzZXJfaWQiOiIxIn0=	2017-10-28 16:54:37.192216+08
emprgjr3tqki5jaouad863bi4ob7yyh4	Zjg4NTE3MzlhNmMzNTJhNzYwZDEzZjZiNmRjN2Q4NjI1ZWRjMTMwNjp7Il9hdXRoX3VzZXJfaGFzaCI6ImFjZTNjZmYwNzY0NDA2NzdjNDdkODhjMDBhNTU1MzJjYjZiN2IwYzkiLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJkamFuZ28uY29udHJpYi5hdXRoLmJhY2tlbmRzLk1vZGVsQmFja2VuZCIsIl9hdXRoX3VzZXJfaWQiOiIxIn0=	2017-10-28 17:12:14.816145+08
e9h244eb638frg11iqgauplpvo9qt3f9	Zjg4NTE3MzlhNmMzNTJhNzYwZDEzZjZiNmRjN2Q4NjI1ZWRjMTMwNjp7Il9hdXRoX3VzZXJfaGFzaCI6ImFjZTNjZmYwNzY0NDA2NzdjNDdkODhjMDBhNTU1MzJjYjZiN2IwYzkiLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJkamFuZ28uY29udHJpYi5hdXRoLmJhY2tlbmRzLk1vZGVsQmFja2VuZCIsIl9hdXRoX3VzZXJfaWQiOiIxIn0=	2017-10-28 20:33:54.20908+08
\.


--
-- Name: auth_group_name_key; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_group
    ADD CONSTRAINT auth_group_name_key UNIQUE (name);


--
-- Name: auth_group_permissions_group_id_0cd325b0_uniq; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_group_permissions
    ADD CONSTRAINT auth_group_permissions_group_id_0cd325b0_uniq UNIQUE (group_id, permission_id);


--
-- Name: auth_group_permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_group_permissions
    ADD CONSTRAINT auth_group_permissions_pkey PRIMARY KEY (id);


--
-- Name: auth_group_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_group
    ADD CONSTRAINT auth_group_pkey PRIMARY KEY (id);


--
-- Name: auth_permission_content_type_id_01ab375a_uniq; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_permission
    ADD CONSTRAINT auth_permission_content_type_id_01ab375a_uniq UNIQUE (content_type_id, codename);


--
-- Name: auth_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_permission
    ADD CONSTRAINT auth_permission_pkey PRIMARY KEY (id);


--
-- Name: auth_user_groups_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_user_groups
    ADD CONSTRAINT auth_user_groups_pkey PRIMARY KEY (id);


--
-- Name: auth_user_groups_user_id_94350c0c_uniq; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_user_groups
    ADD CONSTRAINT auth_user_groups_user_id_94350c0c_uniq UNIQUE (user_id, group_id);


--
-- Name: auth_user_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_user
    ADD CONSTRAINT auth_user_pkey PRIMARY KEY (id);


--
-- Name: auth_user_user_permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_user_user_permissions
    ADD CONSTRAINT auth_user_user_permissions_pkey PRIMARY KEY (id);


--
-- Name: auth_user_user_permissions_user_id_14a6b632_uniq; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_user_user_permissions
    ADD CONSTRAINT auth_user_user_permissions_user_id_14a6b632_uniq UNIQUE (user_id, permission_id);


--
-- Name: auth_user_username_key; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_user
    ADD CONSTRAINT auth_user_username_key UNIQUE (username);


--
-- Name: authtoken_token_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY authtoken_token
    ADD CONSTRAINT authtoken_token_pkey PRIMARY KEY (key);


--
-- Name: authtoken_token_user_id_key; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY authtoken_token
    ADD CONSTRAINT authtoken_token_user_id_key UNIQUE (user_id);


--
-- Name: babycare_appinfo_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_appinfo
    ADD CONSTRAINT babycare_appinfo_pkey PRIMARY KEY (id);


--
-- Name: babycare_babyuser_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_babyuser
    ADD CONSTRAINT babycare_babyuser_pkey PRIMARY KEY (id);


--
-- Name: babycare_comment_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_comment
    ADD CONSTRAINT babycare_comment_pkey PRIMARY KEY (id);


--
-- Name: babycare_event_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_event
    ADD CONSTRAINT babycare_event_pkey PRIMARY KEY (id);


--
-- Name: babycare_feedback_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_feedback
    ADD CONSTRAINT babycare_feedback_pkey PRIMARY KEY (id);


--
-- Name: babycare_iaer_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_iaer
    ADD CONSTRAINT babycare_iaer_pkey PRIMARY KEY (id);


--
-- Name: babycare_like_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_like
    ADD CONSTRAINT babycare_like_pkey PRIMARY KEY (id);


--
-- Name: babycare_loginlog_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_loginlog
    ADD CONSTRAINT babycare_loginlog_pkey PRIMARY KEY (id);


--
-- Name: babycare_redenvelope_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_redenvelope
    ADD CONSTRAINT babycare_redenvelope_pkey PRIMARY KEY (id);


--
-- Name: babycare_verify_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_verify
    ADD CONSTRAINT babycare_verify_pkey PRIMARY KEY (id);


--
-- Name: django_admin_log_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY django_admin_log
    ADD CONSTRAINT django_admin_log_pkey PRIMARY KEY (id);


--
-- Name: django_content_type_app_label_76bd3d3b_uniq; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY django_content_type
    ADD CONSTRAINT django_content_type_app_label_76bd3d3b_uniq UNIQUE (app_label, model);


--
-- Name: django_content_type_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY django_content_type
    ADD CONSTRAINT django_content_type_pkey PRIMARY KEY (id);


--
-- Name: django_migrations_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY django_migrations
    ADD CONSTRAINT django_migrations_pkey PRIMARY KEY (id);


--
-- Name: django_session_pkey; Type: CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY django_session
    ADD CONSTRAINT django_session_pkey PRIMARY KEY (session_key);


--
-- Name: auth_group_name_a6ea08ec_like; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX auth_group_name_a6ea08ec_like ON auth_group USING btree (name varchar_pattern_ops);


--
-- Name: auth_group_permissions_0e939a4f; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX auth_group_permissions_0e939a4f ON auth_group_permissions USING btree (group_id);


--
-- Name: auth_group_permissions_8373b171; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX auth_group_permissions_8373b171 ON auth_group_permissions USING btree (permission_id);


--
-- Name: auth_permission_417f1b1c; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX auth_permission_417f1b1c ON auth_permission USING btree (content_type_id);


--
-- Name: auth_user_groups_0e939a4f; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX auth_user_groups_0e939a4f ON auth_user_groups USING btree (group_id);


--
-- Name: auth_user_groups_e8701ad4; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX auth_user_groups_e8701ad4 ON auth_user_groups USING btree (user_id);


--
-- Name: auth_user_user_permissions_8373b171; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX auth_user_user_permissions_8373b171 ON auth_user_user_permissions USING btree (permission_id);


--
-- Name: auth_user_user_permissions_e8701ad4; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX auth_user_user_permissions_e8701ad4 ON auth_user_user_permissions USING btree (user_id);


--
-- Name: auth_user_username_6821ab7c_like; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX auth_user_username_6821ab7c_like ON auth_user USING btree (username varchar_pattern_ops);


--
-- Name: authtoken_token_key_10f0b77e_like; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX authtoken_token_key_10f0b77e_like ON authtoken_token USING btree (key varchar_pattern_ops);


--
-- Name: babycare_babyuser_e8701ad4; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX babycare_babyuser_e8701ad4 ON babycare_babyuser USING btree (user_id);


--
-- Name: babycare_comment_4437cfac; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX babycare_comment_4437cfac ON babycare_comment USING btree (event_id);


--
-- Name: babycare_comment_d12af49a; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX babycare_comment_d12af49a ON babycare_comment USING btree (baby_id);


--
-- Name: babycare_comment_f2aa0a2c; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX babycare_comment_f2aa0a2c ON babycare_comment USING btree (source_comment_id);


--
-- Name: babycare_event_d12af49a; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX babycare_event_d12af49a ON babycare_event USING btree (baby_id);


--
-- Name: babycare_feedback_d12af49a; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX babycare_feedback_d12af49a ON babycare_feedback USING btree (baby_id);


--
-- Name: babycare_iaer_e8701ad4; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX babycare_iaer_e8701ad4 ON babycare_iaer USING btree (user_id);


--
-- Name: babycare_like_4437cfac; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX babycare_like_4437cfac ON babycare_like USING btree (event_id);


--
-- Name: babycare_like_d12af49a; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX babycare_like_d12af49a ON babycare_like USING btree (baby_id);


--
-- Name: babycare_loginlog_d12af49a; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX babycare_loginlog_d12af49a ON babycare_loginlog USING btree (baby_id);


--
-- Name: babycare_redenvelope_d12af49a; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX babycare_redenvelope_d12af49a ON babycare_redenvelope USING btree (baby_id);


--
-- Name: babycare_verify_d12af49a; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX babycare_verify_d12af49a ON babycare_verify USING btree (baby_id);


--
-- Name: django_admin_log_417f1b1c; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX django_admin_log_417f1b1c ON django_admin_log USING btree (content_type_id);


--
-- Name: django_admin_log_e8701ad4; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX django_admin_log_e8701ad4 ON django_admin_log USING btree (user_id);


--
-- Name: django_session_de54fa62; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX django_session_de54fa62 ON django_session USING btree (expire_date);


--
-- Name: django_session_session_key_c0390e0f_like; Type: INDEX; Schema: public; Owner: ben
--

CREATE INDEX django_session_session_key_c0390e0f_like ON django_session USING btree (session_key varchar_pattern_ops);


--
-- Name: auth_group_permiss_permission_id_84c5c92e_fk_auth_permission_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_group_permissions
    ADD CONSTRAINT auth_group_permiss_permission_id_84c5c92e_fk_auth_permission_id FOREIGN KEY (permission_id) REFERENCES auth_permission(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: auth_group_permissions_group_id_b120cbf9_fk_auth_group_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_group_permissions
    ADD CONSTRAINT auth_group_permissions_group_id_b120cbf9_fk_auth_group_id FOREIGN KEY (group_id) REFERENCES auth_group(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: auth_permiss_content_type_id_2f476e4b_fk_django_content_type_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_permission
    ADD CONSTRAINT auth_permiss_content_type_id_2f476e4b_fk_django_content_type_id FOREIGN KEY (content_type_id) REFERENCES django_content_type(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: auth_user_groups_group_id_97559544_fk_auth_group_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_user_groups
    ADD CONSTRAINT auth_user_groups_group_id_97559544_fk_auth_group_id FOREIGN KEY (group_id) REFERENCES auth_group(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: auth_user_groups_user_id_6a12ed8b_fk_auth_user_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_user_groups
    ADD CONSTRAINT auth_user_groups_user_id_6a12ed8b_fk_auth_user_id FOREIGN KEY (user_id) REFERENCES auth_user(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: auth_user_user_per_permission_id_1fbb5f2c_fk_auth_permission_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_user_user_permissions
    ADD CONSTRAINT auth_user_user_per_permission_id_1fbb5f2c_fk_auth_permission_id FOREIGN KEY (permission_id) REFERENCES auth_permission(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: auth_user_user_permissions_user_id_a95ead1b_fk_auth_user_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY auth_user_user_permissions
    ADD CONSTRAINT auth_user_user_permissions_user_id_a95ead1b_fk_auth_user_id FOREIGN KEY (user_id) REFERENCES auth_user(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: authtoken_token_user_id_35299eff_fk_auth_user_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY authtoken_token
    ADD CONSTRAINT authtoken_token_user_id_35299eff_fk_auth_user_id FOREIGN KEY (user_id) REFERENCES auth_user(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: babycare_babyuser_user_id_a3aed8d8_fk_auth_user_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_babyuser
    ADD CONSTRAINT babycare_babyuser_user_id_a3aed8d8_fk_auth_user_id FOREIGN KEY (user_id) REFERENCES auth_user(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: babycare_comm_source_comment_id_7e9e0d8b_fk_babycare_comment_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_comment
    ADD CONSTRAINT babycare_comm_source_comment_id_7e9e0d8b_fk_babycare_comment_id FOREIGN KEY (source_comment_id) REFERENCES babycare_comment(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: babycare_comment_baby_id_5692e7f8_fk_babycare_babyuser_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_comment
    ADD CONSTRAINT babycare_comment_baby_id_5692e7f8_fk_babycare_babyuser_id FOREIGN KEY (baby_id) REFERENCES babycare_babyuser(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: babycare_comment_event_id_dd13a261_fk_babycare_event_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_comment
    ADD CONSTRAINT babycare_comment_event_id_dd13a261_fk_babycare_event_id FOREIGN KEY (event_id) REFERENCES babycare_event(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: babycare_event_baby_id_cbfba2fd_fk_babycare_babyuser_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_event
    ADD CONSTRAINT babycare_event_baby_id_cbfba2fd_fk_babycare_babyuser_id FOREIGN KEY (baby_id) REFERENCES babycare_babyuser(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: babycare_feedback_baby_id_fbbc63b9_fk_babycare_babyuser_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_feedback
    ADD CONSTRAINT babycare_feedback_baby_id_fbbc63b9_fk_babycare_babyuser_id FOREIGN KEY (baby_id) REFERENCES babycare_babyuser(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: babycare_iaer_user_id_4bf3a62d_fk_babycare_babyuser_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_iaer
    ADD CONSTRAINT babycare_iaer_user_id_4bf3a62d_fk_babycare_babyuser_id FOREIGN KEY (user_id) REFERENCES babycare_babyuser(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: babycare_like_baby_id_cacfa94b_fk_babycare_babyuser_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_like
    ADD CONSTRAINT babycare_like_baby_id_cacfa94b_fk_babycare_babyuser_id FOREIGN KEY (baby_id) REFERENCES babycare_babyuser(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: babycare_like_event_id_e4584794_fk_babycare_event_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_like
    ADD CONSTRAINT babycare_like_event_id_e4584794_fk_babycare_event_id FOREIGN KEY (event_id) REFERENCES babycare_event(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: babycare_loginlog_baby_id_b7de5fa8_fk_babycare_babyuser_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_loginlog
    ADD CONSTRAINT babycare_loginlog_baby_id_b7de5fa8_fk_babycare_babyuser_id FOREIGN KEY (baby_id) REFERENCES babycare_babyuser(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: babycare_redenvelope_baby_id_6fb45d26_fk_babycare_babyuser_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_redenvelope
    ADD CONSTRAINT babycare_redenvelope_baby_id_6fb45d26_fk_babycare_babyuser_id FOREIGN KEY (baby_id) REFERENCES babycare_babyuser(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: babycare_verify_baby_id_426c8def_fk_babycare_babyuser_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY babycare_verify
    ADD CONSTRAINT babycare_verify_baby_id_426c8def_fk_babycare_babyuser_id FOREIGN KEY (baby_id) REFERENCES babycare_babyuser(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: django_admin_content_type_id_c4bce8eb_fk_django_content_type_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY django_admin_log
    ADD CONSTRAINT django_admin_content_type_id_c4bce8eb_fk_django_content_type_id FOREIGN KEY (content_type_id) REFERENCES django_content_type(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: django_admin_log_user_id_c564eba6_fk_auth_user_id; Type: FK CONSTRAINT; Schema: public; Owner: ben
--

ALTER TABLE ONLY django_admin_log
    ADD CONSTRAINT django_admin_log_user_id_c564eba6_fk_auth_user_id FOREIGN KEY (user_id) REFERENCES auth_user(id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

