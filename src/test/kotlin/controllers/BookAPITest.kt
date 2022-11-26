package controllers

import models.Book
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BookAPITest {

    private var music101: Book? = null
    private var finalfantasy : Book? = null
    private var learntocode: Book? = null
    private var cookingfordummies: Book? = null
    private var dolphinworld: Book? = null
    private var populatedBooks: BookAPI? = BookAPI()
    private var emptyBooks: BookAPI? = BookAPI()

    @BeforeEach
    fun setup(){
        music101 = Book("Music 101", 2, 100, "music",false)
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
    fun tearDown(){
        music101 = null
        finalfantasy = null
        learntocode = null
        cookingfordummies = null
        dolphinworld = null
        populatedBooks = null
        emptyBooks = null
    }

    @Test
    fun `adding a Book to a populated list adds to ArrayList`(){
        val newBook = Book("Buddy Holly Life Story", 5, 107, "bio",false)
        assertEquals(5, populatedBooks!!.numberOfBooks())
        assertTrue(populatedBooks!!.add(newBook))
        assertEquals(6, populatedBooks!!.numberOfBooks())
        assertEquals(newBook, populatedBooks!!.findBook(populatedBooks!!.numberOfBooks() - 1))
    }

    @Test
    fun `adding a Book to an empty list adds to ArrayList`(){
        val newBook = Book("Buddy Holly Life Story", 5, 107, "bio", false)
        assertEquals(0, emptyBooks!!.numberOfBooks())
        assertTrue(emptyBooks!!.add(newBook))
        assertEquals(1, emptyBooks!!.numberOfBooks())
        assertEquals(newBook, emptyBooks!!.findBook(emptyBooks!!.numberOfBooks() - 1))
    }
}