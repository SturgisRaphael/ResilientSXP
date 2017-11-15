package crypt;

import java.math.BigInteger;
import java.util.Date;

import javax.ws.rs.GET; // REST-related dependencies
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import crypt.api.hashs.Hasher; // module to test dependencies
import crypt.factories.HasherFactory;
import model.entity.ContractEntity;
import model.entity.ElGamalSignEntity;
import model.entity.Item;
import model.entity.User;
import model.syncManager.ContractSyncManagerImpl;
import model.syncManager.ItemSyncManagerImpl;
import model.syncManager.MessageSyncManagerImpl;
import resilience.factories.SimpleSaveFactory;
import rest.api.ServletPath;
import rest.factories.RestServerFactory;

@ServletPath("/command/hash/*") // url path. PREFIX WITH COMMAND/ !!!
@Path("/")
public class CryptCommander {
  @GET
  @Path("/{input}") // a way to name the pieces of the query
  public String hash(@PathParam("input") String input) { // this argument will be initialized with
    // the piece of the query
    Hasher hasher = HasherFactory.createDefaultHasher();
    return new String(hasher.getHash(input.getBytes()));
  }

  public static void main(String[] args) {
	  SimpleSaveFactory save = new SimpleSaveFactory("/home/raphael/git/ResilientSXP/fichierTest.txt");
	  
	  User u = new User();
	  u.setCreatedAt(new Date());
	  u.setId("001");
	  u.setNick("username");
	  u.setPasswordHash(null);
	  
	  ItemSyncManagerImpl ism = new ItemSyncManagerImpl();
	  
	  ism.begin();
	  
	  ElGamalSignEntity signature = new ElGamalSignEntity();
	  signature.setR(new BigInteger("10000"));
	  signature.setS(new BigInteger("10001"));
	  
	  Item i = new Item();
	  i.setCreatedAt(new Date());
	  i.setDescription("blibloublou");
	  i.setTitle("blipblip");
	  i.setPbkey(new BigInteger("101"));
	  i.setUsername("username");
	  i.setUserid("001");
	  i.setSignature(signature);
	  
	 
	  
	  ism.persist(i);
	  
	  save.writeSave(u, new ContractSyncManagerImpl(), ism, new MessageSyncManagerImpl());
	  
	  SimpleSaveFactory newSave = new SimpleSaveFactory("/home/raphael/git/ResilientSXP/fichierTest.txt");
	  
	  newSave.readSave();
	  
	  System.out.println("newSave.getHeader().getU() = " + newSave.getHeader().getU());
	  
	  ism.close();
	  RestServerFactory.createAndStartRestServer("jetty", 8080, "crypt");
  }
}
