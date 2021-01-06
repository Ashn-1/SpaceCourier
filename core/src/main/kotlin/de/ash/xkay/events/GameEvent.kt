package de.ash.xkay.events

/**
 * Contains all the events available in the game as objects.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
sealed class GameEvent {

    object PlayerDeathEvent : GameEvent() {
        var highscore: Long = 0

        override fun toString() = "PlayerDeathEvent (someVariable = $highscore)"
    }
}
