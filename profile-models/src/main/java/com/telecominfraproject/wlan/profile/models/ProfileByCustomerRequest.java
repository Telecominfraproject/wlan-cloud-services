package com.telecominfraproject.wlan.profile.models;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestParam;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;

public class ProfileByCustomerRequest {

	private final int customerId;
	private final Optional<ProfileType> profileType;
	private final Optional<String> nameSubstring;
	private final Optional<List<ColumnAndSort>> sortBy;
	private final PaginationContext<Profile> paginationContext;

	public ProfileByCustomerRequest(int customerId,
			ProfileType profileType,
			String nameSubstring,
            List<ColumnAndSort> sortBy,
            PaginationContext<Profile> paginationContext)
	{
		this.customerId = Objects.requireNonNull(customerId);
		this.profileType = Optional.ofNullable(profileType);
		this.nameSubstring = Optional.ofNullable(nameSubstring);
		this.sortBy = Optional.ofNullable(sortBy);
		this.paginationContext = Objects.requireNonNull(paginationContext);
	}

	public int getCustomerId() {
		return customerId;
	}

	public Optional<ProfileType> getProfileType() {
		return profileType;
	}

	public Optional<String> getNameSubstring() {
		return nameSubstring;
	}

	public Optional<List<ColumnAndSort>> getSortBy() {
		return sortBy;
	}

	public PaginationContext<Profile> getPaginationContext() {
		return paginationContext;
	}
}