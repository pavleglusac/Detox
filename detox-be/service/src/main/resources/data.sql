INSERT INTO USERS(ID, EMAIL, IMAGE_URL, NAME, PASSWORD, EMAIL_VERIFIED, LOGIN_ATTEMPTS, DELETED, DTYPE) VALUES
    ('e3661c31-d1a4-47ab-94b6-1c6500dccf25', 'pera@gmail.com', NULL, 'Pera', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', true, 0, false, 'Patient');

INSERT INTO USERS(ID, EMAIL, IMAGE_URL, NAME, PASSWORD, EMAIL_VERIFIED, LOGIN_ATTEMPTS, DELETED, DTYPE) VALUES
    ('e3661c31-d1a4-47ab-94b6-1c6500dccf24', 'mika@gmail.com', NULL, 'Mika', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', true, 0, false, 'Doctor');


INSERT INTO ROLE (name) VALUES ('ROLE_DOCTOR');
INSERT INTO ROLE (name) VALUES ('ROLE_PATIENT');

INSERT INTO USER_ROLE (user_id, role_id) VALUES ('e3661c31-d1a4-47ab-94b6-1c6500dccf25', 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES ('e3661c31-d1a4-47ab-94b6-1c6500dccf24', 1);

