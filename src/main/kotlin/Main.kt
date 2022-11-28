import controllers.BookAPI
import controllers.ComicAPI
import models.Book
import models.Comic
import models.Library
import mu.KotlinLogging
import persistence.JSONSerializer
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit
private val logger = KotlinLogging.logger {}


//private val bookAPI = BookAPI(XMLSerializer(File("books.xml")))
private val bookAPI = BookAPI(JSONSerializer(File("books.json")))
//private val comicAPI = ComicAPI(XMLSerializer(File("comics.xml")))
private val comicAPI = ComicAPI(JSONSerializer(File("comics.json")))

fun main(args: Array<String>) {
    runMenu()
}


fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |       LIBRARY APP              |
         > ----------------------------------
         > | BOOK MENU                      |
         > |   1) Add a book                |
         > |   2) List all books            |
         > |   3) Update a book             |
         > |   4) Delete a book             |
         > |   5) Archive a book            |
         > ----------------------------------
         > | Comic MENU                     |
         > |   6) Add a Comic               |
         > |   7) List all Comics           |
         > |   8) Update a Comic            |
         > |   9) Delete a Comic            |
         > |   10) Archive a Comic          |
         > ----------------------------------
         > ----------------------------------
         > |   20) Save                     |
         > |   21) Load                     |
         > |   0) Exit                      |
         > ----------------------------------
        > ==>> """.trimMargin(">"))
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addBook()
            2  -> listBooks()
            3  -> updateBook()
            4  -> deleteBook()
            5 -> archiveBook()
            //7 -> updateLibraryContentsInBook()
            //8 -> deleteAnLibrary()
            //9 -> markLibraryStatus()
            6  -> addComic()
            7  -> listComics()
            8  -> updateComic()
            9  -> deleteComic()
            10 -> archiveComic()
            20 -> save()
            21 -> load()
            0  -> exitApp()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}

fun addBook(){
    val bookTitle = readNextLine("Enter the book's title: ")
    val bookRating = readNextInt("Enter a rating for the book (1-low, 2, 3, 4, 5-high): ")
    val bookISBN = readNextInt("Enter a unique ISBN for the book ")
    val bookGenre = readNextLine("Enter a suitable genre for the book ")
    val isAdded = bookAPI.add(Book(bookTitle, bookRating, bookISBN,bookGenre, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listBooks() {
    if (bookAPI.numberOfBooks() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL books          |
                  > |   2) View ACTIVE books       |
                  > |   3) View ARCHIVED books     |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllBooks();
            2 -> listActiveBooks();
            3 -> listArchivedBooks();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No books stored");
    }
}


fun listAllBooks() {
    println(bookAPI.listAllBooks())
}

fun listArchivedBooks() {
    println(bookAPI.listArchivedBooks())
}

fun updateBook(){
   // logger.info { "updateBook() function invoked" }
    listBooks()
    if (bookAPI.numberOfBooks() > 0) {
        //only ask the user to choose the book if books exist
        val indexToUpdate = readNextInt("Enter the index of the book to update: ")
        if (bookAPI.isValidIndex(indexToUpdate)) {
            val bookTitle = readNextLine("Enter a title for the book: ")
            val bookRating = readNextInt("Enter a rating (1-low, 2, 3, 4, 5-high): ")
            val bookISBN = readNextInt("Enter a Unique ISBN for the book ")
            val bookGenre = readNextLine("Enter a genre for the book: ")

            //pass the index of the book and the new book details to BookAPI for updating and check for success.
            if (bookAPI.updateBook(indexToUpdate, Book(bookTitle, bookRating, bookISBN,bookGenre, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no books for this index number")
        }
    }
}


fun deleteBook(){
    listBooks()
    if (bookAPI.numberOfBooks() > 0) {
        val indexToDelete = readNextInt("Enter the index of the book to delete: ")
        val bookToDelete = bookAPI.deleteBook(indexToDelete)
        if (bookToDelete != null) {
            println("Delete Successful! Deleted book: ${bookToDelete.bookTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun addComic(){
    val comicTitle = readNextLine("Enter the comics's title: ")
    val comicRating = readNextInt("Enter a rating for the comic (1-low, 2, 3, 4, 5-high): ")
    val comicGenre = readNextLine("Enter a suitable genre for the comic ")
    val isAdded = comicAPI.add(Comic(comicTitle, comicRating, comicGenre, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listComics() {
    if (comicAPI.numberOfComics() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL comics          |
                  > |   2) View ACTIVE comics       |
                  > |   3) View ARCHIVED comics     |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllComics();
            2 -> listActiveComics();
            3 -> listArchivedComics();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No comics stored");
    }
}

fun listAllComics() {
    println(comicAPI.listAllComics())
}

fun listArchivedComics() {
    println(comicAPI.listArchivedComics())
}
fun updateComic(){
    //logger.info { "updateComic() function invoked" }
    listComics()
    if (comicAPI.numberOfComics() > 0) {
        //only ask the user to choose the comic if comics exist
        val indexToUpdate = readNextInt("Enter the index of the comic to update: ")
        if (comicAPI.isValidIndex(indexToUpdate)) {
            val comicTitle = readNextLine("Enter a title for the comic: ")
            val comicRating = readNextInt("Enter a rating (1-low, 2, 3, 4, 5-high): ")
            val comicGenre = readNextLine("Enter a genre for the comic: ")

            //pass the index of the comic and the new comic details to ComicAPI for updating and check for success.
            if (comicAPI.updateComic(indexToUpdate, Comic(comicTitle, comicRating, comicGenre, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no comics for this index number")
        }
    }
}


fun deleteComic(){
    //logger.info { "deleteComic() function invoked" }
    listComics()
    if (comicAPI.numberOfComics() > 0) {
        val indexToDelete = readNextInt("Enter the index of the comic to delete: ")
        val comicToDelete = comicAPI.deleteComic(indexToDelete)
        if (comicToDelete != null) {
            println("Delete Successful! Deleted comic: ${comicToDelete.comicTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun listActiveBooks() {
    println(bookAPI.listActiveBooks())
}

fun archiveBook() {
    listActiveBooks()
    if (bookAPI.numberOfActiveBooks() > 0) {
        //only ask the user to choose the book to archive if active books exist
        val indexToArchive = readNextInt("Enter the index of the book to archive: ")
        //pass the index of the book to BookAPI for archiving and check for success.
        if (bookAPI.archiveBook(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

fun listActiveComics() {
    println(comicAPI.listActiveComics())
}

fun archiveComic() {
    listActiveComics()
    if (comicAPI.numberOfActiveComics() > 0) {
        //only ask the user to choose the comic to archive if active comics exist
        val indexToArchive = readNextInt("Enter the index of the comic to archive: ")
        //pass the index of the comic to ComicAPI for archiving and check for success.
        if (comicAPI.archiveComic(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

fun searchBooks() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = bookAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No books found")
    } else {
        println(searchResults)
    }
}

fun searchComics() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = comicAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No comics found")
    } else {
        println(searchResults)
    }
}


fun searchBooks() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = bookAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No books found")
    } else {
        println(searchResults)
    }
}

fun searchComics() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = comicAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No comics found")
    } else {
        println(searchResults)
    }
}
fun save() {
    try {
        bookAPI.store()
        comicAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        bookAPI.load()
        comicAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}
fun exitApp(){
    println("Exiting...bye")
    exit(0)
}