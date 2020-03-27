package com.telecominfraproject.wlan.customer.models.events;

import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtop
 *
 */
public class CustomerRemovedEvent extends CustomerEvent<Customer> {
    private static final long serialVersionUID = 7142208488887559985L;

    public CustomerRemovedEvent(Customer customer){
        super(customer.getId(), customer.getLastModifiedTimestamp(), customer);
    }
    
    /**
     * Constructor used by JSON
     */
    public CustomerRemovedEvent() {
        super(0, 0, null);
    }
    
}
