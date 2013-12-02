package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.util.List;

import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.xpedx.webchannel.catalog.autocomplete.AutocompleteMarketingGroup;

/*
 * Created on Oct 21, 2013
 */

/**
 * Ad hoc testing for AjaxAutocompleteAction.
 *
 * @author Trey Howard
 */
@SuppressWarnings("serial")
public class TestAjaxAutocompleteAction extends WCAction {

	private static AjaxAutocompleteAction createTsudis() {
		// tsudis ST = 60-0006806597-000003-M-XX-S
		// div = 68xpedx (ship from branch = 68_M Pittsburgh)
		// cust = 600006806597
		return new AjaxAutocompleteAction() {
			@Override
			String getBrand() {
				return "xpedx";
			}

			@Override
			String getEntitlementAnonymousBrand() {
				return null;
			}

			@Override
			String getEntitlementDivisionAndBrand() {
				return "68xpedx";
			}

			@Override
			String getEntitlementCompanyCodeAndLegacyCustomerId() {
				return "600006806597";
			}
		};
	}

	private static AjaxAutocompleteAction createLinemark() {
		// linemark ST = 90-0001018921-000001-M-XX-S
		// div = 75xpedx (ship from branch = 75_M Baltimore/DC)
		// cust = 900001018921
		// +(entitled_divisions:90xpedx entitled_customers:900001018921) (+marketing_group_path_parsed:*spring*)
		return new AjaxAutocompleteAction() {
			@Override
			String getBrand() {
				return "xpedx";
			}

			@Override
			String getEntitlementAnonymousBrand() {
				return null;
			}

			@Override
			String getEntitlementDivisionAndBrand() {
				return "75xpedx";
			}

			@Override
			String getEntitlementCompanyCodeAndLegacyCustomerId() {
				return "900001018921";
			}
		};
	}

	private static AjaxAutocompleteAction createEthicon() {
		// ethicon ST = 97-0001274066-000001-M-XX-S
		// div = PGxpedx (PG_M El Paso)
		// cust = 970001274066
		// +(entitled_divisions:90xpedx entitled_customers:900001018921) (+marketing_group_path_parsed:*spring*)
		return new AjaxAutocompleteAction() {
			@Override
			String getBrand() {
				return "xpedx";
			}

			@Override
			String getEntitlementAnonymousBrand() {
				return null;
			}

			@Override
			String getEntitlementDivisionAndBrand() {
				return null;
				// return "PGxpedx"; // division entitlements disabled
			}

			@Override
			String getEntitlementCompanyCodeAndLegacyCustomerId() {
				return "970001274066";
			}
		};
	}

	@SuppressWarnings("all")
	public static void main(String[] args) throws Exception {
		AjaxAutocompleteAction action = createLinemark();
		action.setSearchTerm("packaging spring");
		List<AutocompleteMarketingGroup> mgs = action.searchIndex("C:/Sterling/Foundation/marketinggroupindex");

		for (AutocompleteMarketingGroup mg : mgs) {
			System.out.println("mg:\t" + mg);
		}
	}

}
