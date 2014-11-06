/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.access.AccessHandler
 * Created on 04.01.2008 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.dao.impl.mongo;

import com.mongodb.MongoClient;
import de.xwic.appkit.core.DefaultDAOFactory;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.impl.Right;
import de.xwic.appkit.core.security.impl.Role;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Test for ReflectionHelper.
 *
 * @author Vitaliy Zhovtyuk
 */
@Ignore("Runs on running mongo instance. Integration test.")
public class MongoDAOProviderAPITest {
    private static MongoDAOProvider mongoDAOProvider;
    private MongoClient mongoClient;

    @BeforeClass
    public static void setupClass() throws UnknownHostException {
        Setup setup = new Setup();
        setup.setProperty("mongo.host", "localhost");
        setup.setProperty("mongo.db", "test");
        setup.setProperty("mongo.user", "pulsecli");
        setup.setProperty("mongo.password", "querty");
        ConfigurationManager.setSetup(setup);
        mongoDAOProvider = new MongoDAOProvider();
        DAOSystem.setDAOFactory(new DefaultDAOFactory(mongoDAOProvider));
    }

    @Before
    public void setup() throws UnknownHostException {
        mongoClient = MongoUtil.currentSession();
        mongoClient.getDB("test").dropDatabase();
    }

    @Test
    public void shouldSaveRestoreEntityById() throws UnknownHostException {
        // given
        IRole role = new Role();
        role.setName("roleName");
        IRight right = new Right(role, null, null);
        right.setId(new Random().nextInt());
        // when
        MongoDAOProviderAPI mongoDAOProviderAPI = new MongoDAOProviderAPI(new MongoDAOProvider(), mongoClient);
        mongoDAOProviderAPI.update(right);

        // then
        IRight retrievedRight = (IRight) mongoDAOProviderAPI.getEntity(IRight.class, right.getId());
        assertNotNull("Property evaluated is not null", retrievedRight);
        assertEquals("Property evaluated ", "roleName", retrievedRight.getRole().getName());
    }


    @Test
    public void shouldSaveGenerateId() throws UnknownHostException {
        // given
        IRole role = new Role();
        role.setName("roleName");
        IRight right = new Right(role, null, null);
        // when
        MongoDAOProviderAPI mongoDAOProviderAPI = new MongoDAOProviderAPI(new MongoDAOProvider(), mongoClient);
        mongoDAOProviderAPI.update(right);

        // then
        IRight retrievedRight = (IRight) mongoDAOProviderAPI.getEntity(IRight.class, right.getId());
        assertNotNull("Property evaluated is not null", retrievedRight);
        assertEquals("Property evaluated ", "roleName", retrievedRight.getRole().getName());
    }

    @Test
    public void shouldSaveDeleteEntityById() throws UnknownHostException {
        // given
        IRole role = new Role();
        role.setName("roleName");
        IRight right = new Right(role, null, null);
        right.setId(new Random().nextInt());
        // when

        MongoDAOProviderAPI mongoDAOProviderAPI = new MongoDAOProviderAPI(new MongoDAOProvider(), mongoClient);
        mongoDAOProviderAPI.update(right);

        // then
        mongoDAOProviderAPI.delete(right);
        IRight retrievedRight = (IRight) mongoDAOProviderAPI.getEntity(IRight.class, right.getId());
        assertNull("Property evaluated is null", retrievedRight);
    }

    @Test
    public void shouldGetEntityList() throws UnknownHostException {
        // given
        IRole role = new Role();
        role.setName("roleName");
        IRight right = new Right(role, null, null);
        right.setId(new Random().nextInt());
        PropertyQuery propertyQuery = new PropertyQuery();
        propertyQuery.addEquals("id", right.getId());
        MongoDAOProviderAPI mongoDAOProviderAPI = new MongoDAOProviderAPI(mongoDAOProvider, mongoClient);

        // when
        mongoDAOProviderAPI.update(right);

        // then
        EntityList<IRight> retrievedRights = mongoDAOProviderAPI.getEntities(IRight.class, new Limit(0, 10), propertyQuery);
        assertNotNull("Property evaluated is not null", retrievedRights);
        assertTrue("Property evaluated is not null", !retrievedRights.isEmpty());
        IRight retrievedRight = retrievedRights.get(0);
        assertNotNull("Property evaluated is not null", retrievedRight);
        assertEquals("Property evaluated ", "roleName", retrievedRight.getRole().getName());
    }


    @Test
    public void shouldGetEntityListColumns() throws UnknownHostException {
        // given
        IRole role = new Role();
        role.setName("roleName");
        IRight right = new Right(role, null, null);
        right.setId(new Random().nextInt());
        PropertyQuery propertyQuery = new PropertyQuery();
        List<String> cols = new ArrayList<String>();
        cols.add("role.name");
        propertyQuery.setColumns(cols);
        propertyQuery.addEquals("id", right.getId());

        // when
        MongoDAOProviderAPI mongoDAOProviderAPI = new MongoDAOProviderAPI(mongoDAOProvider, mongoClient);
        mongoDAOProviderAPI.update(right);

        // then
        EntityList<IRight> retrievedRights = mongoDAOProviderAPI.getEntities(IRight.class, new Limit(0, 10), propertyQuery);
        assertNotNull("Property evaluated is not null", retrievedRights);
        assertTrue("Property evaluated is not null", !retrievedRights.isEmpty());
        IRight retrievedRight = retrievedRights.get(0);
        assertNotNull("Property evaluated is not null", retrievedRight);
        assertEquals("Property evaluated ", "roleName", retrievedRight.getRole().getName());
    }
}
