package de.ash.xkay.events

import ashutils.ktx.ashLogger
import com.badlogic.gdx.utils.ObjectMap
import ktx.collections.GdxSet
import ktx.log.debug
import ktx.log.error
import kotlin.reflect.KClass

/**
 * This class handles the listeners and dispatched events. [GameEventListener] can register/de-register themselves here and events are also dispatched here.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class GameEventManager {

    private val logger = ashLogger("EventManager")

    /**
     * Map from an event type to all listeners that registered for that type.
     */
    private val listeners = ObjectMap<KClass<out GameEvent>, GdxSet<GameEventListener>>()

    /**
     * Register the given [listener] to the specified [eventType]. If an event of [eventType] is dispatched the registered [listener] will be called.
     */
    fun register(eventType: KClass<out GameEvent>, listener: GameEventListener) {
        var eventListeners = listeners[eventType]
        if (eventListeners == null) {
            eventListeners = GdxSet()
            listeners.put(eventType, eventListeners)
        }

        if (eventListeners.add(listener)) {
            logger.debug { "Adding listener of type ${eventType.simpleName}: ${listener::class.simpleName}" }
        } else {
            logger.error { "Trying to add already existing listener of type ${eventType.simpleName}: ${listener::class.simpleName}" }
        }
    }

    /**
     * Unregister the [listener] from the given [eventType].
     */
    fun unregister(eventType: KClass<out GameEvent>, listener: GameEventListener) {
        val eventListeners = listeners[eventType]
        when {
            eventListeners == null -> {
                logger.error { "Trying to remove listener ${listener::class.simpleName} from non-existing listeners of type ${eventType.simpleName}" }
            }
            listener !in eventListeners -> {
                logger.error { "Trying to remove non-existing listener of type ${eventType.simpleName}: ${listener::class.simpleName}" }
            }
            else -> {
                logger.debug { "Removing listener of type ${eventType.simpleName}: ${listener::class.simpleName}" }
            }
        }
    }

    /**
     * Unregister the [listener] from all available event types.
     */
    fun unregister(listener: GameEventListener) {
        logger.debug { "Removing ${listener::class.simpleName} from all types" }
        listeners.values().forEach { it.remove(listener) }
    }

    /**
     * Dispatches the given [gameEvent]. All listeners that are registered to this event type will be called.
     */
    fun dispatchEvent(gameEvent: GameEvent) {
        logger.debug { "Dispatch event ${gameEvent::class.simpleName}" }
        listeners[gameEvent::class]?.forEach { it.onEvent(gameEvent) }
    }
}