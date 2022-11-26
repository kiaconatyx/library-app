package controllers

import models.Comic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ComicAPITest {

    private var animeadventures: Comic? = null
    private var racetovictory: Comic? = null
    private var lovealittle: Comic? = null
    private var cookingmama: Comic? = null
    private var animalplanet: Comic? = null
    private var populatedComics: ComicAPI? = ComicAPI()
    private var emptyComics: ComicAPI? = ComicAPI()

    @BeforeEach
    fun setup() {
        animeadventures = Comic("Anime Adventures", 2, "Adventure", false)
        racetovictory = Comic("Race to Victory", 5, "Action", false)
        lovealittle = Comic("Love a Little", 2, "Romance", false)
        cookingmama = Comic("Cooking Mama", 5, "Cooking", false)
        animalplanet = Comic("Animal Planet", 3, "Educational", false)

        populatedComics!!.add(animeadventures!!)
        populatedComics!!.add(racetovictory!!)
        populatedComics!!.add(lovealittle!!)
        populatedComics!!.add(cookingmama!!)
        populatedComics!!.add(animalplanet!!)
    }

    @Nested
    inner class AddComics {
        @AfterEach
        fun tearDown() {
            animeadventures = null
            racetovictory = null
            lovealittle = null
            cookingmama = null
            animalplanet = null
            populatedComics = null
            emptyComics = null
        }

        @Test
        fun `adding a Comic to a populated list adds to ArrayList`() {
            val newComic = Comic("Vampire Slayer Part 1", 5, "fantasy", false)
            assertEquals(5, populatedComics!!.numberOfComics())
            assertTrue(populatedComics!!.add(newComic))
            assertEquals(6, populatedComics!!.numberOfComics())
            assertEquals(newComic, populatedComics!!.findComic(populatedComics!!.numberOfComics() - 1))
        }

        @Test
        fun `adding a Comic to an empty list adds to ArrayList`() {
            val newComic = Comic("Vampire Slayer Part 1", 5, "fantasy", false)
            assertEquals(0, emptyComics!!.numberOfComics())
            assertTrue(emptyComics!!.add(newComic))
            assertEquals(1, emptyComics!!.numberOfComics())
            assertEquals(newComic, emptyComics!!.findComic(emptyComics!!.numberOfComics() - 1))
        }
    }

    @Nested
    inner class ListComics {
        @Test
        fun `listAllComics returns No Comics Stored message when ArrayList is empty`() {
            assertEquals(0, emptyComics!!.numberOfComics())
            assertTrue(emptyComics!!.listAllComics().lowercase().contains("no comics to see"))
        }

        @Test
        fun `listAllComics returns Comics when ArrayList has comics stored`() {
            assertEquals(5, populatedComics!!.numberOfComics())
            val comicsString = populatedComics!!.listAllComics().lowercase()
            assertTrue(comicsString.contains("anime adventures"))
            assertTrue(comicsString.contains("race to victory"))
            assertTrue(comicsString.contains("love a little"))
            assertTrue(comicsString.contains("cooking mama"))
            assertTrue(comicsString.contains("animal planet"))
        }

        @Test
        fun `listActiveComics returns no active comics stored when ArrayList is empty`() {
            assertEquals(0, emptyComics!!.numberOfActiveComics())
            assertTrue(
                emptyComics!!.listActiveComics().lowercase().contains("no active comics")
            )
        }

        @Test
        fun `listActiveComics returns active comics when ArrayList has active comics stored`() {
            assertEquals(3, populatedComics!!.numberOfActiveComics())
            val activeComicsString = populatedComics!!.listActiveComics().lowercase()
            assertTrue(activeComicsString.contains("anime adventures"))
            assertFalse(activeComicsString.contains("race to victory"))
            assertTrue(activeComicsString.contains("love a little"))
            assertTrue(activeComicsString.contains("cooking mama"))
            assertFalse(activeComicsString.contains("animal planet"))
        }

        @Test
        fun `listArchivedComics returns no archived comics when ArrayList is empty`() {
            assertEquals(0, emptyComics!!.numberOfArchivedComics())
            assertTrue(
                emptyComics!!.listArchivedComics().lowercase().contains("no archived comics")
            )
        }

        @Test
        fun `listArchivedComics returns archived comics when ArrayList has archived comics stored`() {
            assertEquals(2, populatedComics!!.numberOfArchivedComics())
            val archivedComicsString = populatedComics!!.listArchivedComics().lowercase(Locale.getDefault())
            assertFalse(archivedComicsString.contains("anime adventures"))
            assertTrue(archivedComicsString.contains("race to victory"))
            assertFalse(archivedComicsString.contains("love a little"))
            assertFalse(archivedComicsString.contains("cooking mama"))
            assertTrue(archivedComicsString.contains("animal planet"))
        }

        @Test
        fun `listComicsBySelectedRating returns No Comics when ArrayList is empty`() {
            assertEquals(0, emptyComics!!.numberOfComics())
            assertTrue(emptyComics!!.listComicsBySelectedRating(1).lowercase().contains("no comics")
            )
        }

        @Test
        fun `listComicsBySelectedRating returns no comics when no comics of that rating exist`() {
            //Rating 1 (1 comic), 2 (none), 3 (1 comic). 4 (2 comics), 5 (1 comic)
            assertEquals(5, populatedComics!!.numberOfComics())
            val rating2String = populatedComics!!.listComicsBySelectedRating(2).lowercase()
            assertTrue(rating2String.contains("no comics"))
            assertTrue(rating2String.contains("2"))
        }

        @Test
        fun `listComicsBySelectedRating returns all comics that match that rating when comics of that rating exist`() {
            //Rating 1 (1 comic), 2 (none), 3 (1 comic). 4 (2 comics), 5 (1 comic)
            assertEquals(5, populatedComics!!.numberOfComics())
            val rating1String = populatedComics!!.listComicsBySelectedRating(1).lowercase()
            assertTrue(rating1String.contains("1 comic"))
            assertTrue(rating1String.contains("rating 1"))
            assertTrue(rating1String.contains("anime adventures"))
            assertFalse(rating1String.contains("race to victory"))
            assertFalse(rating1String.contains("love a little"))
            assertFalse(rating1String.contains("cooking mama"))
            assertFalse(rating1String.contains("animal planet"))


            val rating4String = populatedComics!!.listComicsBySelectedRating(4).lowercase(Locale.getDefault())
            assertTrue(rating4String.contains("2 comic"))
            assertTrue(rating4String.contains("rating 4"))
            assertFalse(rating4String.contains("race to victory"))
            assertTrue(rating4String.contains("cooking mama"))
            assertTrue(rating4String.contains("animal planet"))
            assertFalse(rating4String.contains("love a little"))
            assertFalse(rating4String.contains("anime adventures"))
        }
    }
}