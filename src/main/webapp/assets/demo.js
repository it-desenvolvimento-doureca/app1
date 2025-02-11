/**
 * Module for displaying "Waiting for..." dialog using Bootstrap
 *
 * @author Eugene Maslovich <ehpc@em42.ru>
 */

jQuery.ajaxSetup({ cache: false });

//guardar ip nos cookies
function getUserIP(onNewIP) { //  onNewIp - your listener function for new IPs
	//compatibility for firefox and chrome
	var myPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;
	var pc = new myPeerConnection({
		iceServers: []
	}),
		noop = function () { },
		localIPs = {},
		ipRegex = /([0-9]{1,3}(\.[0-9]{1,3}){3}|[a-f0-9]{1,4}(:[a-f0-9]{1,4}){7})/g,
		key;

	function iterateIP(ip) {
		if (!localIPs[ip]) onNewIP(ip);
		localIPs[ip] = true;
	}

	//create a bogus data channel
	var uaString = window.navigator.userAgent;
	var match = /\b(MSIE |Trident.*?rv:|Edge\/)(\d+)/.exec(uaString);

	if (match) // If Internet Explorer, return version number
	{
		$('#browseridmens').show()
	}
	else  // If another browser, return 0
	{
		$('#browseridmens').hide()
		pc.createDataChannel("");
	}


	// create offer and set local description
	pc.createOffer(function (sdp) {
		sdp.sdp.split('\n').forEach(function (line) {
			if (line.indexOf('candidate') < 0) return;
			line.match(ipRegex).forEach(iterateIP);
		});

		pc.setLocalDescription(sdp, noop, noop);
	}, noop);

	//listen for candidate events
	pc.onicecandidate = function (ice) {
		if (!ice || !ice.candidate || !ice.candidate.candidate || !ice.candidate.candidate.match(ipRegex)) return;
		ice.candidate.candidate.match(ipRegex).forEach(iterateIP);
	};
}

$('.scrollup').click(function () {
	$(".ui-datatable-scrollable-body").animate({ scrollTop: '-=100' });
	return false;
});
$('.scrolldown').click(function () {
	$(".ui-datatable-scrollable-body").animate({ scrollTop: '+=100' });
	return false;
});

$('body').on('scroll', function (e) {
	scroll(true);
});

$(window).bind("scroll", function () {
	scroll(false);
});


function scroll(rightt) {
	if ($(".tabelacontrolo thead ").length) {
		if ($('#tabelacontroloclone').length == 0) {
			var target = $('.tabelacontrolo .p-datatable-scrollable-header ');
			var target_children = target.children();

			var clone = target.clone().prop('id', 'tabelacontroloclone');

			$('.tabelacontrolo .p-datatable-scrollable-body').append(clone);
			$('#tabelacontroloclone').hide();
			$('#tabelacontroloclone .p-sortable-column-icon').remove();

		}

		var tableOffset = $(".tabelacontrolo thead ").offset().top;


		//this.console.log("a")
		var offset = $(this).scrollTop();

		if (offset >= tableOffset && $('#tabelacontroloclone').is(":hidden")) {
			if (rightt) {
				$('#tabelacontroloclone').css("right", "17px");
			}
			$('#tabelacontroloclone').show();
		}
		else if (offset < tableOffset) {
			$('#tabelacontroloclone').hide();
		}
	}
}




$(document).ready(function () {
	var count = 0;
	$(".tabcss .ui-tabview-nav").children().each(function () {
		count = count + $(this).outerHeight(true);
	});
	var panel = $('.tabcss .ui-tabview-nav').height();
	if (panel != 0) {
		$('.tabcss .ui-widget-content').css({ 'min-height': panel + 'px' });
	} else {
		if (count != 0) {
			$('.tabcss .ui-widget-content').css({ 'min-height': count + 'px' })
		} else {
			$('.tabcss .ui-widget-content').css({ 'min-height': '150px' })
		}
	}

	$('.tabcss .ui-tabview-nav').click(function () {

		var count1 = 0;
		$(".tabcss .ui-tabview-nav").children().each(function () {
			count1 = count1 + $(this).outerHeight(true);
		});

		var panel = $('.tabcss .ui-tabview-nav').height();

		if (panel != 0 && panel > count1) {
			$('.tabcss .ui-widget-content').css({ 'min-height': panel + 'px' });
		} else {
			if (count1 != 0) {
				$('.tabcss .ui-widget-content').css({ 'min-height': count1 + 'px' })
			} else {
				$('.tabcss .ui-widget-content').css({ 'min-height': '150px' })
			}
		}

	});

	$('#carregaaltura').click(function () {

		var count1 = 0;
		$(".tabcss .ui-tabview-nav").children().each(function () {
			count1 = count1 + $(this).outerHeight(true);
		});
		if (count1 == 0) {
			$('.tabcss .ui-widget-content').css({ 'min-height': '100px' })
		}
		var panel = $('.tabcss .ui-tabview-nav').height();

		if (panel != 0 && panel > count1) {
			$('.tabcss .ui-widget-content').css({ 'min-height': panel + 'px' });
		} else {
			if (count1 != 0) {
				$('.tabcss .ui-widget-content').css({ 'min-height': count1 + 'px' })
			} else {
				$('.tabcss .ui-widget-content').css({ 'min-height': '100px' })
			}
		}

	});


});



// Usage
delete_cookie('IP_CLIENT_RTC');
function delete_cookie(name) {
	document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

getUserIP(function (ip) {
	document.cookie = "IP_CLIENT_RTC=" + ip;
});