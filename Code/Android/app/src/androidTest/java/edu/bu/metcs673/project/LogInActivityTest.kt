package edu.bu.metcs673.project

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class LogInActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    //Tests app launch
    @Test
    fun appLaunchesSuccessfully() {
        ActivityScenario.launch(LogInActivity::class.java)
    }

    //Tests display of googleBtn button
    @Test
    fun onLaunchGoogleSignInDisplayed(){

        ActivityScenario.launch(LogInActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.googleBtn))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    //Test display of logo
    @Test
    fun onLaunchLogoDisplayed(){
        ActivityScenario.launch(LogInActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.imageView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}