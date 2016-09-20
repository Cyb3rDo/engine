package jadx.tests.integration.trycatch;

import jadx.core.dex.nodes.ClassNode;
import jadx.tests.api.IntegrationTest;
import org.junit.Test;

import static jadx.tests.api.utils.JadxMatchers.containsOne;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TestTryCatch8 extends IntegrationTest {

    @Test
    public void test() {
        ClassNode cls = getClassNode(TestCls.class);
        String code = cls.getCode().toString();

        assertThat(code, containsOne("synchronized (this) {"));
        assertThat(code, containsOne("throw new MyException();"));
        assertThat(code, containsOne("} catch (MyException e) {"));
        assertThat(code, containsOne("this.e = e;"));
        assertThat(code, containsOne("} catch (Exception x) {"));
        assertThat(code, containsOne("this.e = new MyException(\"MyExc\", x);"));
    }

    public static class TestCls {
        MyException e = null;

        public void test() {
            synchronized (this) {
                try {
                    throw new MyException();
                } catch (MyException e) {
                    this.e = e;
                } catch (Exception x) {
                    this.e = new MyException("MyExc", x);
                }
            }
        }

        public void check() {
            test();
            assertThat(e, notNullValue());
            assertThat(e, isA(MyException.class));
            assertThat(e.getMessage(), nullValue());
        }

        static class MyException extends Exception {
            private static final long serialVersionUID = 7963400419047287279L;

            MyException() {
            }

            MyException(String msg, Throwable cause) {
                super(msg, cause);
            }
        }
    }
}