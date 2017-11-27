package resilience.factories;

import model.api.ContractSyncManager;
import model.api.ItemSyncManager;
import model.api.MessageSyncManager;
import model.entity.User;
import resilience.api.Save;
import resilience.impl.ContractsDecorator;
import resilience.impl.DatedHeader;
import resilience.impl.ItemsDecorator;
import resilience.impl.MessagesDecorator;

public class SaveFactory {
	public Save createDatedHeaderContractsItemsMessages(User u, ContractSyncManager contracts, ItemSyncManager items, MessageSyncManager messages) {
		return new MessagesDecorator(new ItemsDecorator(new ContractsDecorator(new DatedHeader(), u, contracts), u, items), u, messages);
	}
	
	
}
