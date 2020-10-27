package com.telecominfraproject.wlan.profile.models;

import java.util.List;

import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;

@Configuration
public class ProfileByCustomerRequestFactory {

	public ProfileByCustomerRequest create(int customerId, List<ColumnAndSort> sortBy, PaginationContext<Profile> paginationContext)
	{
		return create(customerId, null, sortBy, (paginationContext == null ? new PaginationContext<>() : paginationContext));
	}
	
	public ProfileByCustomerRequest create(int customerId, ProfileType profileType, List<ColumnAndSort> sortBy, PaginationContext<Profile> paginationContext)
	{
		return new ProfileByCustomerRequest(customerId, profileType, sortBy, (paginationContext == null) ? new PaginationContext<>() : paginationContext);
	}
}