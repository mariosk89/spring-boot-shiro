package project.configuration;

/**
 * Created by mariosk89 on 19-Mar-16.
 */
public enum ShiroRealms {
    INI("INI"),
    JDBC("JDBC")
    ;

    private final String text;

    private ShiroRealms(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    /**
     * Defaults the Shiro Realm to INI
     * */
    public static ShiroRealms getRealmForString(String realmName)
    {
        ShiroRealms realm = INI;
        try
        {
            realm = ShiroRealms.valueOf(realmName);
        }
        catch (Exception e)
        {}

        return realm;
    }
}
