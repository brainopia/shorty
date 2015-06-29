package main

import "log"

func try(err error) {
	if err != nil {
		log.Panic(err)
	}
}
