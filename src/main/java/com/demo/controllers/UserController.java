package com.demo.controllers;

import com.demo.common.enums.*;
import com.demo.common.utilities.RestAPIResponse;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.Validator;
import com.demo.config.security.AuthorizeValidator;
import com.demo.controllers.helper.SessionHelper;
import com.demo.controllers.helper.UserHelper;
import com.demo.controllers.model.request.*;
import com.demo.controllers.model.response.*;

import com.demo.entities.User;
import com.demo.services.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * @author DigiEx Group
 */
@RestController
@RequestMapping(ApiPath.USER_API)
@Slf4j
public class UserController extends AbstractBaseController {
    final UserService userService;
    final UserHelper userHelper;
    final SessionHelper sessionHelper;
    final SessionService sessionService;

    public UserController(UserService userService,
                          UserHelper userHelper,
                          SessionHelper sessionHelper,
                          SessionService sessionService) {
        this.userService = userService;
        this.userHelper = userHelper;
        this.sessionHelper = sessionHelper;
        this.sessionService = sessionService;
    }

    @AuthorizeValidator({UserRole.ADMIN, UserRole.ADMIN_MEMBER})
    @PostMapping()
    public ResponseEntity<RestAPIResponse> createMember(
            @RequestBody @Valid CreateMemberAdmin memberAdmin
    ) {
        Validator.validateEmail(memberAdmin.getEmail().trim());
        User user = userService.getByEmailAndStatus(memberAdmin.getEmail().trim(), AppStatus.ACTIVE);
        Validator.mustNull(user, RestAPIStatus.EXISTED, "User existed");
        User newMember = userHelper.createMemberAdmin(memberAdmin);
        userService.save(newMember);

        return responseUtil.successResponse(new UserResponse(newMember));
    }

    @AuthorizeValidator({UserRole.ADMIN, UserRole.ADMIN_MEMBER})
    @GetMapping(path = ApiPath.ID)
    public ResponseEntity<RestAPIResponse> getDetail(
            @PathVariable(name = "id") String id
    ) {
        User user = userService.getByIdAndNotINACTIVE(id);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, "User Not Found");
        return responseUtil.successResponse(new UserResponse(user));
    }

    @AuthorizeValidator({UserRole.ADMIN, UserRole.ADMIN_MEMBER})
    @GetMapping()
    public ResponseEntity<RestAPIResponse> getPages(
            @RequestParam(name = "asc_sort", required = false, defaultValue = "false") boolean ascSort,
            @RequestParam(name = "sort_field", required = false, defaultValue = "") String sortField,
            @RequestParam(name = "search_key", required = false, defaultValue = "") String searchKey,
            @RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize
    ) {

        validatePageSize(pageNumber, pageSize);
        Page<UserResponse> userResponses = userService.getPageMember(searchKey, sortField, ascSort, pageNumber, pageSize);
        return responseUtil.successResponse(new PagingResponse(userResponses));
    }

    @AuthorizeValidator(UserRole.ADMIN)
    @DeleteMapping(path = ApiPath.ID)
    public ResponseEntity<RestAPIResponse> deleteMember(
            @PathVariable(name = "id") String id
    ) {
        User user = userService.getByIdAndNotINACTIVE(id);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, "User Not Found");
        user.setStatus(AppStatus.INACTIVE);
        userService.save(user);
        return responseUtil.successResponse("Delete OK");
    }

    @AuthorizeValidator(UserRole.ADMIN)
    @PutMapping(path = ApiPath.ID)
    public ResponseEntity<RestAPIResponse> updateMember(
            @PathVariable(name = "id") String id,
            @RequestBody @Valid UpdateMemberRequest updateMemberRequest
    ) {
        User user = userService.getByIdAndNotINACTIVE(id);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, "User Not Found");

        if (updateMemberRequest.getFirstName() != null && !updateMemberRequest.getFirstName().trim().isEmpty()) {
            user.setFirstName(updateMemberRequest.getFirstName().trim());
        }
        if (updateMemberRequest.getLastName() != null && !updateMemberRequest.getLastName().trim().isEmpty()) {
            user.setLastName(updateMemberRequest.getLastName().trim());
        }
        userService.save(user);
        return responseUtil.successResponse(new UserResponse(user));
    }


}
