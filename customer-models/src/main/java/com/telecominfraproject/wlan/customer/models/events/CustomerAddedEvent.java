package com.telecominfraproject.wlan.customer.models.events;

import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.systemevent.models.CustomerEventWithPayload;

/**
 * @author dtop
 *
 */
public class CustomerAddedEvent extends CustomerEventWithPayload<Customer> {
    private static final long serialVersionUID = 7142208487917559985L;

    public CustomerAddedEvent(Customer customer){
        super(customer.getId(), customer.getLastModifiedTimestamp(), customer);
    }
    
    /**
     * Constructor used by JSON
     */
    public CustomerAddedEvent() {
        super(0, 0, null);
    }
    
}
