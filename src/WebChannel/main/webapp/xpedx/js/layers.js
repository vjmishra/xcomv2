/************** LAYERS **************/
$(".showHelp").click(function(){ $("div#helpLayer").slideDown('fast'); $(this).addClass("selectedHover"); });
$(".hideHelp").click(function(){ $("div#helpLayer").slideUp('fast'); $('.showHelp').removeClass("selectedHover"); });

$(".showAddress").click(function(){ $("div#address").slideDown('fast');});
$(".hideAddress").click(function(){ $("div#address").slideUp('fast');});

$(".showGiftCard").click(function(){ $("div#giftCard").slideDown('fast');});
$(".hideGiftCard").click(function(){ $("div#giftCard").slideUp('fast');});

$(".showQuickAdd").click(function(){ $("div#quickAdd").slideDown('fast');});
$(".hideQuickAdd").click(function(){ $("div#quickAdd").slideUp('fast');});