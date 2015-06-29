package main

import (
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
	"strconv"
)

type Record struct {
	Code      string
	Url       string
	OpenCount int
}

func main() {
	connectDB()
	try(db.AutoMigrate(&Record{}).Error)

	router := gin.New()
	router.Use(gin.Logger(), gin.Recovery())
	router.POST("/shorten", Shorten)
	router.GET("/expand/:code", Expand)
	router.GET("/r/:code", Redirect)
	router.GET("/statistics/:code", Stats)
	router.Run(":3001")
}

func Shorten(c *gin.Context) {
	url := c.PostForm("url")
	code := nextCode()
	record := Record{Url: url, Code: code}

	try(db.Create(&record).Error)
	c.String(200, "http://shorty.com/%s", code)
}

func Expand(c *gin.Context) {
	if record := findByCode(c); record.Url != "" {
		c.String(200, record.Url)
	}
}

func Redirect(c *gin.Context) {
	if record := findByCode(c); record.Url != "" {
		increaseCount(&record)
		c.Redirect(301, record.Url)
	}
}

func Stats(c *gin.Context) {
	if record := findByCode(c); record.Url != "" {
		c.String(200, strconv.Itoa(record.OpenCount))
	}
}

func findByCode(c *gin.Context) (record Record) {
	code := c.Param("code")
	err := db.Where("code = ?", code).First(&record).Error

	if err == gorm.RecordNotFound {
		c.String(404, "Not found")
	} else {
		try(err)
	}
	return record
}
