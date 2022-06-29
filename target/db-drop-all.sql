alter table comment drop foreign key fk_comment_user_id;
drop index ix_comment_user_id on comment;

alter table comment drop foreign key fk_comment_parent_id;
drop index ix_comment_parent_id on comment;

alter table comment drop foreign key fk_comment_post_id;
drop index ix_comment_post_id on comment;

alter table login drop foreign key fk_login_user_id;

alter table post drop foreign key fk_post_language_id;

alter table post drop foreign key fk_post_user_id;
drop index ix_post_user_id on post;

alter table post drop foreign key fk_post_topic_id;
drop index ix_post_topic_id on post;

alter table role_user drop foreign key fk_role_user_role;
drop index ix_role_user_role on role_user;

alter table role_user drop foreign key fk_role_user_user;
drop index ix_role_user_user on role_user;

alter table topic drop foreign key fk_topic_subject_id;
drop index ix_topic_subject_id on topic;

alter table user drop foreign key fk_user_login_user_id;

alter table user_role drop foreign key fk_user_role_user;
drop index ix_user_role_user on user_role;

alter table user_role drop foreign key fk_user_role_role;
drop index ix_user_role_role on user_role;

alter table vote drop foreign key fk_vote_user_id;
drop index ix_vote_user_id on vote;

alter table vote drop foreign key fk_vote_comment_id;
drop index ix_vote_comment_id on vote;

alter table vote drop foreign key fk_vote_post_id;
drop index ix_vote_post_id on vote;

drop table if exists comment;

drop table if exists language;

drop table if exists login;

drop table if exists post;

drop table if exists role;

drop table if exists role_user;

drop table if exists subject;

drop table if exists topic;

drop table if exists user;

drop table if exists user_role;

drop table if exists vote;

