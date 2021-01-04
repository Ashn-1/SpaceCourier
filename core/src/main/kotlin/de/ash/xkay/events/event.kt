package de.ash.xkay.events

import ashutils.ktx.ashLogger
import com.badlogic.gdx.utils.ObjectMap
import ktx.collections.GdxSet
import ktx.log.debug
import ktx.log.error
import kotlin.reflect.KClass

/**
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */

private val logger = ashLogger("GameEvents")

sealed class GameEvent {
    object ExampleEvent : GameEvent() {
        var someVariable = 0f

        override fun toString() = "ExampleEvent (someVariable = $someVariable)"
    }
}

interface GameEventListener {
    fun onEvent(event: GameEvent)
}

class GameEventManager {
    private val listeners = ObjectMap<KClass<out GameEvent>, GdxSet<GameEventListener>>()

    fun addListener(type: KClass<out GameEvent>, listener: GameEventListener) {
        var eventListeners = listeners[type]
        if (eventListeners == null) {
            eventListeners = GdxSet()
            listeners.put(type, eventListeners)
        }

        if (eventListeners.add(listener)) {
            logger.debug { "Adding listener of type $type: $listener" }
        } else {
            logger.error { "Trying to add already existing listener of type $type: $listener" }
        }
    }

    fun removeListener(type: KClass<out GameEvent>, listener: GameEventListener) {
        val eventListeners = listeners[type]
        when {
            eventListeners == null -> {
                logger.error { "Trying to remove listener $listener from non-existing listeners of type $type" }
            }
            listener !in eventListeners -> {
                logger.error { "Trying to remove non-existing listener of type $type: $listener" }
            }
            else -> {
                logger.debug { "Removing listener of type $type: $listener" }
            }
        }
    }

    fun removeListener(listener: GameEventListener) {
        logger.debug { "Removing $listener from all types" }
        listeners.values().forEach { it.remove(listener) }
    }

    fun dispatchEvent(event: GameEvent) {
        logger.debug { "Dispatch event $event" }
        listeners[event::class]?.forEach { it.onEvent(event) }
    }
}
