var focusDelay = 100;
    function toggleVisibility(divID)
    {
        var divElement = document.getElementById(divID);
        if(divElement.style.display != "none")
        {
            divElement.style.display = "none";
        }
        else
        {
            divElement.style.display = "inline";
        }
    }


    function toggleDisplay(element)
    {
        if(element.isDisplayed())
        {
            element.setDisplayed("none");
        }
        else
        {
            element.setDisplayed("");
        }
    }

    function minorLineExpandCollapse2(majorLineKey)
    {
        var xcont = Ext.get("spacer" + majorLineKey);
        toggleDisplay(xcont);

        for(var index=0; ; index++)
        {
            xcont = Ext.get("ml" + majorLineKey + "-" + index);
            if(xcont == null)
            {
                break;
            }

            toggleDisplay(xcont);
        }

        svg_classhandlers_decoratePage();
    }
    function minorLineExpandCollapse(majorLineKey, showImg, hideImg)
    {
        var xlnk = Ext.get("arrow" + majorLineKey);
        if(xlnk.hasClass("selected"))
        {
            xlnk.removeClass("selected");
            var inner = '<span class="showDetails"><img src="'+showImg+'" width="10" height="10" /></span>';
            xlnk.update(inner);
        }
        else
        {
            xlnk.addClass("selected");
            var inner = '<span class="hideDetails"><img src="'+hideImg+'" width="10" height="10" /></span>';
            xlnk.update(inner);
        }

        var xcont = Ext.get("spacer" + majorLineKey);
        toggleDisplay(xcont);

        for(var index=0; ; index++)
        {
            xcont = Ext.get("ml" + majorLineKey + "-" + index);
            if(xcont == null)
            {
                break;
            }

            toggleDisplay(xcont);
        }

        svg_classhandlers_decoratePage();
    }
    function orderAgain(action) {
        document.postOrderForm.action = action;
        document.postOrderForm.submit();
    }
     function editOrder(action) {
     //commented for jira 2442
        //document.postOrderForm.action = action;
        //document.postOrderForm.submit();
        window.location.href=action;

    }

     function refreshOrder(action) {
        document.postOrderForm.action = action;
        document.postOrderForm.submit();
    }
    function cancelOrder(){
      DialogPanel.show("cancelDialog");
      var focusElement = Ext.get('Confirm_Yes');
      if(focusElement != null){
        focusElement.focus(focusDelay);
      }
    }

    Ext.onReady(function() {
        Ext.select(".showDetailsControl").on('click', function(e, t){
            var el = Ext.get(t);
            if (!el.hasClass("showDetailsControl")) {
                el = el.parent(".showDetailsControl");
            }
            minorLineExpandCollapse2(el.select(".hidden-data").first().dom.innerHTML);
            el.toggleClass("hidden");
            el.toggleClass("shown");
        });
    });
