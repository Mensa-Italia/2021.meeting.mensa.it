package swaix.dev.mensaeventi

import timber.log.Timber


abstract class TestSuite {
    // create driver
    // create logger

//   private fun executeTest(steps: () -> Unit) {
//        // DO SOMETHING
//        steps.invoke()
//        // DO SOMETHING ELSE
//    }

    abstract fun getTests(): List<Test>

    init {
        getTests().forEach {
//            executeTest {
            it.executeSteps()
//            }
        }
    }

}

abstract class Test(private val steps: () -> Unit) {
    fun executeSteps() {
        // DO SOMETHING
        Timber.d("prima dei test")
        steps.invoke()
        Timber.d("dopo dei test")
        // DO SOMETHING ELSE
    }
}

class T1 : Test({
    Timber.d("corpo t1")
    // definire gli steps
})

class T2 : Test({
    Timber.d("corpo t2")
    // definire gli steps
})

class TextExample : TestSuite() {
    override fun getTests(): List<Test> = listOf(
        T1(),
        T2(),
    )
}



