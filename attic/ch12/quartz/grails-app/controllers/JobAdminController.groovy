class JobAdminController {

	def quartzScheduler
	
    def index = { redirect(action:'show') }

	def show = {
		
		def status = ""
		
		switch(params.operation) {
			case 'pause':
				quartzScheduler.pauseJob("ControllableJob", "myServices")
				status = "Paused Single Job"
				break
			case 'resume':
				quartzScheduler.resumeJob("ControllableJob", "myServices")
				status = "Resumed Single Job"
				break
			case 'pauseGroup':
				quartzScheduler.pauseJobGroup("myServices")
				status = "Paused Job Group"
				break
			case 'resumeGroup':
				quartzScheduler.resumeJobGroup("myServices")
				status = "Resumed Job Group"
				break
			case 'pauseAll':
				quartzScheduler.pauseAll()
				status = "Paused All Jobs"
				break
			case 'resumeAll':
				quartzScheduler.resumeAll()
				status = "Resumed All Jobs"
				break
		}
		
		return [ status: status ]
		
	}
}
