package blockchainTests

import ar.edu.unq.epers.tactics.modelo.*
import ar.edu.unq.epers.tactics.persistencia.dao.mongodb.BloqueDAO
import ar.edu.unq.epers.tactics.service.BlockchainService
import ar.edu.unq.epers.tactics.service.impl.BlockchainServiceImplement
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class BlockchainServiceTest {

    lateinit var service: BlockchainService

    @Before
    fun setup() {
        service = BlockchainServiceImplement(30, BloqueDAO())
    }

    @Test
    fun guardarGenesisBlockSinTransacciones() {
        var bloqueNro = service.agregarBloque(0,
                        "2020-05-12 15:30:31.000",
                            mutableListOf(),
                        48472)

        var bloque = service.obtenerBloque(0)

        Assert.assertEquals(0, bloqueNro)
        Assert.assertEquals(bloqueNro, bloque.numero)
    }

    @Test
    fun seGuardaUnNuevoBloqueConTransacciones() {
        generateGenesisBlock()

        var transacciones: MutableList<Transaccion> = mutableListOf()
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletMinero", "walletPartyUno", 150))
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletMinero", "walletPartyDos", 150))
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletMinero", "walletPartyTres", 200))
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletMinero", "walletPartyCuatro", 200))


        var bloqueNro = service.agregarBloque(1, "2020-05-12 15:35:55.000", transacciones, 79757)
        var bloque = service.obtenerBloque(1)

        Assert.assertEquals(1, bloqueNro)
        Assert.assertEquals(bloqueNro, bloque.numero)
    }

    @Test
    fun seAgreganMasBloquesValidosALaCadena() {
        generateGenesisBlock()
        generateBlockNro1()
        generateBlockNro2()
        generateBlockNro3()

        var nonce = service.minarBloque(4,
                "2020-05-12 15:30:31.000",
                "00008F87C7CBAC71C1A706A6FE4EB280054674D2031427A5C307CA9C8D53987C",
                    mutableListOf())

        Assert.assertEquals(5002, nonce)


        var bloqueNro = service.agregarBloque(4, "2020-05-12 15:30:31.000", mutableListOf(), 5002)
        Assert.assertEquals(4, bloqueNro)


        var bloque = service.obtenerBloque(bloqueNro)
        var newHash = service.blockHash(bloque)
        Assert.assertTrue(newHash.startsWith("0000"))

    }

    @Test
    fun seAgreganBloquesPreviosALaCadenaInvalida() {
        generateGenesisBlock()
        generateBlockNro1()
        generateBlockNro2()
        generateBlockNro3()
    }

    @Test
    fun seAgregaBloqueALaCadenaInvalida() {
        var bloqueNro = service.agregarBloque(4, "2020-05-12 15:30:31.000", mutableListOf(), 5002)

        Assert.assertEquals(0, bloqueNro)

        var bloque = service.obtenerBloque(3)
        var newHash = service.blockHash(bloque)
        Assert.assertFalse(newHash.startsWith("0000"))
    }

    @After
    fun dropAll() {
        //service.deleteAll()
    }

    private fun generateGenesisBlock() {
        service.agregarBloque(0, "2020-05-12 15:30:31.000", mutableListOf(), 48472)
    }
    private fun generateBlockNro1() {
        var transacciones: MutableList<Transaccion> = mutableListOf()
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletMinero", "walletPartyUno", 150))
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletMinero", "walletPartyDos", 150))
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletMinero", "walletPartyTres", 200))
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletMinero", "walletPartyCuatro", 200))

        service.agregarBloque(1, "2020-05-12 15:35:55.000", transacciones, 79757)
    }
    private fun generateBlockNro2() {
        var transacciones: MutableList<Transaccion> = mutableListOf()
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletPartyUno", "walletPartyDos", 2))
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletPartyUno", "walletPartyTres", 5))
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletPartyCuatro", "walletPartyTres", 1))
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletPartyCuatro", "walletPartyUno", 10))

        service.agregarBloque(2, "2020-05-12 15:35:55.000", transacciones, 3871)
    }
    private fun generateBlockNro3() {
        var transacciones: MutableList<Transaccion> = mutableListOf()
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletPartyDos", "walletPartyTres", 10))
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletPartyCuatro", "walletPartyTres", 5))
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletPartyUno", "walletPartyTres", 10))
        transacciones.add(Transaccion("2020-05-12 15:30:31.000", "walletPartyCuatro", "walletPartyUno", 7))

        service.agregarBloque(3, "2020-05-12 15:35:55.000", transacciones, 17770)
    }
}