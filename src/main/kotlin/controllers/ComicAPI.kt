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

    fun numberOfComics(): Int {
        return comics.size
    }

    fun findComic(index: Int): Comic? {
        return if (isValidListIndex(index, comics)) {
            comics[index]
        } else null
    }

    //utility method to determine if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }
}