import controllers.BookAPI
import controllers.ComicAPI
import models.Book
import models.Comic
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
         > ----------------------------------
         > | Comic MENU                     |
         > |   5) Add a Comic               |
         > |   6) List all Comics           |
         > |   7) Update a Comic            |
         > |   8) Delete a Comic            |
         > ----------------------------------
         > ----------------------------------
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
            5  -> addComic()
            6  -> listComics()
            7  -> updateComic()
            8  -> deleteComic()
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

fun listBooks(){
    println(bookAPI.listAllBooks())
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

fun listComics(){
    println(comicAPI.listAllComics())
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