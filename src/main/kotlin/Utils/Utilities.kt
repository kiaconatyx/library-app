package Utils

import models.Author
import models.Book

object Utilities {

    // NOTE: JvmStatic annotation means that the methods are static i.e. we can call them over the class
    //      name; we don't have to create an object of Utilities to use them.

    @JvmStatic
    fun formatListString(booksToFormat: List<Book>): String =
        booksToFormat
            .joinToString(separator = "\n") { book ->  "$book" }

    @JvmStatic
    fun formatSetString(librariesToFormat: Set<Author>): String =
        librariesToFormat
            .joinToString(separator = "\n") { book ->  "\t$book" }

}
