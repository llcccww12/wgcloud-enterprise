-- MailSet: 扩 FROM_PWD 列长度（适配企业用户授权码/客户端密码超过原 30 字符的情况）。
-- 报错样例:
--   org.springframework.dao.DataIntegrityViolationException: Data truncation:
--   Data too long for column 'FROM_PWD' at row 1
-- 二开后 server 端的 MailSetService / MailSetController 不再限制, 但数据库列需要先扩长,
-- 否则保存邮件设置时 MySQL 会截断/报错。

USE wgcloud;

ALTER TABLE MAIL_SET
  MODIFY COLUMN FROM_PWD VARCHAR(100) COLLATE utf8_unicode_ci DEFAULT NULL;
