package util;

import com.oracle.tools.packager.Log;
import org.junit.Test;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by goznauk on 15. 10. 21..
 */
public class RegisterTest {
    static String raw = "/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

    @Test
    public void register() {
        Map<String, String> map = HttpRequestUtils.parseQueryString(raw);
        map.forEach((s, s2) -> System.out.println(s + "," + s2));
    }
}
