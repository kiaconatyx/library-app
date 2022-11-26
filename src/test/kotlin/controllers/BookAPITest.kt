package controllers

import models.Book
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
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
    }
}