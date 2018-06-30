package com.acme.a3csci3130;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class businessCRUDTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.acme.a3csci3130", appContext.getPackageName());
    }

    @Test
    public void createMinimumContact() throws Exception{
        onView(allOf(withId(R.id.submitButton), withText("Create Contact"))).perform(click());
        onView(withId(R.id.name)).perform(typeTextIntoFocusedView("Smol Business Place"));
        onView(withId(R.id.businessNumber)).perform(click());
        onView(withId(R.id.businessNumber)).perform(typeTextIntoFocusedView("123456789"));
        onView(withId(R.id.primaryBusiness_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Fisher"))).perform(click());
        onView(allOf(withId(R.id.submitButton), withText("Create Business"))).perform(click());

        onData(allOf(is(instanceOf(Contact.class)), withContent("Smol Business Place"))).perform(click());
        onView(withId(R.id.det_name)).check(matches(withText("Smol Business Place")));
        //delete the entry so the test can be run subsequently
        onView(withId(R.id.deleteButton)).perform(click());
    }
    @Test
    public void createFullDetailContact() throws Exception{
        onView(allOf(withId(R.id.submitButton), withText("Create Contact"))).perform(click());
        onView(withId(R.id.name)).perform(typeTextIntoFocusedView("Big Business Place"));
        onView(withId(R.id.businessNumber)).perform(click());
        onView(withId(R.id.businessNumber)).perform(typeTextIntoFocusedView("123456789"));
        onView(withId(R.id.primaryBusiness_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Fisher"))).perform(click());
        onView(withId(R.id.address)).perform(click());
        onView(withId(R.id.address)).perform(typeTextIntoFocusedView("15 Somewhere road"));
        onView(withId(R.id.province_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(allOf(withId(R.id.submitButton), withText("Create Business"))).perform(click());

        onData(allOf(is(instanceOf(Contact.class)), withContent("Big Business Place"))).perform(click());
        onView(withId(R.id.det_name)).check(matches(withText("Big Business Place")));
        //delete the entry so the test can be run subsequently
        onView(withId(R.id.deleteButton)).perform(click());

    }
    @Test
    public void createAndUpdate() throws Exception{
        onView(allOf(withId(R.id.submitButton), withText("Create Contact"))).perform(click());
        onView(withId(R.id.name)).perform(typeTextIntoFocusedView("Another Business Place"));
        onView(withId(R.id.businessNumber)).perform(click());
        onView(withId(R.id.businessNumber)).perform(typeTextIntoFocusedView("123456789"));
        onView(withId(R.id.primaryBusiness_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Fisher"))).perform(click());
        onView(withId(R.id.address)).perform(click());
        onView(withId(R.id.address)).perform(typeTextIntoFocusedView("15 Somewhere road"));
        onView(withId(R.id.province_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(allOf(withId(R.id.submitButton), withText("Create Business"))).perform(click());

        onData(allOf(is(instanceOf(Contact.class)), withContent("Another Business Place"))).perform(click());
        onView(withId(R.id.det_name)).check(matches(withText("Another Business Place")));
        onView(withId(R.id.det_name)).perform(click());
        onView(withId(R.id.det_name)).perform(clearText());
        onView(withId(R.id.det_name)).perform(typeTextIntoFocusedView("A new name"));
        onView(withId(R.id.updateButton)).perform(click());
        //should now navigate to main
        onData(allOf(is(instanceOf(Contact.class)), withContent("A new name"))).perform(click());
        onView(withId(R.id.det_name)).check(matches(withText("A new name")));
        onView(withId(R.id.deleteButton)).perform(click());
    }
    public static Matcher<Object> withContent(final String content) {
        return new BoundedMatcher<Object, Contact>(Contact.class) {
            @Override
            public boolean matchesSafely(Contact listEntry) {
                return listEntry.name.equals(content);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with content '" + content + "'");
            }
        };
    }



}
