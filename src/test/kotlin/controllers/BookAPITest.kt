package controllers

import models.Book
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class BookAPITest {

    private var music101: Book? = null
    private var finalfantasy: Book? = null
    private var learntocode: Book? = null
    private var cookingfordummies: Book? = null
    private var dolphinworld: Book? = null
    private var populatedBooks: BookAPI? = BookAPI(XMLSerializer(File("books.xml")))
    private var emptyBooks: BookAPI? = BookAPI(XMLSerializer(File("books.xml")))

    @BeforeEach
    fun setup() {
        music101 = Book(101,"Music 101", 2, 100, "music", false)
        finalfantasy = Book(102,"Final Fantasy - The World Beyond", 1, 101, "fantasy", false)
        learntocode = Book(103,"Learn to Code", 4, 102, "Tech", false)
        cookingfordummies = Book(104,"Cooking for Dummies", 5, 103, "cooking", false)
        dolphinworld = Book(105,"Dolphin World", 3, 104, "Animals & Nature", false)

        populatedBooks!!.add(music101!!)
        populatedBooks!!.add(finalfantasy!!)
        populatedBooks!!.add(learntocode!!)
        populatedBooks!!.add(cookingfordummies!!)
        populatedBooks!!.add(dolphinworld!!)
    }

    @AfterEach
    fun tearDown() {
        music101 = null
        finalfantasy = null
        learntocode = null
        cookingfordummies = null
        dolphinworld = null
        populatedBooks = null
        emptyBooks = null
    }

    @Nested
    inner class AddBooks {
        @Test
        fun `adding a Book to a populated list adds to ArrayList`() {
            val newBook = Book(105,"Buddy Holly Life Story", 5, 107, "bio", false)
            assertEquals(5, populatedBooks!!.numberOfBooks())
            assertTrue(populatedBooks!!.add(newBook))
            assertEquals(6, populatedBooks!!.numberOfBooks())
            assertEquals(newBook, populatedBooks!!.findBook(populatedBooks!!.numberOfBooks() - 1))
        }

        @Test
        fun `adding a Book to an empty list adds to ArrayList`() {
            val newBook = Book(105,"Buddy Holly Life Story", 5, 107, "bio", false)
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.add(newBook))
            assertEquals(1, emptyBooks!!.numberOfBooks())
            assertEquals(newBook, emptyBooks!!.findBook(emptyBooks!!.numberOfBooks() - 1))
        }
    }

    @Nested
    inner class DeleteBooks {

        @Test
        fun `deleting a Book that does not exist, returns null`() {
            assertNull(emptyBooks!!.deleteBook(0))
            assertNull(populatedBooks!!.deleteBook(-1))
            assertNull(populatedBooks!!.deleteBook(5))
        }

        @Test
        fun `deleting a book that exists delete and returns deleted object`() {
            assertEquals(5, populatedBooks!!.numberOfBooks())
            assertEquals(dolphinworld, populatedBooks!!.deleteBook(4))
            assertEquals(4, populatedBooks!!.numberOfBooks())
            assertEquals(music101, populatedBooks!!.deleteBook(0))
            assertEquals(3, populatedBooks!!.numberOfBooks())
        }
    }

    @Nested
    inner class UpdateBooks {
        @Test
        fun `updating a book that does not exist returns false`(){
            assertFalse(populatedBooks!!.updateBook(6, Book(108,"Updating Book", 2, 113, "Fiction", false)))
            assertFalse(populatedBooks!!.updateBook(-1, Book(108,"Updating Book", 2, 114, "True Crime", false)))
            assertFalse(emptyBooks!!.updateBook(0, Book(108,"Updating Book", 2, 112,"Music",  false)))
        }

        @Test
        fun `updating a book that exists returns true and updates`() {
            //check book 5 exists and check the contents
            assertEquals(dolphinworld, populatedBooks!!.findBook(4))
            assertEquals("Dolphin World", populatedBooks!!.findBook(4)!!.bookTitle)
            assertEquals(3, populatedBooks!!.findBook(4)!!.bookRating)
            assertEquals("Nature", populatedBooks!!.findBook(4)!!.bookGenre)

            //update book 5 with new information and ensure contents updated successfully
            assertTrue(populatedBooks!!.updateBook(4, Book(106,"Updating Book", 2, 102, "Nature", false)))
            assertEquals("Updating Book", populatedBooks!!.findBook(4)!!.bookTitle)
            assertEquals(2, populatedBooks!!.findBook(4)!!.bookRating)
            assertEquals("Animals", populatedBooks!!.findBook(4)!!.bookGenre)
        }
    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty books.XML file.
            val storingBooks = BookAPI(XMLSerializer(File("books.xml")))
            storingBooks.store()

            //Loading the empty books.xml file into a new object
            val loadedBooks = BookAPI(XMLSerializer(File("books.xml")))
            loadedBooks.load()

            //Comparing the source of the books (storingBooks) with the XML loaded books (loadedBooks)
            assertEquals(0, storingBooks.numberOfBooks())
            assertEquals(0, loadedBooks.numberOfBooks())
            assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 books to the books.XML file.
            val storingBooks = BookAPI(XMLSerializer(File("books.xml")))
            storingBooks.add(finalfantasy!!)
            storingBooks.add(dolphinworld!!)
            storingBooks.add(cookingfordummies!!)
            storingBooks.store()

            //Loading books.xml into a different collection
            val loadedBooks = BookAPI(XMLSerializer(File("books.xml")))
            loadedBooks.load()

            //Comparing the source of the books (storingBooks) with the XML loaded books (loadedBooks)
            assertEquals(3, storingBooks.numberOfBooks())
            assertEquals(3, loadedBooks.numberOfBooks())
            assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())
            assertEquals(storingBooks.findBook(0), loadedBooks.findBook(0))
            assertEquals(storingBooks.findBook(1), loadedBooks.findBook(1))
            assertEquals(storingBooks.findBook(2), loadedBooks.findBook(2))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty books.json file.
            val storingBooks = BookAPI(JSONSerializer(File("books.json")))
            storingBooks.store()

            //Loading the empty books.json file into a new object
            val loadedBooks = BookAPI(JSONSerializer(File("books.json")))
            loadedBooks.load()

            //Comparing the source of the books (storingBooks) with the json loaded books (loadedBooks)
            assertEquals(0, storingBooks.numberOfBooks())
            assertEquals(0, loadedBooks.numberOfBooks())
            assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 books to the books.json file.
            val storingBooks = BookAPI(JSONSerializer(File("books.json")))
            storingBooks.add(music101!!)
            storingBooks.add(dolphinworld!!)
            storingBooks.add(cookingfordummies!!)
            storingBooks.store()

            //Loading books.json into a different collection
            val loadedBooks = BookAPI(JSONSerializer(File("books.json")))
            loadedBooks.load()

            //Comparing the source of the books (storingBooks) with the json loaded books (loadedBooks)
            assertEquals(3, storingBooks.numberOfBooks())
            assertEquals(3, loadedBooks.numberOfBooks())
            assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())
            assertEquals(storingBooks.findBook(0), loadedBooks.findBook(0))
            assertEquals(storingBooks.findBook(1), loadedBooks.findBook(1))
            assertEquals(storingBooks.findBook(2), loadedBooks.findBook(2))
        }
    }

    @Nested
    inner class ArchiveBooks {
        @Test
        fun `archiving a book that does not exist returns false`(){
            assertFalse(populatedBooks!!.archiveBook(6))
            assertFalse(populatedBooks!!.archiveBook(-1))
            assertFalse(emptyBooks!!.archiveBook(0))
        }

        @Test
        fun `archiving an already archived book returns false`(){
            assertTrue(populatedBooks!!.findBook(2)!!.isBookArchived)
            assertFalse(populatedBooks!!.archiveBook(2))
        }

        @Test
        fun `archiving an active book that exists returns true and archives`() {
            assertFalse(populatedBooks!!.findBook(1)!!.isBookArchived)
            assertTrue(populatedBooks!!.archiveBook(1))
            assertTrue(populatedBooks!!.findBook(1)!!.isBookArchived)
        }
    }
    @Nested
    inner class ListBooks {
        @Test
        fun `listAllBooks returns No Books Stored message when ArrayList is empty`() {
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.listAllBooks().lowercase().contains("no books to see here"))
        }

        @Test
        fun `listAllBooks returns Books when ArrayList has books stored`() {
            assertEquals(5, populatedBooks!!.numberOfBooks())
            val booksString = populatedBooks!!.listAllBooks().lowercase()
            assertTrue(booksString.contains("music 101"))
            assertTrue(booksString.contains("learn to code"))
            assertTrue(booksString.contains("cooking for dummies"))
            assertTrue(booksString.contains("final fantasy - the world beyond"))
            assertTrue(booksString.contains("dolphin world"))
        }

        @Test
        fun `listActiveBooks returns no active books stored when ArrayList is empty`() {
            assertEquals(0, emptyBooks!!.numberOfActiveBooks())
            assertTrue(
                emptyBooks!!.listActiveBooks().lowercase().contains("no active books")
            )
        }

        @Test
        fun `listActiveBooks returns active books when ArrayList has active books stored`() {
            assertEquals(3, populatedBooks!!.numberOfActiveBooks())
            val activeBooksString = populatedBooks!!.listActiveBooks().lowercase()
            assertTrue(activeBooksString.contains("music 101"))
            assertFalse(activeBooksString.contains("learn to code"))
            assertTrue(activeBooksString.contains("final fantasy - the world beyond"))
            assertTrue(activeBooksString.contains("cooking for dummies"))
            assertFalse(activeBooksString.contains("dolphin world"))
        }

        @Test
        fun `listArchivedBooks returns no archived books when ArrayList is empty`() {
            assertEquals(0, emptyBooks!!.numberOfArchivedBooks())
            assertTrue(
                emptyBooks!!.listArchivedBooks().lowercase().contains("no archived books")
            )
        }

        @Test
        fun `listArchivedBooks returns archived books when ArrayList has archived books stored`() {
            assertEquals(2, populatedBooks!!.numberOfArchivedBooks())
            val archivedBooksString = populatedBooks!!.listArchivedBooks().lowercase(Locale.getDefault())
            assertFalse(archivedBooksString.contains("music 101"))
            assertTrue(archivedBooksString.contains("learn to code"))
            assertFalse(archivedBooksString.contains("final fantasy - the world beyond"))
            assertFalse(archivedBooksString.contains("cooking for dummies"))
            assertTrue(archivedBooksString.contains("dolphin world"))
        }

        @Test
        fun `listBooksBySelectedRating returns No Books when ArrayList is empty`() {
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.listBooksBySelectedRating(1).lowercase().contains("no books")
            )
        }

        @Test
        fun `listBooksBySelectedRating returns no books when no books of that rating exist`() {
            assertEquals(5, populatedBooks!!.numberOfBooks())
            val rating2String = populatedBooks!!.listBooksBySelectedRating(2).lowercase()
            assertTrue(rating2String.contains("no books"))
            assertTrue(rating2String.contains("2"))
        }

        @Test
        fun `listBooksBySelectedRating returns all books that match that rating when books of that rating exist`() {
            assertEquals(5, populatedBooks!!.numberOfBooks())
            val rating1String = populatedBooks!!.listBooksBySelectedRating(1).lowercase()
            assertTrue(rating1String.contains("1 comic"))
            assertTrue(rating1String.contains("rating 1"))
            assertTrue(rating1String.contains("music 101"))
            assertFalse(rating1String.contains("dolphin world"))
            assertFalse(rating1String.contains("cooking for dummies"))
            assertFalse(rating1String.contains("learn to code"))
            assertFalse(rating1String.contains("final fantasy - the world beyond"))


            val rating4String = populatedBooks!!.listBooksBySelectedRating(4).lowercase(Locale.getDefault())
            assertTrue(rating4String.contains("2 comic"))
            assertTrue(rating4String.contains("rating 4"))
            assertFalse(rating4String.contains("dolphin world"))
            assertTrue(rating4String.contains("learn to code"))
            assertTrue(rating4String.contains("cooking for dummies"))
            assertFalse(rating4String.contains("music 101"))
            assertFalse(rating4String.contains("final fantasy - the world beyond"))
        }

        @Test
        fun `listBooksBySelectedGenre returns No Books when ArrayList is empty`() {
            Assertions.assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.listBooksBySelectedGenre("Work").lowercase().contains("no books")
            )
        }

        @Test
        fun `listBooksBySelectedGenre returns no books when no books of that genre exist`() {
            //Genre 1 (1 book), 2 (none), 3 (1 book). 4 (2 books), 5 (1 book)
            Assertions.assertEquals(5, populatedBooks!!.numberOfBooks())
            val genre2String = populatedBooks!!.listBooksBySelectedGenre("Work").lowercase()
            assertTrue(genre2String.contains("no books"))
            assertTrue(genre2String.contains("2"))
        }
        @Test
        fun `listBooksBySelectedGenre returns all books that match that genre when books of that genre exist`() {
            //Genre 1 (1 book), 2 (none), 3 (1 book). 4 (2 books), 5 (1 book)
            Assertions.assertEquals(5, populatedBooks!!.numberOfBooks())
            val genre1String = populatedBooks!!.listBooksBySelectedGenre("Hobby").lowercase()
            assertTrue(genre1String.contains("1 book"))
            assertTrue(genre1String.contains("genre 1"))
            assertTrue(genre1String.contains("cooking for dummies"))
            assertFalse(genre1String.contains("dolphin world"))
            assertFalse(genre1String.contains("music 101"))
            assertFalse(genre1String.contains("learn to code"))
            assertFalse(genre1String.contains("final fantasy - the world beyond"))

            val genre4String = populatedBooks!!.listBooksBySelectedGenre("Work").lowercase()
            assertTrue(genre4String.contains("2 book"))
            assertTrue(genre4String.contains("genre 4"))
            assertFalse(genre4String.contains("dolphin world"))
            assertTrue(genre4String.contains("learn to code"))
            assertTrue(genre4String.contains("final fantasy - the world beyond"))
            assertFalse(genre4String.contains("music 101"))
            assertFalse(genre4String.contains("cooking for dummies"))
        }

        @Test
        fun `listBooksBySelectedISBN returns No Books when ArrayList is empty`() {
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.listBooksBySelectedISBN(1).lowercase().contains("no books")
            )
        }

        @Test
        fun `listBooksBySelectedISBN returns no books when no books of that ISBN exist`() {
            assertEquals(5, populatedBooks!!.numberOfBooks())
            val ISBNString = populatedBooks!!.listBooksBySelectedISBN(2).lowercase()
            assertTrue(ISBNString.contains("no books"))
            assertTrue(ISBNString.contains("2"))
        }

        @Test
        fun `listBooksBySelectedISBN returns all books that match that ISBN when books of that ISBN exist`() {
            assertEquals(5, populatedBooks!!.numberOfBooks())
            val ISBN1String = populatedBooks!!.listBooksBySelectedISBN(1).lowercase()
            assertTrue(ISBN1String.contains("1 comic"))
            assertTrue(ISBN1String.contains("rating 1"))
            assertTrue(ISBN1String.contains("music 101"))
            assertFalse(ISBN1String.contains("dolphin world"))
            assertFalse(ISBN1String.contains("cooking for dummies"))
            assertFalse(ISBN1String.contains("learn to code"))
            assertFalse(ISBN1String.contains("final fantasy - the world beyond"))


            val ISBN4String = populatedBooks!!.listBooksBySelectedISBN(4).lowercase(Locale.getDefault())
            assertTrue(ISBN4String.contains("100 comic"))
            assertTrue(ISBN4String.contains("rating 4"))
            assertFalse(ISBN4String.contains("dolphin world"))
            assertTrue(ISBN4String.contains("learn to code"))
            assertTrue(ISBN4String.contains("cooking for dummies"))
            assertFalse(ISBN4String.contains("music 101"))
            assertFalse(ISBN4String.contains("final fantasy - the world beyond"))
        }

    }
    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfBooksCalculatedCorrectly() {
            assertEquals(5, populatedBooks!!.numberOfBooks())
            assertEquals(0, emptyBooks!!.numberOfBooks())
        }

        @Test
        fun numberOfArchivedBooksCalculatedCorrectly() {
            assertEquals(2, populatedBooks!!.numberOfArchivedBooks())
            assertEquals(0, emptyBooks!!.numberOfArchivedBooks())
        }

        @Test
        fun numberOfActiveBooksCalculatedCorrectly() {
            assertEquals(3, populatedBooks!!.numberOfActiveBooks())
            assertEquals(0, emptyBooks!!.numberOfActiveBooks())
        }

        @Test
        fun numberOfBooksByRatingCalculatedCorrectly() {
            assertEquals(1, populatedBooks!!.numberOfBooksByRating(1))
            assertEquals(0, populatedBooks!!.numberOfBooksByRating(2))
            assertEquals(1, populatedBooks!!.numberOfBooksByRating(3))
            assertEquals(2, populatedBooks!!.numberOfBooksByRating(4))
            assertEquals(1, populatedBooks!!.numberOfBooksByRating(5))
            assertEquals(0, emptyBooks!!.numberOfBooksByRating(1))
        }

        @Test
        fun numberOfBooksByISBNCalculatedCorrectly() {
            assertEquals(1, populatedBooks!!.numberOfBooksByISBN(1))
            assertEquals(0, populatedBooks!!.numberOfBooksByISBN(2))
            assertEquals(1, populatedBooks!!.numberOfBooksByISBN(3))
            assertEquals(2, populatedBooks!!.numberOfBooksByISBN(4))
            assertEquals(1, populatedBooks!!.numberOfBooksByISBN(5))
            assertEquals(0, emptyBooks!!.numberOfBooksByISBN(1))
        }
    }

    @Nested
    inner class SearchMethods {

        @Test
        fun `search books by title returns no books when no books with that title exist`() {
            //Searching a populated collection for a title that doesn't exist.
            Assertions.assertEquals(5, populatedBooks!!.numberOfBooks())
            val searchResults = populatedBooks!!.searchByTitle("no results expected")
            assertTrue(searchResults.isEmpty())

            //Searching an empty collection
            Assertions.assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.searchByTitle("").isEmpty())
        }

        @Test
        fun `search books by title returns books when books with that title exist`() {
            Assertions.assertEquals(5, populatedBooks!!.numberOfBooks())

            //Searching a populated collection for a full title that exists (case matches exactly)
            var searchResults = populatedBooks!!.searchByTitle("Learn To Code")
            assertTrue(searchResults.contains("Learn To Code"))
            assertFalse(searchResults.contains("Cooking for Dummies"))

            //Searching a populated collection for a partial title that exists (case matches exactly)
            searchResults = populatedBooks!!.searchByTitle("Do")
            assertTrue(searchResults.contains("Learn To Code"))
            assertTrue(searchResults.contains("Dolphin World"))
            assertFalse(searchResults.contains("Cooking for Dummies"))

            //Searching a populated collection for a partial title that exists (case doesn't match)
            searchResults = populatedBooks!!.searchByTitle("F")
            assertTrue(searchResults.contains("Final Fantasy"))
            assertTrue(searchResults.contains("Cooking For Dummies"))
            assertFalse(searchResults.contains("Dolphin World"))
        }
    }
    
}