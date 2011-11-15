
function sliderApprovalWidget(elementId, imgElementId, showImgClass, hideImgClass, isDisplayed){
    // define slide operation
    var slideText = function(elementId, imgElementId, showImgClass, hideImgClass){
        var slideMeElement = Ext.get(elementId);
        var imgElement = Ext.get(imgElementId);
        if(slideMeElement){
            var upArrow=Ext.get(showImgClass);
            var downArrow=Ext.get(hideImgClass);
            if (slideMeElement.isVisible()){
                slideMeElement.slideOut('t', {
                    easing: 'easeOut',
                    remove: false,
                    useDisplay: true,
                    callback: function(){
                        svg_classhandlers_decoratePage();
                        imgElement.removeClass(showImgClass);
                        imgElement.addClass(hideImgClass);
                    }
                });
            }
            else {
                slideMeElement.slideIn('t', {
                    easing: 'easeIn',
                    callback: function(){
                        svg_classhandlers_decoratePage()
                        imgElement.removeClass(hideImgClass);
                        imgElement.addClass(showImgClass);
                    }
                });
            }
        }
    }

    // set up initial style according to the state of the content element
    var imgElement = Ext.get(imgElementId);
    if (isDisplayed==undefined) {
        isDisplayed = upArrow.isDisplayed();
    }
    if (isDisplayed) {
        imgElement.removeClass(hideImgClass);
        imgElement.addClass(showImgClass);
    }
    else {
        imgElement.removeClass(showImgClass);
        imgElement.addClass(hideImgClass);
    }

    // Set up event handler
    Ext.get(imgElementId).on('click',function(e,t){
        slideText(elementId, imgElementId, showImgClass, hideImgClass);
    });
}

Ext.onReady(function() {
    sliderApprovalWidget('approvalListWidget','toggleApprovalList','sliderIn','sliderOut', true);
});




