
-- initial admin with password admin123
-- $2a$10$oQ7uwLfxoiOSPXduWlvyXu/d56TRMMdJTvNxS2uLyWiJvv/3Ulanm
-- $2a$10$ynocuKoBe3Iqgzhj.YLCZuo1yuDTf7iXBJtJeMIlguSckMgJueWy.
insert into users values('admin','{bcrypt}$2a$10$mT5cNJhoCFZkbYZAjA7NLOAb/vY2kx0wU.8pqSmXVk6EcKECpSydi', 'admin@wgzhao.com', 'ADMIN', true);
insert into authorities values('admin', 'ADMIN');