package models

data class Book(var bookTitle: String,
                var bookRating: Int,
                var bookISBN: Int,
                var bookGenre: String,
                var isBookArchived :Boolean = false,
                var libraries : MutableSet<Library> = mutableSetOf()) {

    private var lastLibraryId = 0
    private fun getLibraryId() = lastLibraryId++

    fun addLibrary(library: Library) : Boolean {
        library.libraryId = getLibraryId()
        return libraries.add(library)

    }
    fun numberOfLibraries() = libraries.size

    fun findOne(id: Int): Library?{
        return libraries.find{ library -> library.libraryId == id }
    }

    fun delete(id: Int): Boolean {
        return books.removeIf { book -> book.bookId == id}
    }

    fun update(id: Int, newBook: Library): Boolean {
        val foundBook = findOne(id)

        //if the object exists, use the details passed in the newBook parameter to
        //update the found object in the Set
        if (foundBook != null){
            foundBook.bookContents = newBook.bookContents
            foundBook.isBookComplete = newBook.isBookComplete
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
            for (library in libraries) {
                if (!library.isLibraryComplete) {
                    return false
                }
            }
        }
        return true //a book with empty libraries can be archived, or all libraries are complete
    }
}