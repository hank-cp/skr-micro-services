INSERT INTO "TENENT" ("CODE", "NAME", "VIP_LEVEL") VALUES ('org', 'org', 1);


INSERT INTO "ACCOUNT" ("ID", "uid", "STATUS") VALUES (1,UUID(),0);
INSERT INTO "ACCOUNT" ("ID", "uid", "STATUS") VALUES (2,UUID(),0);
INSERT INTO "ACCOUNT" ("ID", "uid", "STATUS") VALUES (3,UUID(),0);


INSERT INTO "USERNAME_PASSWORD_CERTIFICATION" ("ACCOUNT_ID", "USERNAME", "PASSWORD") VALUES (1, 'dev', 'dev');
INSERT INTO "USERNAME_PASSWORD_CERTIFICATION" ("ACCOUNT_ID", "USERNAME", "PASSWORD") VALUES (2, 'test', 'test');
INSERT INTO "USERNAME_PASSWORD_CERTIFICATION" ("ACCOUNT_ID", "USERNAME", "PASSWORD") VALUES (3, 'guest', 'guest');


INSERT INTO "T_USER" ("TENENT_CODE", "ACCOUNT_ID", "USERNAME", "PERMISSION_BIT", "STATUS") VALUES ('org', 1, 'dev', 9223372036854775807, 0);
INSERT INTO "T_USER" ("TENENT_CODE", "ACCOUNT_ID", "USERNAME", "PERMISSION_BIT", "STATUS") VALUES ('org', 2, 'test', 0, 0);