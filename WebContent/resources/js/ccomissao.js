$(function(){
	var decimal = $('.js-decimal');
	decimal.maskMoney({decimal: ",", thousands: ".", allowZero: true, allowNegative: true});
	
	var decimal2 = $("#vlr-dialog");
	decimal2.maskMoney({decimal2: ",", thousands: ".", allowZero: true, allowNegative: true});
	
	var plain = $('.js-plain');
	plain.maskMoney({precision: 0});
	
	PrimeFaces.locales['pt_BR'] = {
			closeText : 'Fechar',
			prevText : 'Anterior',
			nextText : 'Próximo',
			currentText : 'Começo',
			monthNames : [ 'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio',
					'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro',
					'Dezembro' ],
			monthNamesShort : [ 'Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul',
					'Ago', 'Set', 'Out', 'Nov', 'Dez' ],
			dayNames : [ 'Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta',
					'Sexta', 'Sábado' ],
			dayNamesShort : [ 'Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb' ],
			dayNamesMin : [ 'D', 'S', 'T', 'Q', 'Q', 'S', 'S' ],
			weekHeader : 'Semana',
			firstDay : 0,
			isRTL : false,
			showMonthAfterYear : false,
			yearSuffix : '',
			timeOnlyTitle : 'Só Horas',
			timeText : 'Tempo',
			hourText : 'Hora',
			minuteText : 'Minuto',
			secondText : 'Segundo',
			ampm : false,
			month : 'Mês',
			week : 'Semana',
			day : 'Dia',
			allDayText : 'Todo o Dia'
		};
	
//
//	    $('.date-picker')
//			.datepicker(
//					{
//						changeMonth : true,
//						changeYear : true,
//						showButtonPanel : true,
//						dateFormat : 'MM/yy',
//						locales : 'pt_BR',
//						onClose : function(dateText, inst) {
//							$(this).datepicker(
//									'setDate',
//									new Date(inst.selectedYear,
//											inst.selectedMonth, 1));
//						}
//					});
	
});



