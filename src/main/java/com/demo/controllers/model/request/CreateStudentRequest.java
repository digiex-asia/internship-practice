package com.demo.controllers.model.request;

import com.demo.common.enums.Gender;
import com.demo.common.utilities.Constant;
import com.demo.common.utilities.FormatDateJsonDeserializer;
import com.demo.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class CreateStudentRequest {

    @Size(max = 64, message = ParamError.MAX_LENGTH)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String firstName;

    @Size(max = 64, message = ParamError.MAX_LENGTH)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String lastName;

    @Size(max = 250, message = ParamError.MAX_LENGTH)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String email;

    private long bob;

    private String address;

    @NotNull(message = ParamError.FIELD_NAME)
    private Gender gender;

    @Size(max = 12, message = ParamError.MAX_VALUE)
    @Size(min = 8, message = ParamError.MIN_VALUE)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String phoneNumber;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String classId;

    @NotEmpty(message = ParamError.FIELD_NAME)
    @Valid List<CreateSubjectRequest> subjects;

}
