package org.easyit.demo.boot;

import com.google.common.collect.Lists;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.easyit.demo.api.CutPoint;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

public class ByteBuddyStarterTest {

    @Test
    public void multiNameMatcher() {
        ByteBuddyStarter byteBuddyStarter = new ByteBuddyStarter();
        CutPoint.CutPointImpl cutPoint = new CutPoint.CutPointImpl();
        cutPoint.setClassName(Father.class.getName());
        cutPoint.setEnhanceType("abstract");
        ElementMatcher<TypeDescription> matcher = byteBuddyStarter.multiNameMatcher(Lists.newArrayList(cutPoint));

        MatcherAssert.assertThat(matcher.matches(TypeDescription.ForLoadedType.of(Son.class)), CoreMatchers.is(true));
        MatcherAssert.assertThat(matcher.matches(TypeDescription.ForLoadedType.of(Father.class)), CoreMatchers.is(true));
        MatcherAssert.assertThat(matcher.matches(TypeDescription.ForLoadedType.of(A.class)), CoreMatchers.is(false));
    }

    @Test
    public void multiNameMatcher_() {
        ByteBuddyStarter byteBuddyStarter = new ByteBuddyStarter();
        CutPoint.CutPointImpl cutPoint1 = new CutPoint.CutPointImpl();
        cutPoint1.setClassName(Father.class.getName());
        cutPoint1.setEnhanceType("abstract");

        CutPoint.CutPointImpl cutPoint2 = new CutPoint.CutPointImpl();
        cutPoint2.setClassName(A.class.getName());

        ElementMatcher<TypeDescription> matcher = byteBuddyStarter.multiNameMatcher(Lists.newArrayList(cutPoint1, cutPoint2));

        MatcherAssert.assertThat(matcher.matches(TypeDescription.ForLoadedType.of(Son.class)), CoreMatchers.is(true));
        MatcherAssert.assertThat(matcher.matches(TypeDescription.ForLoadedType.of(Father.class)), CoreMatchers.is(true));
        MatcherAssert.assertThat(matcher.matches(TypeDescription.ForLoadedType.of(A.class)), CoreMatchers.is(true));
    }
}