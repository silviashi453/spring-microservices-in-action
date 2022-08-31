package com.app.licensingservice.controller;

import com.app.licensingservice.model.License;
import com.app.licensingservice.service.LicenseService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value="v1/organization/{organizationId}/license")
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @RequestMapping(value="/{licenseId}", method = RequestMethod.GET)
    public ResponseEntity<License> getLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId
    ) {
        License license = licenseService
                .getLicense(licenseId, organizationId);

            license.add(linkTo(methodOn(LicenseController.class)
                            .getLicense(organizationId, license.getLicenseId()))
                            .withSelfRel(),
                    linkTo(methodOn(LicenseController.class)
                            .createLicense(organizationId, license))
                            .withRel("createLicense"),
                    linkTo(methodOn(LicenseController.class)
                            .deleteLicense(organizationId, licenseId))
                            .withRel("deleteLicense"),
                    linkTo(methodOn(LicenseController.class)
                            .updateLicense(organizationId, license))
                            .withRel("updateLicense"));
        return ResponseEntity.ok(license);
    }

    @PutMapping
    public ResponseEntity<String> updateLicense(
            @PathVariable("organizationId")
            String organizationId,
            @RequestBody License request
    ) {
        return ResponseEntity.ok(licenseService.updateLicense(request,
                organizationId));
    }

    @PostMapping
    public ResponseEntity<String> createLicense(
            @PathVariable("organizationId") String organizationId,
            @RequestBody License license
    ) {
        return ResponseEntity.ok(licenseService.createLicense(license, organizationId));
    }

    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId
    ) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId,
                organizationId));
    }

}
