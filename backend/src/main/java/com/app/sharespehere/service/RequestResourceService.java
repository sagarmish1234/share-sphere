package com.app.sharespehere.service;

import com.app.sharespehere.dto.RequestResourceDetailDto;
import com.app.sharespehere.exception.ResourceNotFoundException;
import com.app.sharespehere.exception.UpdateDeniedException;
import com.app.sharespehere.mapper.ModelMapper;
import com.app.sharespehere.model.Account;
import com.app.sharespehere.model.RequestResource;
import com.app.sharespehere.model.RequestStatus;
import com.app.sharespehere.model.Resource;
import com.app.sharespehere.repository.RequestResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RequestResourceService {


    @Autowired
    private RequestResourceRepository requestResourceRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ResourceService resourceService;


    public void saveRequest(RequestResource requestResource) {


        requestResourceRepository.save(requestResource);
    }

    public void createResource(Long resourceId, OAuth2User principal, Integer borrowDays) {
        Account borrower = accountService.getAccount(principal);
        Resource resource = resourceService.getResourceById(resourceId);

        RequestResource requestResource = RequestResource.builder()
                .resource(resource)
                .borrower(borrower)
                .owner(resource.getAccount())
                .borrowDays(borrowDays)
                .status(RequestStatus.PENDING)
                .build();

        this.saveRequest(requestResource);

    }


    public RequestResource getRequestById(Long id) {
        return requestResourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Request %s not found", id)));
    }

    public void approveRequest(Long requestId, OAuth2User principal) {
        RequestResource request = this.validateRequest(requestId, principal);
        request.setStatus(RequestStatus.APPROVED);
        request.setBorrowDate(new Date());
        this.saveRequest(request);
    }

    public void rejectRequest(Long requestId, OAuth2User principal) {
        RequestResource request = this.validateRequest(requestId, principal);
        if (request.getStatus() == RequestStatus.RETURNED)
            throw new UpdateDeniedException("The request must not be in RETURNED state to perform this operation");
        request.setBorrowDate(null);
        request.setReturnDate(null);
        request.setStatus(RequestStatus.REJECTED);
        this.saveRequest(request);
    }

    public void returnRequest(Long requestId, OAuth2User principal) {
        RequestResource request = this.validateRequest(requestId, principal);
        if (!(request.getStatus() == RequestStatus.APPROVED))
            throw new UpdateDeniedException("The request must be in the approved state to perform this operation");
        request.setStatus(RequestStatus.RETURNED);
        request.setReturnDate(new Date());
        this.saveRequest(request);
    }

    public List<RequestResourceDetailDto> getOwnerRequests(OAuth2User principal) {
        Account owner = accountService.getAccount(principal);
        List<RequestResource> requestList = requestResourceRepository.findByOwner(owner);
        return requestList.stream().map(requestResource -> RequestResourceDetailDto.builder()
                .resourceDto(ModelMapper.maptoResourceDto(requestResource.getResource(), resourceService))
                .requestResourceDto(ModelMapper.mapToRequestResourceDto(requestResource))
                .build()).toList();
    }

    public List<RequestResourceDetailDto> getBorrowerRequests(OAuth2User principal) {
        Account borrower = accountService.getAccount(principal);
        List<RequestResource> requestList = requestResourceRepository.findByBorrower(borrower);
        return requestList.stream().map(requestResource -> RequestResourceDetailDto.builder()
                .resourceDto(ModelMapper.maptoResourceDto(requestResource.getResource(), resourceService))
                .requestResourceDto(ModelMapper.mapToRequestResourceDto(requestResource))
                .build()).toList();
    }


    private RequestResource validateRequest(Long requestId, OAuth2User principal) {
        String email = principal.getAttribute("email");
        RequestResource request = this.getRequestById(requestId);
        if (!request.getOwner().getEmail().equals(email))
            throw new UpdateDeniedException(String.format("User not the owner of the request %s", requestId));
        return request;
    }


}
