package de.ashenlab.spacecourier.events

/**
 * Implemented by classes that want to observe certain events.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
interface GameEventListener {

    /**
     * Called when an [gameEvent] is dispatched that the listener subclass registered for.
     */
    fun onEvent(gameEvent: GameEvent)
}