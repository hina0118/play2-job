$(function() {
	$(".scheduleBtn").click(function() {
		var id = $(this).attr("job-id");
		$("#schedulingJobId").val(id);
	});
	$(".rescheduleBtn").click(function() {
		var id = $(this).attr("job-id");
		$("#reschedulingJobId").val(id);
	});
	$("#schedulingJob").click(function() {
		var id = $("#schedulingJobId").val();
		var pattern = $("#schedulingPattern").val();
		document.location = jsRoutes.controllers.Job.schedule(id, pattern).url;
	});
	$("#reschedulingJob").click(function() {
		var id = $("#reschedulingJobId").val();
		var pattern = $("#reschedulingPattern").val();
		document.location = jsRoutes.controllers.Job.reschedule(id, pattern).url;
	});
});