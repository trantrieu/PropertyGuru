package trieu.propertyguru.utils.teh.utils;

import com.google.common.base.Objects;

import java.util.List;
import java.util.Map;

import trieu.propertyguru.utils.teh.fields.TEHFields;
import trieu.propertyguru.utils.teh.fields.TEHFieldsFactory;

/**
 * TEH core implementation
 * 
 */
public class TEHUtils {

    public static String toString(Object object) {
	return toString(object, TEHFieldsFactory.get());
    }

    protected static String toString(Object object, TEHFields tehFields) {
	if (tehFields.isTEHActivated(object)) {
	    Objects.ToStringHelper toStringHelper = Objects.toStringHelper(object.getClass().getName() + '@' + object.hashCode());
	    for (TEHFields.TEHFieldValue fieldValue : tehFields.extractToStringFieldValues(object)) {
		toStringHelper.add(fieldValue.fieldName, String.valueOf(fieldValue.value));
	    }
	    return toStringHelper.toString();
	}
	return String.valueOf(object);
    }

    public static boolean equals(Object object, Object other) {
	return equals(object, other, TEHFieldsFactory.get());
    }

    public static boolean equals(Object object, Object other, TEHFields tehFields) {
	if (tehFields.isTEHActivated(object) && tehFields.isTEHActivated(other)) {
	    Map<String, TEHFields.TEHFieldValue> objectEqualsFieldValues = tehFields.extractEqualsFieldValues(object);
	    if (objectEqualsFieldValues.isEmpty()) {
		return object == other;
	    }
	    Map<String, TEHFields.TEHFieldValue> otherEqualsFieldValues = tehFields.extractEqualsFieldValues(other);
	    for (TEHFields.TEHFieldValue objectFieldValue : objectEqualsFieldValues.values()) {
		TEHFields.TEHFieldValue otherFieldValue = otherEqualsFieldValues.get(objectFieldValue.fieldName);
		if (!Objects.equal(objectFieldValue.value, otherFieldValue.value)) {
		    return false;
		}
	    }
	    return true;
	}
	return Objects.equal(object, other);
    }

    /**
     * @deprecated use {@link #hashCode(Object, int)} instead with
     *             super.hashCode()
     */
    protected static int hashCode(Object object) {
	if (object == null) {
	    return 0;
	}
	return hashCode(object, 0);
    }

    public static int hashCode(Object object, int nativeHashCode) {
	return hashCode(object, nativeHashCode, TEHFieldsFactory.get());
    }

    /**
     * @since 0.4
     */
    public static int hashCode(Object object, int nativeHashCode, TEHFields tehFields) {
	if (tehFields.isTEHActivated(object)) {
	    List<Object> hashCodeValues = tehFields.extractHashCodeValues(object);
	    if (!hashCodeValues.isEmpty()) {
		return Objects.hashCode(hashCodeValues);
	    }
	}
	return nativeHashCode;
    }
}
