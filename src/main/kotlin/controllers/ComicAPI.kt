package controllers

import models.Comic

class ComicAPI {
    private var comics = ArrayList<Comic>()

    fun add(comic: Comic): Boolean {
        return comics.add(comic)
    }

    fun listAllComics(): String {
        return if (comics.isEmpty()) {
            "No comics stored"
        } else {
            var listOfComics = ""
            for (i in comics.indices) {
                listOfComics += "${i}: ${comics[i]} \n"
            }
            listOfComics
        }
    }
}