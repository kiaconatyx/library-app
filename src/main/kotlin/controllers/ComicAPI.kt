package controllers

import models.Comic
import persistence.Serializer

class ComicAPI(serializerType: Serializer){

    private var serializer: Serializer = serializerType
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


    fun archiveComic(indexToArchive: Int): Boolean {
        if (isValidIndex(indexToArchive)) {
            val comicToArchive = comics[indexToArchive]
            if (!comicToArchive.isComicArchived) {
                comicToArchive.isComicArchived = true
                return true
            }
        }
        return false
    }
    fun listComicsBySelectedGenre(cat: String): String =
        if (comics.isEmpty()) "No Comics Stored"
        else {
            val listOfComics = formatListString(comics.filter{ comic -> comic.comicGenre == cat})
            if (listOfComics.equals("")) "No comics with genre: $cat"
            else "${numberOfComicsByGenre(cat)} comics with genre $cat: $listOfComics"
        }


    fun numberOfComics(): Int = comics.size
    fun numberOfActiveComics(): Int = comics.count{comic: Comic -> !comic.isComicArchived}
    fun numberOfArchivedComics(): Int = comics.count{comic: Comic -> comic.isComicArchived}
    fun numberOfComicsByRating(rating: Int): Int = comics.count { p: Comic -> p.comicRating == rating }
    fun numberOfComicsByGenre(cat: String): Int = comics.count { p: Comic -> p.comicGenre == cat }

    fun deleteComic(indexToDelete: Int): Comic? {
        return if (isValidListIndex(indexToDelete, comics)) {
            comics.removeAt(indexToDelete)
        } else null
    }

    fun updateComic(indexToUpdate: Int, comic: Comic?): Boolean {
        //find the comic object by the index number
        val foundComic = findComic(indexToUpdate)

        //if the comic exists, use the comic details passed as parameters to update the found comic in the ArrayList.
        if ((foundComic != null) && (comic != null)) {
            foundComic.comicTitle = comic.comicTitle
            foundComic.comicRating = comic.comicRating
            foundComic.comicGenre = comic.comicGenre
            return true
        }

        //if the comic was not found, return false, indicating that the update was not successful
        return false
    }

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, comics);
    }

    @Throws(Exception::class)
    fun load() {
        comics = serializer.read() as ArrayList<Comic>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(comics)
    }
}