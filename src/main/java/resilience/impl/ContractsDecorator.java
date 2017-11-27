package resilience.impl;

import java.util.Collection;

import model.api.ContractSyncManager;
import model.entity.ContractEntity;
import model.entity.User;
import model.syncManager.ContractSyncManagerImpl;
import resilience.api.Save;

public class ContractsDecorator extends SaveDecorator implements Save{
	private ContractSyncManager contracts;
	
	public ContractsDecorator(Save save, User u, ContractSyncManager contracts) {
		super(save, u);
		this.contracts = contracts;
	}

	public ContractsDecorator(Save save) {
		super(save);
		this.contracts = new ContractSyncManagerImpl();
	}

	@Override
	public String write(){
		Collection<ContractEntity> contractCollection = contracts.findAll();
		String result = new String();
		for(ContractEntity c : contractCollection)
		{
			if(c.getUserid() == u.getId()) {
				result += c.toString() + "\n";
			}
		}

		return save.write() + "\n" + "<contracts>" + "\n" + result + "</contracts>";
	}
	
	@Override
	public int read(String s) {
		return save.read(s);
	}

	public ContractSyncManager getContracts() {
		return contracts;
	}
	
	
}
