package com.vms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkflowFormV2Dto {
    @JsonProperty("formId")
    private Long formId;

    @JsonProperty("revisionNo")
    private Integer revisionNo;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("authorized_account")
    private Long authorizedAccountId;
}

