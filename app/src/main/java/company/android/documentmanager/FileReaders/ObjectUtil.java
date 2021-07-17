package company.android.documentmanager.FileReaders;

import java.util.List;
import java.util.Map;

public class ObjectUtil {
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isAnyNull(Object obj, Object obj2) {
        return isNull(obj) || isNull(obj2);
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if ((obj instanceof String) && ((String) obj).trim().length() == 0) {
            return true;
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        if (obj instanceof List) {
            return ((List) obj).isEmpty();
        }
        if (!(obj instanceof Object[]) || ((Object[]) obj).length != 0) {
            return false;
        }
        return true;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isAnyEmpty(Object... objArr) {
        for (Object isEmpty : objArr) {
            if (isEmpty(isEmpty)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllEmpty(Object obj, Object obj2) {
        return isEmpty(obj) && isEmpty(obj2);
    }

    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static String toString(Object obj, String str) {
        return obj == null ? str : obj.toString();
    }
}
