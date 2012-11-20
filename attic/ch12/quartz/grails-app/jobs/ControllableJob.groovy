
class ControllableJob {
	
    def timeout = 5000 // execute job once in 5 seconds
	def concurrent = false

	def group = "myServices"

    def execute() {
        println "Controllable Job running..."
    }
}
