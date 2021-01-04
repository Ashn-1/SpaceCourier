package de.ash.xkay.events

import ashutils.ktx.ashLogger
import com.badlogic.gdx.utils.ObjectMap
import ktx.collections.GdxSet
import ktx.log.debug
import ktx.log.error
import kotlin.reflect.KClass

/**
 * This class handles the listeners and dispatched events. [EventListener] can register/de-register themselves here and events are also dispatched here.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class EventManager {

    private val logger = ashLogger("EventManager")

    /**
     * Map from an event type to all listeners that registered for that type.
     */
    private val listeners = ObjectMap<KClass<out AshEvent>, GdxSet<EventListener>>()

    /**
     * Register the given [listener] to the specified [eventType]. If an event of [eventType] is dispatched the registered [listener] will be called.
     */
    fun register(eventType: KClass<out AshEvent>, listener: EventListener) {
        var eventListeners = listeners[eventType]
        if (eventListeners == null) {
            eventListeners = GdxSet()
            listeners.put(eventType, eventListeners)
        }

        if (eventListeners.add(listener)) {
            logger.debug { "Adding listener of type $eventType: $listener" }
        } else {
            logger.error { "Trying to add already existing listener of type $eventType: $listener" }
        }
    }

    /**
     * Unregister the [listener] from the given [eventType].
     */
    fun unregister(eventType: KClass<out AshEvent>, listener: EventListener) {
        val eventListeners = listeners[eventType]
        when {
            eventListeners == null -> {
                logger.error { "Trying to remove listener $listener from non-existing listeners of type $eventType" }
            }
            listener !in eventListeners -> {
                logger.error { "Trying to remove non-existing listener of type $eventType: $listener" }
            }
            else -> {
                logger.debug { "Removing listener of type $eventType: $listener" }
            }
        }
    }

    /**
     * Unregister the [listener] from all available event types.
     */
    fun unregister(listener: EventListener) {
        logger.debug { "Removing $listener from all types" }
        listeners.values().forEach { it.remove(listener) }
    }

    /**
     * Dispatches the given [event]. All listeners that are registered to this event type will be called.
     */
    fun dispatchEvent(event: AshEvent) {
        logger.debug { "Dispatch event $event" }
        listeners[event::class]?.forEach { it.onEvent(event) }
    }
}