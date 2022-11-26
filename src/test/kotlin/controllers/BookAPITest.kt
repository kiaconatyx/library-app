package controllers

import models.Book
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class BookAPITest {

    private var music101: Book? = null
    private var finalfantasy: Book? = null
    private var learntocode: Book? = null
    private var cookingfordummies: Book? = null
    private var dolphinworld: Book? = null
    private var populatedBooks: BookAPI? = BookAPI()
    private var emptyBooks: BookAPI? = BookAPI()

    @BeforeEach
    fun setup() {
        music101 = Book("Music 101", 2, 100, "music", false)
        finalfantasy = Book("Final Fantasy - The World Beyond", 1, 101, "fantasy", false)
        learntocode = Book("Learn to Code", 4, 102, "Tech", false)
        cookingfordummies = Book("Cooking for Dummies", 5, 103, "cooking", false)
        dolphinworld = Book("Dolphin World", 3, 104, "Animals & Nature", false)

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
            val newBook = Book("Buddy Holly Life Story", 5, 107, "bio", false)
            assertEquals(5, populatedBooks!!.numberOfBooks())
            assertTrue(populatedBooks!!.add(newBook))
            assertEquals(6, populatedBooks!!.numberOfBooks())
            assertEquals(newBook, populatedBooks!!.findBook(populatedBooks!!.numberOfBooks() - 1))
        }

        @Test
        fun `adding a Book to an empty list adds to ArrayList`() {
            val newBook = Book("Buddy Holly Life Story", 5, 107, "bio", false)
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.add(newBook))
            assertEquals(1, emptyBooks!!.numberOfBooks())
            assertEquals(newBook, emptyBooks!!.findBook(emptyBooks!!.numberOfBooks() - 1))
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
            assertTrue(genre4String.contains("priority 4"))
            assertFalse(genre4String.contains("dolphin world"))
            assertTrue(genre4String.contains("learn to code"))
            assertTrue(genre4String.contains("final fantasy - the world beyond"))
            assertFalse(genre4String.contains("music 101"))
            assertFalse(genre4String.contains("cooking for dummies"))
        }

    }
}