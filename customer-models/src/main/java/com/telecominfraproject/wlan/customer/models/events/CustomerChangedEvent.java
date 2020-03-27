package com.telecominfraproject.wlan.customer.models.events;

import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtop
 *
 */
public class CustomerChangedEvent extends CustomerEvent<Customer> {
    private static final long serialVersionUID = 7142209997917559985L;

    public CustomerChangedEvent(Customer customer){
        super(customer.getId(), customer.getLastModifiedTimestamp(), customer);
    }
    
    /**
     * Constructor used by JSON
     */
    public CustomerChangedEvent() {
        super(0, 0, null);
    }
    
}
