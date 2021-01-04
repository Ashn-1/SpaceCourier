package de.ash.xkay.events

/**
 * Implemented by classes that want to observe certain events.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
interface EventListener {

    /**
     * Called when an [event] is dispatched that the listener subclass registered for.
     */
    fun onEvent(event: AshEvent)
}