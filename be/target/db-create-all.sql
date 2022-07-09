create table comment (
  id                            bigint auto_increment not null,
  time                          datetime(6) not null,
  content                       varchar(255) not null,
  user_id                       bigint,
  active                        tinyint(1) not null,
  parent_id                     bigint,
  post_id                       bigint,
  constraint pk_comment primary key (id)
);

create table language (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  code                          varchar(255),
  constraint pk_language primary key (id)
);

create table login (
  user_id                       bigint auto_increment not null,
  username                      varchar(255) not null,
  password                      varchar(255) not null,
  token                         varchar(255),
  constraint uq_login_username unique (username),
  constraint pk_login primary key (user_id)
);

create table post (
  id                            bigint auto_increment not null,
  content                       varchar(255) not null,
  active                        tinyint(1) not null,
  create_date                   datetime(6) not null,
  language_id                   bigint,
  user_id                       bigint,
  topic_id                      bigint,
  constraint uq_post_language_id unique (language_id),
  constraint pk_post primary key (id)
);

create table role (
  id                            integer auto_increment not null,
  type                          varchar(8) not null,
  active                        tinyint(1) not null,
  constraint pk_role primary key (id)
);

create table role_user (
  role_id                       integer not null,
  user_id                       bigint not null,
  constraint pk_role_user primary key (role_id,user_id)
);

create table subject (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  active                        tinyint(1) not null,
  constraint pk_subject primary key (id)
);

create table topic (
  id                            bigint auto_increment not null,
  active                        tinyint(1) not null,
  name                          varchar(255) not null,
  subject_id                    bigint,
  constraint pk_topic primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  first_name                    varchar(255) not null,
  last_name                     varchar(255) not null,
  dob                           datetime(6) not null,
  email                         varchar(255) not null,
  biography                     varchar(255),
  status                        varchar(11),
  active                        tinyint(1) not null,
  last_change_password          bigint,
  login_user_id                 bigint,
  constraint uq_user_login_user_id unique (login_user_id),
  constraint pk_user primary key (id)
);

create table user_role (
  user_id                       bigint not null,
  role_id                       integer not null,
  constraint pk_user_role primary key (user_id,role_id)
);

create table vote (
  id                            bigint auto_increment not null,
  type                          varchar(8),
  time                          datetime(6) not null,
  user_id                       bigint,
  comment_id                    bigint,
  post_id                       bigint,
  constraint pk_vote primary key (id)
);

create index ix_comment_user_id on comment (user_id);
alter table comment add constraint fk_comment_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_comment_parent_id on comment (parent_id);
alter table comment add constraint fk_comment_parent_id foreign key (parent_id) references comment (id) on delete restrict on update restrict;

create index ix_comment_post_id on comment (post_id);
alter table comment add constraint fk_comment_post_id foreign key (post_id) references post (id) on delete restrict on update restrict;

alter table login add constraint fk_login_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table post add constraint fk_post_language_id foreign key (language_id) references language (id) on delete restrict on update restrict;

create index ix_post_user_id on post (user_id);
alter table post add constraint fk_post_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_post_topic_id on post (topic_id);
alter table post add constraint fk_post_topic_id foreign key (topic_id) references topic (id) on delete restrict on update restrict;

create index ix_role_user_role on role_user (role_id);
alter table role_user add constraint fk_role_user_role foreign key (role_id) references role (id) on delete restrict on update restrict;

create index ix_role_user_user on role_user (user_id);
alter table role_user add constraint fk_role_user_user foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_topic_subject_id on topic (subject_id);
alter table topic add constraint fk_topic_subject_id foreign key (subject_id) references subject (id) on delete restrict on update restrict;

alter table user add constraint fk_user_login_user_id foreign key (login_user_id) references login (user_id) on delete restrict on update restrict;

create index ix_user_role_user on user_role (user_id);
alter table user_role add constraint fk_user_role_user foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_role_role on user_role (role_id);
alter table user_role add constraint fk_user_role_role foreign key (role_id) references role (id) on delete restrict on update restrict;

create index ix_vote_user_id on vote (user_id);
alter table vote add constraint fk_vote_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_vote_comment_id on vote (comment_id);
alter table vote add constraint fk_vote_comment_id foreign key (comment_id) references comment (id) on delete restrict on update restrict;

create index ix_vote_post_id on vote (post_id);
alter table vote add constraint fk_vote_post_id foreign key (post_id) references post (id) on delete restrict on update restrict;

