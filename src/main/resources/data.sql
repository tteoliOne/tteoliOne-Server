INSERT INTO authority (AUTHORITY_NAME, create_at, update_at,status) values ('ROLE_DISABLED_USER', now(), now(),'A');
INSERT INTO authority (AUTHORITY_NAME, create_at, update_at,status) values ('ROLE_USER', now(), now(),'A');
INSERT INTO authority (AUTHORITY_NAME, create_at, update_at,status) values ('ROLE_WITHDRAW_USER', now(), now(),'A');
INSERT INTO authority (AUTHORITY_NAME, create_at, update_at,status) values ('', now(), now(),'A');

INSERT INTO category (CATEGORY_ID, CATEGORY_NAME, create_at, update_at,status) values (1, '채소', now(), now(),'A');
INSERT INTO category (CATEGORY_ID, CATEGORY_NAME, create_at, update_at,status) values (2, '과일', now(), now(),'A');
INSERT INTO category (CATEGORY_ID, CATEGORY_NAME, create_at, update_at,status) values (3, '간편식', now(), now(),'A');
INSERT INTO category (CATEGORY_ID, CATEGORY_NAME, create_at, update_at,status) values (4, '정육', now(), now(),'A');
INSERT INTO category (CATEGORY_ID, CATEGORY_NAME, create_at, update_at,status) values (5, '수산물', now(), now(),'A');
INSERT INTO category (CATEGORY_ID, CATEGORY_NAME, create_at, update_at,status) values (6, '기타', now(), now(),'A');
