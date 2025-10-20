package baseNoStates;

/**
 * Possible actions that can be requested by readers or areas.
 * Using constants avoids typos across the codebase.
 */
public final class Actions {
    private Actions() {} // no instantiation

    // Reader + area requests
    public static final String LOCK            = "lock";
    public static final String UNLOCK          = "unlock";
    public static final String UNLOCK_SHORTLY  = "unlock_shortly"; // canonical form

    // Door (physical) actions
    public static final String OPEN  = "open";
    public static final String CLOSE = "close";

    /** Lowercase + trim */
    public static String normalize(String s) {
        return s == null ? null : s.trim().toLowerCase();
    }

    /** Map input variants to one canonical action string */
    public static String canonicalize(String s) {
        s = normalize(s);
        if (s == null) return null;

        // accept both "unlock shortly" and "unlock_shortly"
        if ("unlock shortly".equals(s) || "unlock_shortly".equals(s)) return UNLOCK_SHORTLY;

        // the rest match as-is
        if (LOCK.equals(s) || UNLOCK.equals(s) || OPEN.equals(s) || CLOSE.equals(s)) return s;

        return null; // unknown action
    }

    public static boolean isValid(String s) {
        return canonicalize(s) != null;
    }
}
