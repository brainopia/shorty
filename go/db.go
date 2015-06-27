package main

import (
	"database/sql"
	"github.com/jinzhu/gorm"
	_ "github.com/lib/pq"
	"github.com/peterhellberg/env"
	"github.com/tv42/base58"
	"log"
	"math/big"
)

var (
	db      gorm.DB
	codeSeq *sql.Stmt
)

func connectDB() {
	var err error

	info := env.String("DB_INFO", "dbname=demo sslmode=disable")
	db, err = gorm.Open("postgres", info)
	try(err)
	try(db.DB().Ping())

	db.Exec("CREATE SEQUENCE code_seq START 3364") // = 58*58
	codeSeq, err = db.DB().Prepare("SELECT nextval('code_seq')")
	try(err)
}

func nextCode() string {
	var number int64
	try(codeSeq.QueryRow().Scan(&number))
	code := base58.EncodeBig(nil, big.NewInt(number))
	return string(code)
}

func increaseCount(record *Record) {
	db.Model(record).Update("OpenCount", gorm.Expr("open_count + 1"))
}

func try(err error) {
	if err != nil {
		log.Fatal(err)
	}
}
