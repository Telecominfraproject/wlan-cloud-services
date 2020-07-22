package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.ArrayList;
import java.util.List;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;

public class CEGWClientBlocklistChangeNotification extends EquipmentCommand {


	private static final long serialVersionUID = 4401284478686864193L;
	
	private List<MacAddress> blockList = new ArrayList<>();

	/**
     * Constructor
     * 
     * @param inventoryId
     * @param equipmentId
     */
    public CEGWClientBlocklistChangeNotification(String inventoryId, long equipmentId, List<MacAddress> blockList) {
        super(CEGWCommandType.ClientBlocklistChangeNotification, inventoryId, equipmentId);
        this.blockList.addAll(blockList);
    }

    /**
     * Constructor used by JSON
     */
    protected CEGWClientBlocklistChangeNotification() {
        super(CEGWCommandType.ClientBlocklistChangeNotification, null, 0);
    }

	public List<MacAddress> getBlockList() {
		return blockList;
	}

	public void setBlockList(List<MacAddress> blockList) {
		this.blockList = blockList;
	}
}
