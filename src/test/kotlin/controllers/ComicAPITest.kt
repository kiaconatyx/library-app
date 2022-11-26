package controllers

import models.Comic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ComicAPITest {

    private var animeadventures: Comic? = null
    private var racetovictory : Comic? = null
    private var lovealittle: Comic? = null
    private var cookingmama: Comic? = null
    private var animalplanet: Comic? = null
    private var populatedComics: ComicAPI? = ComicAPI()
    private var emptyComics: ComicAPI? = ComicAPI()

    @BeforeEach
    fun setup(){
        animeadventures = Comic("Anime Aventures", 2, "Adventure", false)
        racetovictory = Comic("Race to Victory", 5, "Action",  false)
        lovealittle = Comic("Love a Little", 2, "Romance",  false)
        cookingmama = Comic("Cooking Mama", 5, "Cooking",  false)
        animalplanet = Comic("Animal Planet", 3, "Educational",  false)

        populatedComics!!.add(animeadventures!!)
        populatedComics!!.add(racetovictory!!)
        populatedComics!!.add(lovealittle!!)
        populatedComics!!.add(cookingmama!!)
        populatedComics!!.add(animalplanet!!)
    }

    @AfterEach
    fun tearDown(){
        animeadventures = null
        racetovictory = null
        lovealittle = null
        cookingmama = null
        animalplanet = null
        populatedComics = null
        emptyComics = null
    }

    @Test
    fun `adding a Comic to a populated list adds to ArrayList`(){
        val newComic = Comic("Vampire Slayer Part 1", 5, "fantasy", false)
        assertEquals(5, populatedComics!!.numberOfComics())
        assertTrue(populatedComics!!.add(newComic))
        assertEquals(6, populatedComics!!.numberOfComics())
        assertEquals(newComic, populatedComics!!.findComic(populatedComics!!.numberOfComics() - 1))
    }

    @Test
    fun `adding a Comic to an empty list adds to ArrayList`(){
        val newComic = Comic("Vampire Slayer Part 1", 5, "fantasy",  false)
        assertEquals(0, emptyComics!!.numberOfComics())
        assertTrue(emptyComics!!.add(newComic))
        assertEquals(1, emptyComics!!.numberOfComics())
        assertEquals(newComic, emptyComics!!.findComic(emptyComics!!.numberOfComics() - 1))
    }

    @Test
    fun `listAllComics returns No Comics Stored message when ArrayList is empty`() {
        assertEquals(0, emptyComics!!.numberOfComics())
        assertTrue(emptyComics!!.listAllComics().lowercase().contains("no comics to see"))
    }

    @Test
    fun `listAllComics returns Comics when ArrayList has notes stored`() {
        assertEquals(5, populatedComics!!.numberOfComics())
        val comicsString = populatedComics!!.listAllComics().lowercase()
        assertTrue(comicsString.contains("anime adventures"))
        assertTrue(comicsString.contains("race to victory"))
        assertTrue(comicsString.contains("love a little"))
        assertTrue(comicsString.contains("cooking mama"))
        assertTrue(comicsString.contains("animal planet"))
    }
}