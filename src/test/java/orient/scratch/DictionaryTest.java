package orient.scratch;

import com.orientechnologies.orient.core.command.script.OCommandScript;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePoolFactory;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.record.impl.ODocument;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class DictionaryTest
{
  private static final String dbUrl = "remote:localhost/security";

  private static final String dbUser = "root";

  private static final String dbPass = "admin";

  private static final String key = "foobar";

  private OPartitionedDatabasePool dbPool;

  @Before
  public void setUp() throws Exception {
    dbPool = new OPartitionedDatabasePoolFactory().get(dbUrl, dbUser, dbPass);
    try(ODatabaseDocumentTx db = dbPool.acquire()) {
      db.getDictionary().remove(key);
      db.command(new OCommandScript("delete vertex v")).execute();
    }
  }

  @Test
  public void test() throws Exception {
    try(ODatabaseDocumentTx db = dbPool.acquire()) {
      db.begin();

      ODocument doc = new ODocument("V");

      db.getDictionary().put(key, doc);

      db.commit();
    }

    try(ODatabaseDocumentTx db = dbPool.acquire()) {
      ORecord record = db.getDictionary().get(key);

      assertNotNull(record);
    }
  }

}
