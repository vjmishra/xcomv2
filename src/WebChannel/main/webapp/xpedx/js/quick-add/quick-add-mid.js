$(document).ready(function(){
    var options = { 
        //target:        '.quick-add-to-cart-form tbody',   // target element(s) to be updated with server response 
        //beforeSubmit:  showRequest,  // pre-submit callback 
        success:       showResponse,  // post-submit callback
        url: 'check-for-item.ajax-mid.php',
        type: 'post'
 
        // other available options: 
        //url:       url         // override for form's 'action' attribute 
        //type:      type        // 'get' or 'post', override for form's 'method' attribute 
        //dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
        //clearForm: true        // clear all form fields after successful submit 
        //resetForm: true        // reset the form after successful submit 
 
        // $.ajax options can be used here too, for example: 
        //timeout:   3000 
    }; 
 
    // bind form using 'ajaxForm' 
    $('#quick-add-selector').ajaxForm(options);
})
var initDelAction = true;
// post-submit callback 
function showResponse(responseText, statusText, xhr, $form)  { 
    // for normal html responses, the first argument to the success callback 
    // is the XMLHttpRequest object's responseText property 
 
    // if the ajaxForm method was passed an Options Object with the dataType 
    // property set to 'xml' then the first argument to the success callback 
    // is the XMLHttpRequest object's responseXML property 
 
    // if the ajaxForm method was passed an Options Object with the dataType 
    // property set to 'json' then the first argument to the success callback 
    // is the json data object returned by the server 
 
    //alert('status: ' + statusText + '\n\nresponseText: \n' + responseText + 
    //    '\n\nThe output div should have already been updated with the responseText.');
    $('.quick-add-to-cart-form tbody').append(responseText);
    $('.quick-add-to-cart-form').slideDown('slow');
    $('.del-quick-add').last().click(function() {
        var containedrow = $(this).parent().parent();
        if ($('.quick-add-to-cart-form tbody').children('tr').length==1) {
            $('.quick-add-to-cart-form').slideUp('slow', function() {
                initDelAction = true;
                containedrow.remove();
            });
        } else {
            containedrow.remove();
        }
        return false;
    });
}
