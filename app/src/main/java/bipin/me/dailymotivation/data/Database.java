package bipin.me.dailymotivation.data;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by BipinSutar on 15/03/17.
 */

@SimpleSQLTable(
        table = "Quotes",
        provider = "QuotesProvider"
)

public class Database {

    @SimpleSQLColumn(value = TableStructure.COLUMN_ID, primary = true, autoincrement = true)
    public int id;

    @SimpleSQLColumn(value = TableStructure.COLUMN_AUTHOR)
    public String authorName;

    @SimpleSQLColumn(value = TableStructure.COLUMN_QUOTE)
    public String quote;

}
