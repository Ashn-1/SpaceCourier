package de.ash.xkay.events

/**
 * Contains all the events available in the game as objects.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
sealed class AshEvent {

    object ExampleEvent : AshEvent() {
        var someVariable = 0f

        override fun toString() = "ExampleEvent (someVariable = $someVariable)"
    }
}
