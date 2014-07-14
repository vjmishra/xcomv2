/* This file is sepcific to any methods related to User List page.
 * 
 */

/**
 * @author vbandhu
 * 
 */

/**
 * Method called on click of Add User button in User List page in User profile
 *       
 * 
 */

function getNewContactInfo(url){

   document.userList.action = url;
   document.userList.submit();
}

