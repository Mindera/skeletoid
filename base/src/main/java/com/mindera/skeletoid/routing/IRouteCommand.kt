package com.mindera.skeletoid.routing

/**
 * Route command pattern interface which encapsulate all information needed to navigate to different screens, new Activities or go back on navigation.
 */
interface IRouteCommand {
    fun navigate()
}