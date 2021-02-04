package com.telecominfraproject.wlan.customer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;


/**
 * @author dtoptygin
 *
 */
public class Customer extends BaseJsonModel implements HasCustomerId {
    private static final long serialVersionUID = 3061657893135029599L;

    private int id;

    private String name;
    private String email;

    private CustomerDetails details;
    
    private long createdTimestamp;
    private long lastModifiedTimestamp;

    public Customer()
    {
    }

    public Customer(Customer customer) {
        this();
        this.id = customer.id;
        this.name = customer.name;
        this.email = customer.email;
        this.details = customer.details;
        this.createdTimestamp = customer.createdTimestamp;
        this.lastModifiedTimestamp = customer.lastModifiedTimestamp;
    }

    @Override
    public Customer clone() {
        Customer ret = (Customer) super.clone();
        if(details!=null) {
        	ret.details = details.clone();
        }
        
        return ret;
    }

    //WARNING: do not use any mutable fields in equals/hashCode
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Customer other = (Customer) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
    	return id;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(long createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public long getLastModifiedTimestamp() {
		return lastModifiedTimestamp;
	}

	public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
		this.lastModifiedTimestamp = lastModifiedTimestamp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public CustomerDetails getDetails() {
		return details;
	}

	public void setDetails(CustomerDetails details) {
		this.details = details;
	}

	@Override
	@JsonIgnore
	public int getCustomerId() {
	    return id;
	}
}
