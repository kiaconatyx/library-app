import java.lang.System.exit
import java.util.*
val scanner = Scanner(System.`in`)

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu() : Int {
    println("")
    println("--------------------")
    println("Book Depo APP")
    println("--------------------")
    println("Book MENU")
    println("  1) Add a book")
    println("  2) List all books")
    println("  3) Update a book")
    println("  4) Delete a book")
    println("--------------------")
    println("  0) Exit")
    println("--------------------")
    print("==>> ")
    return scanner.nextInt()
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addBook()
            2  -> listBooks()
            3  -> updateBook()
            4  -> deleteBook()
            0  -> exitApp()
            else -> println("Invalid option entered: " + option)
        }
    } while (true)
}

fun addBook(){
    println("You chose to add a new book")
}

fun listBooks(){
    println("You chose to list all books")
}

fun updateBook(){
    println("You chose to Update books")
}

fun deleteBook(){
    println("You chose to Delete a book")
}

fun exitApp(){
    println("Exiting...bye")
    exit(0)
}