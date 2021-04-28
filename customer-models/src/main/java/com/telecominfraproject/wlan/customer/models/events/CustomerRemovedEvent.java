package com.telecominfraproject.wlan.customer.models.events;

import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.systemevent.models.CustomerEventWithPayload;

/**
 * @author dtop
 *
 */
public class CustomerRemovedEvent extends CustomerEventWithPayload<Customer> {
    private static final long serialVersionUID = 7142208488887559985L;

    public CustomerRemovedEvent(Customer customer){
        super(customer.getId(), customer);
    }
    
    /**
     * Constructor used by JSON
     */
    public CustomerRemovedEvent() {
        super(0, 0, null);
    }
    
}
