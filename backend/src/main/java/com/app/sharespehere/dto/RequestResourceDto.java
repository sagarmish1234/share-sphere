package com.app.sharespehere.dto;

import com.app.sharespehere.model.RequestStatus;
import lombok.Builder;

import java.util.Date;

@Builder
public record RequestResourceDto(Long resourceId, Long accountId, Date borrowDate, Date returnDate, RequestStatus status) {
}
