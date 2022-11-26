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

    fun listActiveComics(): String {
        return if (numberOfActiveComics() == 0) {
            "No active comics here"
        } else {
            var listOfActiveComics = ""
            for (comic in comics) {
                if (!comic.isComicArchived) {
                    listOfActiveComics += "${comics.indexOf(comic)}: $comic \n"
                }
            }
            listOfActiveComics
        }
    }

    fun listArchivedComics(): String {
        return if (numberOfArchivedComics() == 0) {
            "No archived comics are found"
        } else {
            var listOfArchivedComics = ""
            for (comic in comics) {
                if (comic.isComicArchived) {
                    listOfArchivedComics += "${comics.indexOf(comic)}: $comic \n"
                }
            }
            listOfArchivedComics
        }
    }

    fun numberOfArchivedComics(): Int {
        var counter = 0
        for (comic in comics) {
            if (comic.isComicArchived) {
                counter++
            }
        }
        return counter
    }

    fun numberOfActiveComics(): Int {
        var counter = 0
        for (comic in comics) {
            if (!comic.isComicArchived) {
                counter++
            }
        }
        return counter
    }

    fun listComicsBySelectedRating(rating: Int): String {
        return if (comics.isEmpty()) {
            "No comics stored"
        } else {
            var listOfComics = ""
            for (i in comics.indices) {
                if (comics[i].comicRating == rating) {
                    listOfComics +=
                        """$i: ${comics[i]}
                        """.trimIndent()
                }
            }
            if (listOfComics.equals("")) {
                "No comics with rating: $rating"
            } else {
                "${numberOfComicsByRating(rating)} comics with a rating of $rating: $listOfComics"
            }
        }
    }

    fun numberOfComicsByRating(rating: Int): Int {
        var counter = 0
        for (comic in comics) {
            if (comic.comicRating == rating) {
                counter++
            }
        }
        return counter
    }

    fun listComicsBySelectedGenre(cat: String): String =
        if (comics.isEmpty()) "No Comics Stored"
        else {
            val listOfComics = formatListString(comics.filter{ comic -> comic.comicGenre == cat})
            if (listOfComics.equals("")) "No comics with genre: $cat"
            else "${numberOfComicsByGenre(cat)} comics with genre $cat: $listOfComics"
        }

    fun numberOfComicsByGenre(cat: String): Int = comics.count { p: Comic -> p.comicGenre == cat }

    fun deleteComic(indexToDelete: Int): Comic? {
        return if (isValidListIndex(indexToDelete, comics)) {
            comics.removeAt(indexToDelete)
        } else null
    }
}