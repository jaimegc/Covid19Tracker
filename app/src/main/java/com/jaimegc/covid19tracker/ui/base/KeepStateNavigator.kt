package com.jaimegc.covid19tracker.ui.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.jaimegc.covid19tracker.R

/**
 *  This class is to keep state in fragment navigator. Show & Hide fragments instead of attach / detach
 *  Also, we manually handle title and back pressed
 *  Adapted from
 *  https://github.com/STAR-ZERO/navigation-keep-fragment-sample/
 */
@Navigator.Name("keep_state_fragment")
class KeepStateNavigator(
    private val context: Context,
    private val manager: FragmentManager,
    private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    private lateinit var destination: Destination
    private var onBackPressed = false

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        this.destination = destination

        val tag =
            if (onBackPressed) {
                onBackPressed = false
                R.id.navigation_country.toString()
            } else {
                destination.id.toString()
            }

        val transaction = manager.beginTransaction()

        var initialNavigate = false
        val currentFragment = manager.primaryNavigationFragment
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        } else {
            initialNavigate = true
        }

        var fragment = manager.findFragmentByTag(tag)
        if (fragment == null) {
            val className = destination.className
            fragment = manager.fragmentFactory.instantiate(context.classLoader, className)
            transaction.add(containerId, fragment, tag)
        } else {
            transaction.show(fragment)
        }

        transaction.setPrimaryNavigationFragment(fragment)
        transaction.setReorderingAllowed(true)
        transaction.commit()

        (context as Activity).let { activity ->
            when (destination.id) {
                R.id.navigation_country ->
                    activity.title = context.getString(R.string.title_country)
                R.id.navigation_world ->
                    activity.title = context.getString(R.string.title_world)
            }
        }

        return if (initialNavigate) {
            destination
        } else {
            null
        }
    }

    fun onBackPressed() {
        onBackPressed = true
        navigate(destination, null, null, null)
    }
}
