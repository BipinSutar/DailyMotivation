package bipin.me.dailymotivation.data;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

/**
 * Created by BipinSutar on 15/03/17.
 */

@SimpleSQLConfig(
        name = "QuotesProvider",
        authority = "bipin.me.dailymotivation",
        database = "quotes.db",
        version = 1
)

public class QuotesProviderConfig implements ProviderConfig {

    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }
}
