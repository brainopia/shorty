CREATE TABLE urls(
  code text,
  url varchar(8000), -- 8000 is from RFC7230
  open_count integer
);

CREATE UNIQUE INDEX code ON urls(code)
