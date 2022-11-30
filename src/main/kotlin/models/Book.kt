package models

import Utils.Utilities

data class Book(var bookId: Int = 0,
                var bookTitle: String,
                var bookRating: Int,
                var bookISBN: Int,
                var bookGenre: String,
                var isBookArchived :Boolean = false,
                var libraries : MutableSet<Author> = mutableSetOf()) {

    private var lastAuthorId = 0
    private fun getAuthorId() = lastAuthorId++

    fun addAuthor(author: Author) : Boolean {
        author.authorId = getAuthorId()
        return libraries.add(author)

    }
    fun numberOfLibraries() = libraries.size

    fun findOne(id: Int): Author?{
        return libraries.find{ author -> author.authorId == id }
    }

    fun delete(id: Int): Boolean {
        return libraries.removeIf { author -> author.authorId == id}
    }

    fun update(id: Int, newAuthor: Author): Boolean {
        val foundAuthor = findOne(id)

        //if the object exists, use the details passed in the newBook parameter to
        //update the found object in the Set
        if (foundAuthor != null){
            foundAuthor.authorContents = newAuthor.authorContents
            foundAuthor.isAuthorComplete = newAuthor.isAuthorComplete
            return true
        }

        //if the object was not found, return false, indicating that the update was not successful
        return false
    }

    fun listLibraries() =
        if (libraries.isEmpty())  "\tNO BOOKS ADDED"
        else  Utilities.formatSetString(libraries)

    override fun toString(): String {
        val archived = if (isBookArchived) 'Y' else 'N'
        return "$bookId: $bookTitle, Rating($bookRating),ISBN($bookISBN), Genre($bookGenre), Archived($archived) \n${listLibraries()}"
    }

    fun checkBookCompletionStatus(): Boolean {
        if (libraries.isNotEmpty()) {
            for (author in libraries) {
                if (!author.isAuthorComplete) {
                    return false
                }
            }
        }
        return true //a book with empty libraries can be archived, or all libraries are complete
    }
}