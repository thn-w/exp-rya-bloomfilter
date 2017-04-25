package exp.rya.bloomfilter.utils;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.log4j.Logger;
import org.apache.rya.api.persist.RyaDAOException;
import org.apache.rya.rdftriplestore.inference.InferenceEngineException;
import org.junit.Test;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.sail.SailException;

public class UtilsTest {
    private static final Logger log = Logger.getLogger(UtilsTest.class);

    @Test
    public void test() throws RepositoryException, SailException, AccumuloException, AccumuloSecurityException, RyaDAOException, InferenceEngineException {
        final SailRepositoryConnection conn = Utils.getInMemoryAccConn(true);
        log.info("conn --> " + conn.toString());

        final ValueFactory vf = conn.getValueFactory();
        log.info("vf --> " + vf.toString());

        conn.close();
    }

}
