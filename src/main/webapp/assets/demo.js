/**
 * Module for displaying "Waiting for..." dialog using Bootstrap
 *
 * @author Eugene Maslovich <ehpc@em42.ru>
 */

jQuery.ajaxSetup({ cache: false });



/*var username = getCookie("app_producao_versao");
var version = "1.1.4";

//verifica a versão nos cookies se for igual não faz nada se for diferente atualiza a página e os cookies
if (username != null) {
	if (username != version) {
		location.reload(true);
		setCookie("app_producao_versao", version);
	}
} else {
	location.reload(true);
	setCookie("app_producao_versao", version);

}

function setCookie(cname, cvalue) {
	document.cookie = cname + "=" + cvalue + ";";
}

function getCookie(cname) {
	var name = cname + "=";
	var decodedCookie = decodeURIComponent(document.cookie);
	var ca = decodedCookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') {
			c = c.substring(1);
		}
		if (c.indexOf(name) == 0) {
			return c.substring(name.length, c.length);
		}
	}
	return null;
}
*/

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

			/*var cStyle = document.body.currentStyle || window.getComputedStyle(document.body, "");
			hasVScroll = cStyle.overflow == "visible"
				|| cStyle.overflowY == "visible"
				|| (hasVScroll && cStyle.overflow == "auto")
				|| (hasVScroll && cStyle.overflowY == "auto");
				 && hasVScroll
				*/
			if (rightt) {
				$('#tabelacontroloclone').css("right", "17px");
			}
			$('#tabelacontroloclone').show();
			/*clone.children().width(function (i, val) {
				return target_children.eq(i).width();
			});*/
		}
		else if (offset < tableOffset) {
			$('#tabelacontroloclone').hide();
		}
	}
}




/*
$('#editarclick2').click(function () {

	$dialog.find('h3').text("A Inserir Dados...")

});


var $dialog = $(
	'<div id="myModal" class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog" aria-hidden="true" style="padding-top:15%; overflow-y:visible;">' +
	'<div class="modal-dialog modal-m">' +
	'<div class="modal-content">' +
	'<div class="modal-header"><h3 id="titlemodal" style="margin:0; color: black;"></h3></div>' +
	'<div class="modal-body">' +
	'<div class="progress1 progress1-striped active" style="margin-bottom:0;"><div class="progress1-bar" style="width: 100%"></div></div>' +
	'</div>' +
	'</div></div></div>');
*/

/*
var waitingDialog = waitingDialog || (function ($) {
	'use strict';

	// Creating modal dialog's DOM


	return {
		/**
		 * Opens our dialog
		 * @param message Custom message
		 * @param options Custom options:
		 * 				  options.dialogSize - bootstrap postfix for dialog size, e.g. "sm", "m";
		 * 				  options.progressType - bootstrap postfix for progress bar type, e.g. "success", "warning".
		
		 
		show: function (message, options) {
			// Assigning defaults
			if (typeof options === 'undefined') {
				options = {};
			}
			if (typeof message === 'undefined') {
				message = 'Loading';
			}
			var settings = $.extend({
				dialogSize: 'm',
				progressType: '',
				onHide: null // This callback runs after the dialog was hidden
			}, options);

			// Configuring dialog
			$dialog.find('.modal-dialog').attr('class', 'modal-dialog').addClass('modal-' + settings.dialogSize);
			$dialog.find('.progress1-bar').attr('class', 'progress1-bar');
			if (settings.progressType) {
				$dialog.find('.progress1-bar').addClass('progress1-bar-' + settings.progressType);
			}
			$dialog.find('h3').text(message);
			// Adding callbacks
			if (typeof settings.onHide === 'function') {
				$dialog.off('hidden.bs.modal').on('hidden.bs.modal', function (e) {
					settings.onHide.call($dialog);
				});
			}
			// Opening dialog
			$dialog.modal();
		},
		/**
		 * Closes dialog
		 
		 
		hide: function () {
			if ($('#myModal').hasClass('show')) {
				$dialog.modal('hide');
			}

		}
	};

})(jQuery);*/
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

!function (a) { function b(b, d) { function e() { if (w) { $canvas = a('<canvas class="pg-canvas"></canvas>'), v.prepend($canvas), p = $canvas[0], q = p.getContext("2d"), f(); for (var b = Math.round(p.width * p.height / d.density), c = 0; b > c; c++) { var e = new l; e.setStackPos(c), x.push(e) } a(window).on("resize", function () { h() }), a(document).on("mousemove", function (a) { y = a.pageX, z = a.pageY }), B && !A && window.addEventListener("deviceorientation", function () { D = Math.min(Math.max(-event.beta, -30), 30), C = Math.min(Math.max(-event.gamma, -30), 30) }, !0), g(), o("onInit") } } function f() { p.width = v.width(), p.height = v.height(), q.fillStyle = d.dotColor, q.strokeStyle = d.lineColor, q.lineWidth = d.lineWidth } function g() { if (w) { s = a(window).width(), t = a(window).height(), q.clearRect(0, 0, p.width, p.height); for (var b = 0; b < x.length; b++)x[b].updatePosition(); for (var b = 0; b < x.length; b++)x[b].draw(); E || (r = requestAnimationFrame(g)) } } function h() { for (f(), i = x.length - 1; i >= 0; i--)(x[i].position.x > v.width() || x[i].position.y > v.height()) && x.splice(i, 1); var a = Math.round(p.width * p.height / d.density); if (a > x.length) for (; a > x.length;) { var b = new l; x.push(b) } else a < x.length && x.splice(a); for (i = x.length - 1; i >= 0; i--)x[i].setStackPos(i) } function j() { E = !0 } function k() { E = !1, g() } function l() { switch (this.stackPos, this.active = !0, this.layer = Math.ceil(3 * Math.random()), this.parallaxOffsetX = 0, this.parallaxOffsetY = 0, this.position = { x: Math.ceil(Math.random() * p.width), y: Math.ceil(Math.random() * p.height) }, this.speed = {}, d.directionX) { case "left": this.speed.x = +(-d.maxSpeedX + Math.random() * d.maxSpeedX - d.minSpeedX).toFixed(2); break; case "right": this.speed.x = +(Math.random() * d.maxSpeedX + d.minSpeedX).toFixed(2); break; default: this.speed.x = +(-d.maxSpeedX / 2 + Math.random() * d.maxSpeedX).toFixed(2), this.speed.x += this.speed.x > 0 ? d.minSpeedX : -d.minSpeedX }switch (d.directionY) { case "up": this.speed.y = +(-d.maxSpeedY + Math.random() * d.maxSpeedY - d.minSpeedY).toFixed(2); break; case "down": this.speed.y = +(Math.random() * d.maxSpeedY + d.minSpeedY).toFixed(2); break; default: this.speed.y = +(-d.maxSpeedY / 2 + Math.random() * d.maxSpeedY).toFixed(2), this.speed.x += this.speed.y > 0 ? d.minSpeedY : -d.minSpeedY } } function m(a, b) { return b ? void (d[a] = b) : d[a] } function n() { v.find(".pg-canvas").remove(), o("onDestroy"), v.removeData("plugin_" + c) } function o(a) { void 0 !== d[a] && d[a].call(u) } var p, q, r, s, t, u = b, v = a(b), w = !!document.createElement("canvas").getContext, x = [], y = 0, z = 0, A = !navigator.userAgent.match(/(iPhone|iPod|iPad|Android|BlackBerry|BB10|mobi|tablet|opera mini|nexus 7)/i), B = !!window.DeviceOrientationEvent, C = 0, D = 0, E = !1; return d = a.extend({}, a.fn[c].defaults, d), l.prototype.draw = function () { q.beginPath(), q.arc(this.position.x + this.parallaxOffsetX, this.position.y + this.parallaxOffsetY, d.particleRadius / 2, 0, 2 * Math.PI, !0), q.closePath(), q.fill(), q.beginPath(); for (var a = x.length - 1; a > this.stackPos; a--) { var b = x[a], c = this.position.x - b.position.x, e = this.position.y - b.position.y, f = Math.sqrt(c * c + e * e).toFixed(2); f < d.proximity && (q.moveTo(this.position.x + this.parallaxOffsetX, this.position.y + this.parallaxOffsetY), d.curvedLines ? q.quadraticCurveTo(Math.max(b.position.x, b.position.x), Math.min(b.position.y, b.position.y), b.position.x + b.parallaxOffsetX, b.position.y + b.parallaxOffsetY) : q.lineTo(b.position.x + b.parallaxOffsetX, b.position.y + b.parallaxOffsetY)) } q.stroke(), q.closePath() }, l.prototype.updatePosition = function () { if (d.parallax) { if (B && !A) { var a = (s - 0) / 60; pointerX = (C - -30) * a + 0; var b = (t - 0) / 60; pointerY = (D - -30) * b + 0 } else pointerX = y, pointerY = z; this.parallaxTargX = (pointerX - s / 2) / (d.parallaxMultiplier * this.layer), this.parallaxOffsetX += (this.parallaxTargX - this.parallaxOffsetX) / 10, this.parallaxTargY = (pointerY - t / 2) / (d.parallaxMultiplier * this.layer), this.parallaxOffsetY += (this.parallaxTargY - this.parallaxOffsetY) / 10 } switch (d.directionX) { case "left": this.position.x + this.speed.x + this.parallaxOffsetX < 0 && (this.position.x = v.width() - this.parallaxOffsetX); break; case "right": this.position.x + this.speed.x + this.parallaxOffsetX > v.width() && (this.position.x = 0 - this.parallaxOffsetX); break; default: (this.position.x + this.speed.x + this.parallaxOffsetX > v.width() || this.position.x + this.speed.x + this.parallaxOffsetX < 0) && (this.speed.x = -this.speed.x) }switch (d.directionY) { case "up": this.position.y + this.speed.y + this.parallaxOffsetY < 0 && (this.position.y = v.height() - this.parallaxOffsetY); break; case "down": this.position.y + this.speed.y + this.parallaxOffsetY > v.height() && (this.position.y = 0 - this.parallaxOffsetY); break; default: (this.position.y + this.speed.y + this.parallaxOffsetY > v.height() || this.position.y + this.speed.y + this.parallaxOffsetY < 0) && (this.speed.y = -this.speed.y) }this.position.x += this.speed.x, this.position.y += this.speed.y }, l.prototype.setStackPos = function (a) { this.stackPos = a }, e(), { option: m, destroy: n, start: k, pause: j } } var c = "particleground"; a.fn[c] = function (d) { if ("string" == typeof arguments[0]) { var e, f = arguments[0], g = Array.prototype.slice.call(arguments, 1); return this.each(function () { a.data(this, "plugin_" + c) && "function" == typeof a.data(this, "plugin_" + c)[f] && (e = a.data(this, "plugin_" + c)[f].apply(this, g)) }), void 0 !== e ? e : this } return "object" != typeof d && d ? void 0 : this.each(function () { a.data(this, "plugin_" + c) || a.data(this, "plugin_" + c, new b(this, d)) }) }, a.fn[c].defaults = { minSpeedX: .1, maxSpeedX: .7, minSpeedY: .1, maxSpeedY: .7, directionX: "center", directionY: "center", density: 1e4, dotColor: "#666666", lineColor: "#666666", particleRadius: 7, lineWidth: 1, curvedLines: !1, proximity: 100, parallax: !0, parallaxMultiplier: 5, onInit: function () { }, onDestroy: function () { } } }(jQuery),/**
* requestAnimationFrame polyfill by Erik Möller. fixes from Paul Irish and Tino Zijdel
* @see: http://paulirish.com/2011/requestanimationframe-for-smart-animating/
* @see: http://my.opera.com/emoller/blog/2011/12/20/requestanimationframe-for-smart-er-animating
* @license: MIT license
*/

	$(document).ready(function () {
		$(function () {
			if ($("#particles").length > 0) {
				$('#particles').particleground({
					minSpeedX: 0.1,
					maxSpeedX: 0.7,
					minSpeedY: 0.1,
					maxSpeedY: 0.7,
					directionX: 'center', // 'center', 'left' or 'right'. 'center' = dots bounce off edges
					directionY: 'center', // 'center', 'up' or 'down'. 'center' = dots bounce off edges
					density: 10000, // How many particles will be generated: one particle every n pixels
					dotColor: '#6eacff',
					lineColor: '#6eacff',
					particleRadius: 7, // Dot size
					lineWidth: 1,
					curvedLines: true,
					proximity: 100, // How close two dots need to be before they join
					parallax: false
				});
			}
		});
	});






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

// Usage
delete_cookie('IP_CLIENT_RTC');
function delete_cookie(name) {
	document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

getUserIP(function (ip) {
	document.cookie = "IP_CLIENT_RTC=" + ip;
});