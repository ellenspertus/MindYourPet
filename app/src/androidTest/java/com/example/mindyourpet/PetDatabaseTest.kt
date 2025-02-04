package com.example.mindyourpet

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.mindyourpet.database.Pet
import com.example.mindyourpet.database.PetDatabase
import com.example.mindyourpet.database.PetDatabaseDao
import com.example.mindyourpet.database.Reminder
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws


/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class PetDatabaseTest {

    private lateinit var petDao: PetDatabaseDao
    private lateinit var db: PetDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, PetDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        petDao = db.PetDatabaseDao

        initializeDatabase()
    }
    fun initializeDatabase(){

        val p = Pet(petId = 0,name="Bingo", speciesId = 0)
        val p1=Pet(petId = 1,name="Jeans",speciesId = 1)
        val p2=Pet(petId = 2,name="Kim", speciesId = 4)
        val p3 = Pet(petId = 3,name="Bob",speciesId = 3)
        val p4 = Pet(petId = 4,name="Burro", speciesId = 2)
        val p5 = Pet(petId = 5,name="Elvert",speciesId =  0)


        runBlocking {
            petDao.addPet(p)
            petDao.addPet(p1)
            petDao.addPet(p2)
            petDao.addPet(p3)
            petDao.addPet(p4)
            petDao.addPet(p5)
        }
    }
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    //this method is to get info with petId
    //it will return name and speciesId
    @Test
    @Throws(Exception::class)
    fun getName() = runBlocking {
        val newPet = petDao.getName(1)
        assertEquals("Bingo", newPet.name)
        assertEquals(0, newPet.speciesId)
        val newPet1 = petDao.getName(2)
        assertEquals("Jeans", newPet.name)
        assertEquals(1, newPet.speciesId)
    }

    //For Species Id: 0 == cat 1==dog 2==bird 3==Monkey 4==fish
    @Test
    @Throws(Exception::class)
    fun getAllSameSpeciesName() = runBlocking {

        var allCats = petDao.getAllSpeciesId(0)
        assertEquals(2, allCats.size)
        assertEquals("Bingo", allCats.get(0).name)
        assertEquals("Elvert", allCats.get(1).name)
    }

    @Test
    @Throws(Exception::class)
    fun getSpeciesIdWithName() = runBlocking {
        val petNameReturn = petDao.getPetId("Bingo")
        assertEquals(0,petNameReturn.petId)
        //assertEquals(p5, petNameReturn.speciesId)
    }

}