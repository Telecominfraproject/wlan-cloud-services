package com.telecominfraproject.wlan.customer.models.events;

import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.systemevent.models.CustomerEventWithPayload;

/**
 * @author dtop
 *
 */
public class CustomerChangedEvent extends CustomerEventWithPayload<Customer> {
    private static final long serialVersionUID = 7142209997917559985L;

    public CustomerChangedEvent(Customer customer){
        super(customer.getId(), customer);
    }
    
    /**
     * Constructor used by JSON
     */
    public CustomerChangedEvent() {
        super(0, 0, null);
    }
    
}
