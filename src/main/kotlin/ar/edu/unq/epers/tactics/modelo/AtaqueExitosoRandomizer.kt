package ar.edu.unq.epers.tactics.modelo

import kotlin.random.Random

object AtaqueExitosoRandomizer {

    var testMode: Boolean = false
    var fixedValue: Int = 1

    fun getValue(): Int {
        if (testMode) return fixedValue
        else return Random.nextInt(1, 20)
    }

    fun setTestMode(fixVal : Int) {
        testMode = true
        fixedValue = fixVal
    }
    fun setRuntimeMode() {
        testMode = false
    }
}