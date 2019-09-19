jQuery.validator.messages.required = '<!-- @RES:C:Required -->';
jQuery.validator.messages.number = '<!-- @RES:C:Number -->';
jQuery.validator.messages.minlength = jQuery.validator.format("<!-- @RES:C:MinLength -->");
jQuery.validator.messages.equalTo = jQuery.validator.format("<!-- @RES:C:EnterSameValue -->");

var DeviceCategoryNames = new Array();
DeviceCategoryNames[1] = "<!-- @RES:C:DeviceCategory1.Heating -->";
DeviceCategoryNames[2] = "<!-- @RES:C:DeviceCategory2.HotWater -->";
DeviceCategoryNames[3] = "<!-- @RES:C:DeviceCategory3.Locks -->";
DeviceCategoryNames[4] = "<!-- @RES:C:DeviceCategory4.Lights -->";
DeviceCategoryNames[6] = "<!-- @RES:C:DeviceCategory6.Ventilation -->";
DeviceCategoryNames[7] = "<!-- @RES:C:DeviceCategory7.Monitoring -->";

var DependencyTypeNames = new Array();
DependencyTypeNames[0] = "<!-- @RES:C:DependencyType0.Room -->";
DependencyTypeNames[1] = "<!-- @RES:C:DependencyType1.Device -->";
DependencyTypeNames[2] = "<!-- @RES:C:DependencyType2.Group -->";
DependencyTypeNames[3] = "<!-- @RES:C:DependencyType3.Condition -->";
DependencyTypeNames[4] = "<!-- @RES:C:DependencyType4.Block -->";
DependencyTypeNames[5] = "<!-- @RES:C:DependencyType5.Mode -->";
DependencyTypeNames[6] = "<!-- @RES:C:DependencyType6.Other -->";
DependencyTypeNames[7] = "<!-- @RES:C:DependencyType7.Zone -->";
DependencyTypeNames[8] = "<!-- @RES:C:DependencyType8.Command -->";

var InactiveStatesNames = new Array();
InactiveStatesNames[0] = "<!-- @RES:C:InactiveState0.NC -->";
InactiveStatesNames[1] = "<!-- @RES:C:InactiveState1.NO -->";

var EqualityOperatorNames = new Array();
EqualityOperatorNames[0] = "<!-- @RES:C:EqualityOperator0.GreaterOrEqual -->";
EqualityOperatorNames[1] = "<!-- @RES:C:EqualityOperator1.Lower -->";

var GroupMatchNames = new Array();
GroupMatchNames[0] = "<!-- @RES:C:GroupMatch0.AllInTheGroup -->";
GroupMatchNames[1] = "<!-- @RES:C:GroupMatch1.OneOfTheGroup -->";

var YesNoNames = new Array();
YesNoNames[0] = "<!-- @RES:C:YesNo0.No -->";
YesNoNames[1] = "<!-- @RES:C:YesNo1.Yes -->";

var DaysNames = new Array();
DaysNames['mo'] = "<!-- @RES:C:Monday -->";
DaysNames[1] = "<!-- @RES:C:Monday -->";
DaysNames['tu'] = "<!-- @RES:C:Tuesday -->";
DaysNames[2] = "<!-- @RES:C:Tuesday -->";
DaysNames['we'] = "<!-- @RES:C:Wednesday -->";
DaysNames[3] = "<!-- @RES:C:Wednesday -->";
DaysNames['th'] = "<!-- @RES:C:Thursday -->";
DaysNames[4] = "<!-- @RES:C:Thursday -->";
DaysNames['fr'] = "<!-- @RES:C:Friday -->";
DaysNames[5] = "<!-- @RES:C:Friday -->";
DaysNames['sa'] = "<!-- @RES:C:Saturday -->";
DaysNames[6] = "<!-- @RES:C:Saturday -->";
DaysNames['su'] = "<!-- @RES:C:Sunday -->";
DaysNames[0] = "<!-- @RES:C:Sunday -->";

var OperationModeNames = new Array();
OperationModeNames[1] = "<!-- @RES:C:OperationMode1.Automatic -->";
OperationModeNames[3] = "<!-- @RES:C:OperationMode3.Diagnostics -->";
OperationModeNames[4] = "<!-- @RES:C:OperationMode4.Configuration -->";
OperationModeNames[5] = "<!-- @RES:C:OperationMode5.Settings -->";

var DigitalActivityNames = new Array();
DigitalActivityNames[true] = "<!-- @RES:C:Active -->";
DigitalActivityNames[false] = "<!-- @RES:C:Inactive -->";

var ValueTypeNames = new Array();
ValueTypeNames[0] = "<!-- @RES:C:Temperature -->";
ValueTypeNames[1] = "<!-- @RES:C:Humidity -->";
ValueTypeNames[2] = "<!-- @RES:C:Luminosity -->";

jQuery.extend(jQuery.mobile.datebox.prototype.options.lang, {
	'invariant': {
        setDateButtonLabel: '<!-- @RES:C:SetDate -->',
        setTimeButtonLabel: '<!-- @RES:C:SetTime -->',
        setDurationButtonLabel: '<!-- @RES:C:SetDuration -->',
        calTodayButtonLabel: '<!-- @RES:C:Today -->',
        titleDateDialogLabel: '<!-- @RES:C:SetDate -->',
        titleTimeDialogLabel: '<!-- @RES:C:SetTime -->',
        daysOfWeek: ['<!-- @RES:C:Sunday -->', '<!-- @RES:C:Monday -->', '<!-- @RES:C:Tuesday -->', '<!-- @RES:C:Wednesday -->', '<!-- @RES:C:Thursday -->', '<!-- @RES:C:Friday -->', '<!-- @RES:C:Saturday -->'],
        daysOfWeekShort: ['<!-- @RES:C:Day.Su -->', '<!-- @RES:C:Day.Mo -->', '<!-- @RES:C:Day.Tu -->', '<!-- @RES:C:Day.We -->', '<!-- @RES:C:Day.Th -->', '<!-- @RES:C:Day.Fr -->', '<!-- @RES:C:Day.Sa -->'],
        monthsOfYear: ['<!-- @RES:C:January -->', '<!-- @RES:C:February -->', '<!-- @RES:C:March -->', '<!-- @RES:C:April -->', '<!-- @RES:C:May -->', '<!-- @RES:C:June -->', '<!-- @RES:C:July -->', '<!-- @RES:C:August -->', '<!-- @RES:C:September -->', '<!-- @RES:C:October -->', '<!-- @RES:C:November -->', '<!-- @RES:C:December -->'],
        monthsOfYearShort: ['<!-- @RES:C:Month.Jan -->', '<!-- @RES:C:Month.Feb -->', '<!-- @RES:C:Month.Mar -->', '<!-- @RES:C:Month.Apr -->', '<!-- @RES:C:Month.May -->', '<!-- @RES:C:Month.Jun -->', '<!-- @RES:C:Month.Jul -->', '<!-- @RES:C:Month.Aug -->', '<!-- @RES:C:Month.Sep -->', '<!-- @RES:C:Month.Oct -->', '<!-- @RES:C:Month.Nov -->', '<!-- @RES:C:Month.Dec -->'],
        durationLabel: ['<!-- @RES:C:Days -->', '<!-- @RES:C:Hours -->', '<!-- @RES:C:Minutes -->', '<!-- @RES:C:Seconds -->'],
        durationDays: ['<!-- @RES:C:Day -->', '<!-- @RES:C:Days -->'],
        timeFormat: 24,
		headerFormat: '%A, %B %-d, %Y',
        dateFieldOrder: ['m', 'd'],
        timeFieldOrder: ['h', 'i', 'a'],
        slideFieldOrder: ['y', 'm', 'd'],
		dateFormat: "%-d %B",
        isRTL: false,
		tooltip: "<!-- @RES:C:OpenDatePicker -->",
		nextMonth: "<!-- @RES:C:NextMonth -->",
		prevMonth: "<!-- @RES:C:PreviousMonth -->",
		useArabicIndic: false,
		calStartDay: 0,
		clearButton: "<!-- @RES:C:Clear -->",
		durationOrder: ['h', 'i', 's'],
		meridiem: ["AM", "PM"],
		timeOutput: "%H:%M",
		durationFormat: "%Dl:%DM:%DS",
		calDateListLabel: "<!-- @RES:C:OtherDates -->"
	}
});

jQuery.extend(jQuery.mobile.datebox.prototype.options, {
	useLang: 'invariant'
});