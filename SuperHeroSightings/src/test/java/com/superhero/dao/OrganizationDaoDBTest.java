package com.superhero.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.superhero.dao.OrganizationDao;
import com.superhero.dao.HeroDao;
import com.superhero.model.Organisation;
import com.superhero.model.Hero;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganizationDaoDBTest {
    
    //Autowire in the DAO classes:    
    @Autowired
    OrganizationDao organizationDao;    
                
    @Autowired
    HeroDao superDao;
    
    
    public OrganizationDaoDBTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        List<Organisation> organizations = organizationDao.getAllOrganizations();
        for(Organisation organization : organizations) {
            organizationDao.deleteOrganizationById(organization.getId());
        }        
        
        List<Hero> supers = superDao.getAllSupers();
        for(Hero hero_villain : supers) {
            superDao.deleteSuperById(hero_villain.getId());
        }
    }
    
    @After
    public void tearDown() {
    }

    
    /**     --------------------------- TESTS ---------------------------.     */
    /**
     * Test of addAndGetOrganization method, of class OrganizationDaoDB.
     */
    @Test
    public void testAddAndGetOrganization() {
        Organisation organization = new Organisation();
        organization.setName("Test Name");
        organization.setDescription("Test Description");
        organization.setAddress("Test Address");
        organization.setContact("Test Contact");
        organization = organizationDao.addOrganization(organization);
        
        Organisation fromDao = organizationDao.getOrganizationById(organization.getId());
        
        assertEquals(organization, fromDao);
    }

    /**
     * Test of getAllOrganizations method, of class OrganizationDaoDB.
     */
    @Test
    public void testGetAllOrganizations() {
        Organisation organization1 = new Organisation();
        organization1.setName("Test Name");
        organization1.setDescription("Test Description");
        organization1.setAddress("Test Address");
        organization1.setContact("Test Contact");
        organization1 = organizationDao.addOrganization(organization1);
        
        Organisation organization2 = new Organisation();
        organization2.setName("Test Name");
        organization2.setDescription("Test Description");
        organization2.setAddress("Test Address");
        organization2.setContact("Test Contact");
        organization2 = organizationDao.addOrganization(organization2);
        
        List<Organisation> organizations = organizationDao.getAllOrganizations();
        
        assertEquals(2, organizations.size());
        assertTrue(organizations.contains(organization1));
        assertTrue(organizations.contains(organization2));
    }

    /**
     * Test of updateOrganization method, of class OrganizationDaoDB.
     */
    @Test
    public void testUpdateOrganization() {
        Organisation organization = new Organisation();
        organization.setName("Test Name");
        organization.setDescription("Test Description");
        organization.setAddress("Test Address");
        organization.setContact("Test Contact");
        organization = organizationDao.addOrganization(organization);
        
        Organisation fromDao = organizationDao.getOrganizationById(organization.getId());
        assertEquals(organization, fromDao);
        
        organization.setName("New Name");
        organizationDao.updateOrganization(organization);
        
        assertNotEquals(organization, fromDao);
        
        fromDao = organizationDao.getOrganizationById(organization.getId());
        
        assertEquals(organization, fromDao);
    }

    /**
     * Test of deleteOrganizationById method, of class OrganizationDaoDB.
     */
    @Test
    public void testDeleteOrganizationById() {
        Organisation organization = new Organisation(); //Create organization
        organization.setName("Test Name");
        organization.setDescription("Test Description");
        organization.setAddress("Test Address");
        organization.setContact("Test Contact");
        organization = organizationDao.addOrganization(organization);
        List<Organisation> organizations = new ArrayList<>();
        organizations.add(organization);

        Hero heroVillain = new Hero(); //Create a super
        heroVillain.setName("Test Super Name");
        heroVillain.setDescription("Test Super Description");        
        heroVillain.setOrganizations(organizations); //add organizations list
        superDao.addSuper(heroVillain); //save super 
        
        Hero savedSuper = superDao.getSuperById(heroVillain.getId());
        
        assertEquals(savedSuper.getOrganizations().get(0).getId(), organization.getId());
        
        Organisation organizationFromDao = organizationDao.getOrganizationById(organization.getId()); //get the saved organization from db
        assertEquals(organization, organizationFromDao); //Assert that the organization created is the same as the organization get from the db
        
        organizationDao.deleteOrganizationById(organization.getId());
        
        organizationFromDao = organizationDao.getOrganizationById(organization.getId());
        assertNull(organizationFromDao); //Assert that the organization was deleted
        
        Hero superWithoutOrganization = superDao.getSuperById(heroVillain.getId());
        
        assertEquals(superWithoutOrganization.getOrganizations().size(), 0); //Assert that this super doesn't have an organization        
    }
    
} 
