function update()
{
    document.OrderSummaryForm.action = document.getElementById('updateURL');
    document.OrderSummaryForm.submit();
}

var pageSubmitted = false;

function submitOrder()
{
    if(pageSubmitted == false)
    {
        pageSubmitted = true;
        document.OrderSummaryForm.action = document.getElementById('submitOrderURL');
        document.OrderSummaryForm.submit();
    }
}

function showCVVHelp(){
 Ext.Ajax.request({
              url :   document.getElementById('GetCVVNumberHelp').href,
              method: 'GET',
            success: function ( response, request ) {
            var theDiv = document.getElementById("cvvHelpDiv");
            theDiv.innerHTML = response.responseText;
        return true;
        },
        failure: function ( response, request ) {
            alert('Failed: ' + response.responseText);
            return false;
        }
       });
       DialogPanel.show('cvvHelpDialog');

}

Ext.onReady(function() {
    var initialFocus = document.getElementById("submitOrder1");
    if(initialFocus != null)
    {
        initialFocus.focus();
    }
});
