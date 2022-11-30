package models

import Utils.Utilities

data class Book(var bookId: Int = 0,
                var bookTitle: String,
                var bookRating: Int,
                var bookISBN: Int,
                var bookGenre: String,
                var isBookArchived :Boolean = false,
                var authors : MutableSet<Author> = mutableSetOf()) {

    private var lastAuthorId = 0
    private fun getAuthorId() = lastAuthorId++

    fun addAuthor(author: Author) : Boolean {
        author.authorId = getAuthorId()
        return authors.add(author)

    }
    fun numberOfAuthors() = authors.size

    fun findOne(id: Int): Author?{
        return authors.find{ author -> author.authorId == id }
    }

    fun delete(id: Int): Boolean {
        return authors.removeIf { author -> author.authorId == id}
    }

    fun update(id: Int, newAuthor: Author): Boolean {
        val foundAuthor = findOne(id)

        //if the object exists, use the details passed in the newBook parameter to
        //update the found object in the Set
        if (foundAuthor != null){
            foundAuthor.authorName = newAuthor.authorName
            foundAuthor.isAuthorActive = newAuthor.isAuthorActive
            return true
        }

        //if the object was not found, return false, indicating that the update was not successful
        return false
    }

    fun listAuthors() =
        if (authors.isEmpty())  "\tNO BOOKS ADDED"
        else  Utilities.formatSetString(authors)

    override fun toString(): String {
        val archived = if (isBookArchived) 'Y' else 'N'
        return "$bookId: $bookTitle, Rating($bookRating),ISBN($bookISBN), Genre($bookGenre), Archived($archived) \n${listAuthors()}"
    }

    fun checkBookCompletionStatus(): Boolean {
        if (authors.isNotEmpty()) {
            for (author in authors) {
                if (!author.isAuthorActive) {
                    return false
                }
            }
        }
        return true //a book with empty authors can be archived, or all authors are complete
    }
}