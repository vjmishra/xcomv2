function addCoupon(actionURL)
{
	document.OrderSummaryForm.action = actionURL;
	document.OrderSummaryForm.submit();
}

function deleteCoupon(couponID,actionURL)
{
    document.OrderSummaryForm.deleteCouponID.value = couponID;
 	document.OrderSummaryForm.action = actionURL;
	document.OrderSummaryForm.submit();
}