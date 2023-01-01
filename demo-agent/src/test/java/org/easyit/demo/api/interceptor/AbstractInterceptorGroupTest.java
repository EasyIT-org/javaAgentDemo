package org.easyit.demo.api.interceptor;

import com.google.common.collect.Lists;
import org.easyit.demo.api.CutPoint;
import org.easyit.demo.api.model.Parameters;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AbstractInterceptorGroupTest {

    private TestInterceptor interceptor1 = new TestInterceptor(1, "1");
    private TestInterceptor interceptor2 = new TestInterceptor(2, "2");

    private TestInterceptorGroup testInterceptorGroup = new TestInterceptorGroup();

    @Before
    public void before() {
        TestInterceptor.reset();
    }

    @Test
    public void testBeforeMethod() {
        testInterceptorGroup.beforeMethod(null);
        MatcherAssert.assertThat(TestInterceptor.RECORD, CoreMatchers.equalTo(Lists.newArrayList(1 + TestInterceptor.BEFORE, 2 + TestInterceptor.BEFORE)));
    }


    @Test
    public void testHandleMethodException() {
        testInterceptorGroup.handleMethodException(null);
        MatcherAssert.assertThat(TestInterceptor.RECORD, CoreMatchers.equalTo(Lists.newArrayList(1 + TestInterceptor.EXCEPTION, 2 + TestInterceptor.EXCEPTION)));
    }

    @Test
    public void testAfterMethod() {
        testInterceptorGroup.afterMethod(null);
        MatcherAssert.assertThat(TestInterceptor.RECORD, CoreMatchers.equalTo(Lists.newArrayList(2 + TestInterceptor.AFTER, 1 + TestInterceptor.AFTER)));
    }


    private class TestInterceptorGroup extends AbstractInterceptorGroup {
        @Override
        public CutPoint getCutPoint() {
            return null;
        }

        @Override
        protected List getInterceptorList(Parameters parameters) {
            return Lists.newArrayList(interceptor1, interceptor2);
        }
    }


}