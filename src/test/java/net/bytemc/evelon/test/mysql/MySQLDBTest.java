package net.bytemc.evelon.test.mysql;

import net.bytemc.evelon.DatabaseProtocol;
import net.bytemc.evelon.Debugger;
import net.bytemc.evelon.Evelon;
import net.bytemc.evelon.test.DefaultTest;
import net.bytemc.evelon.test.TestRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public final class MySQLDBTest {

    @Test
    @Disabled
    public void test() {
        Debugger.setEnable(true);
        Evelon.setCradinates(DatabaseProtocol.MYSQL, "localhost", "", "root", "polo", 3306);
        DefaultTest.REPOSITORY.query().create(DefaultTest.TEST_REPO);
    }

}
