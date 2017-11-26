package resilience.impl;

import java.util.ArrayList;
import java.util.Collection;

import controller.Users;
import controller.tools.JsonTools;
import model.entity.Item;
import model.entity.User;
import network.api.Peer;
import network.factories.PeerFactory;

import com.fasterxml.jackson.core.type.TypeReference;

public class ItemSave {
	final private JsonTools<Collection<User>> json = new JsonTools<>(new TypeReference<Collection<User>>(){});
	
	public void save_item(Item i, int peerID){ //called for each item
	    ArrayList<Peer> peers = find_random_peers(); //select 5 random peers  who published "connected" Advertisement.
	    for(Peer p : peers){
	        send_item(i, peerID);//send item marked with PeerID in a Resilient Item Service.
	    }
	}

	private void send_item(Item i, int peerID) {
		
	}

	public ArrayList<Peer> find_random_peers() {
		ArrayList<User> userList = new ArrayList<User>(json.toEntity((new Users()).get()));
		ArrayList<Peer> result = new ArrayList<Peer>();
		PeerFactory peerFactory = new PeerFactory();
		
		for(User u : userList)
		{
			String[] rdvPeerIds = new String[1];
			rdvPeerIds[0] = u.getId();
			result.add(peerFactory.createDefaultAndStartPeerForTest(9801, rdvPeerIds));
		}
		return result;
	}
}
