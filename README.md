# 도커 안내사항

## MySQL 도커 생성

```dockerfile
docker run --name kuke-board-mysql -e MYSQL_ROOT_PASSWORD=root -d -p 3306:3306 mysql:8.0.38
```

## redis 도커 생성
```dockerfile
docker run --name kuke-board-redis -d -p 6379:6379 redis:7.4
```

## Kafka 도커 생성
```dockerfile
docker run -d --name kuke-board-kafka -p 9092:9092 apache/kafka:3.8.0
```

### Kafka 토픽 생성
```kafaka
./kafka-topics.sh --bootstrap-server localhost:9092 --create --topic kuke-board-article --replication-factor 1 --partitions 3
./kafka-topics.sh --bootstrap-server localhost:9092 --create --topic kuke-board-comment --replication-factor 1 --partitions 3
./kafka-topics.sh --bootstrap-server localhost:9092 --create --topic kuke-board-like --replication-factor 1 --partitions 3
./kafka-topics.sh --bootstrap-server localhost:9092 --create --topic kuke-board-view --replication-factor 1 --partitions 3
```

# DB init

## 데이터베이스 생성 
```mysql
create database article;
create database article_like;
create database article_view;
create database comment;
```


## 테이블 생성
### **schema :: article**
```mysql
create table article.article
(
    article_id  bigint        not null
        primary key,
    title       varchar(100)  not null,
    content     varchar(3000) not null,
    board_id    bigint        not null,
    writer_id   bigint        not null,
    created_at  datetime      not null,
    modified_at datetime      not null
);
```

```mysql
create table article.board_article_count
(
    board_id      bigint not null
        primary key,
    article_count bigint not null
);
```


### **schema :: article_like**
```mysql
create table article_like.article_like
(
    article_like_id bigint   not null
        primary key,
    article_id      bigint   not null,
    user_id         bigint   not null,
    created_at      datetime not null,
    constraint idx_article_id_user_id
        unique (article_id, user_id)
);
```

```mysql
create table article_like.article_like_count
(
    article_id bigint not null
        primary key,
    like_count bigint not null,
    version    bigint not null
);
```

### **schema :: article_view**
```mysql
create table article_view.article_view_count
(
    article_id bigint not null
        primary key,
    view_count bigint not null
);
```

### **schema :: comment**
```mysql
create table comment.article_comment_count
(
    article_id    bigint not null
        primary key,
    comment_count bigint not null
);
```

```mysql
create table comment.comment
(
    comment_id        bigint        not null
        primary key,
    content           varchar(3000) not null,
    article_id        bigint        not null,
    parent_comment_id bigint        not null,
    writer_id         bigint        not null,
    deleted           tinyint(1)    not null,
    created_at        datetime      not null
);
```

```mysql
create table comment.comment_v2
(
    comment_id bigint                          not null
        primary key,
    content    varchar(3000)                   not null,
    article_id bigint                          not null,
    writer_id  bigint                          not null,
    path       varchar(25) collate utf8mb4_bin not null,
    deleted    tinyint(1)                      not null,
    created_at datetime                        not null,
    constraint idx_article_id_path
        unique (article_id, path)
);
```

### **schema::공통**
```mysql
create table article.outbox(
    outbox_id BIGINT not null primary key ,
    shard_key bigint not null,
    event_type varchar(100) not null,
    payload varchar(5000) not null,
    created_at datetime not null
);
create index idx_shard_key_created_at on article.outbox(shard_key asc, created_at asc);

create table article_like.outbox(
    outbox_id BIGINT not null primary key ,
    shard_key bigint not null,
    event_type varchar(100) not null,
    payload varchar(5000) not null,
    created_at datetime not null
);
create index idx_shard_key_created_at on article_like.outbox(shard_key asc, created_at asc);

create table article_view.outbox(
    outbox_id BIGINT not null primary key ,
    shard_key bigint not null,
    event_type varchar(100) not null,
    payload varchar(5000) not null,
    created_at datetime not null
);
create index idx_shard_key_created_at on article_view.outbox(shard_key asc, created_at asc);

create table comment.outbox(
    outbox_id BIGINT not null primary key ,
    shard_key bigint not null,
    event_type varchar(100) not null,
    payload varchar(5000) not null,
    created_at datetime not null
);
create index idx_shard_key_created_at on comment.outbox(shard_key asc, created_at asc);
```