package com.bhasaka.fruitables.core.models;

import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model for the Billing Form component.
 *
 * <p>This model adapts from a {@link Resource} and is used to retrieve
 * all author-configured labels and placeholders for the billing form
 * in an AEM component.</p>
 *
 * <p>It enables dynamic rendering of form fields such as:
 * <ul>
 *     <li>Customer personal details (name, company)</li>
 *     <li>Address information</li>
 *     <li>Contact details (mobile, email)</li>
 *     <li>Optional actions (create account, ship to different address)</li>
 *     <li>Additional notes</li>
 * </ul>
 * </p>
 *
 * <p>Lombok's {@code @Getter} annotation is used to automatically generate
 * getter methods for all fields.</p>
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Getter
public class BillingFormModel {

    /**
     * Title of the billing section.
     */
    @ValueMapValue
    private String sectionTitle;

    /**
     * Label for the first name field.
     */
    @ValueMapValue
    private String firstNameLabel;

    /**
     * Label for the last name field.
     */
    @ValueMapValue
    private String lastNameLabel;

    /**
     * Label for the company name field.
     */
    @ValueMapValue
    private String companyNameLabel;

    /**
     * Label for the address field.
     */
    @ValueMapValue
    private String addressLabel;

    /**
     * Placeholder text for the address input field.
     */
    @ValueMapValue
    private String addressPlaceholder;

    /**
     * Label for the city field.
     */
    @ValueMapValue
    private String cityLabel;

    /**
     * Label for the country field.
     */
    @ValueMapValue
    private String countryLabel;

    /**
     * Label for the ZIP or postal code field.
     */
    @ValueMapValue
    private String zipLabel;

    /**
     * Label for the mobile number field.
     */
    @ValueMapValue
    private String mobileLabel;

    /**
     * Label for the email field.
     */
    @ValueMapValue
    private String emailLabel;

    /**
     * Label for the "Create Account" option.
     */
    @ValueMapValue
    private String createAccountLabel;

    /**
     * Label for the "Ship to a different address" option.
     */
    @ValueMapValue
    private String shipDifferentAddressLabel;

    /**
     * Placeholder text for additional notes input.
     */
    @ValueMapValue
    private String notesPlaceholder;

    /**
     * Initialization method invoked after all field injections are complete.
     *
     * <p>Currently does not contain any logic, but can be used for
     * validation or transformation of field values if required.</p>
     */
    @PostConstruct
    protected void init() {
    }
}