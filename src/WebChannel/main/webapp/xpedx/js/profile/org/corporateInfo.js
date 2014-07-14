/* This file is used for Corporate Information
 * tab of Organization Profile.
 * 
 */



<!-- Corporate Section Script Starts -->

/**
 * Method called on click of Add Child button in Corporate Information page in Org profile
 *       
 * 
 */

function getNewCorporateInfo(url){
    
   form = document.getElementById("customerCorporateInfohierarchy");
   form.elements["#action.name"].value = "";
   document.customerCorporateInfohierarchy.action = url;
   document.customerCorporateInfohierarchy.submit();
}

<!-- Corporate Section Script ENDS -->