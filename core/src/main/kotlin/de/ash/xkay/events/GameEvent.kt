package de.ash.xkay.events

/**
 * Contains all the events available in the game as objects.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
sealed class GameEvent {

    object PlayerDeathEvent : GameEvent() {
        var score: Int = 0

        override fun toString() = "PlayerDeathEvent (highscore = $score)"
    }

    object ScoreChangedEvent : GameEvent() {
        var score: Int = 0

        override fun toString() = "ScoreChangedEvent (score = $score"
    }
}
