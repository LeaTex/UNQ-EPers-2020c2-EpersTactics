package testsPelea

import ar.edu.unq.epers.tactics.modelo.AtaqueExitosoRandomizer
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.service.impl.PartyServiceImplement
import helpers.DataService
import helpers.DataServiceImplement
import org.junit.After
import org.junit.Assert
import org.junit.Test

class AtaqueExitosoRandomizerTest {

    @Test
    fun testGetFixedValueInTestMode() {
        AtaqueExitosoRandomizer.setTestMode(5)
        Assert.assertEquals(5, AtaqueExitosoRandomizer.getValue())
        Assert.assertEquals(5, AtaqueExitosoRandomizer.getValue())
        Assert.assertEquals(5, AtaqueExitosoRandomizer.getValue())

        AtaqueExitosoRandomizer.setTestMode(13)
        Assert.assertEquals(13, AtaqueExitosoRandomizer.getValue())
        Assert.assertEquals(13, AtaqueExitosoRandomizer.getValue())
        Assert.assertEquals(13, AtaqueExitosoRandomizer.getValue())
    }

    @Test
    fun testGetRandomValueInRuntimeMode() {
        Assert.assertTrue(AtaqueExitosoRandomizer.getValue() >= 1)
        Assert.assertTrue(AtaqueExitosoRandomizer.getValue() >= 1)
        Assert.assertTrue(AtaqueExitosoRandomizer.getValue() <= 20)
        Assert.assertTrue(AtaqueExitosoRandomizer.getValue() <= 20)
    }

    @After
    fun tearDown() {
        AtaqueExitosoRandomizer.setRuntimeMode()
    }
}