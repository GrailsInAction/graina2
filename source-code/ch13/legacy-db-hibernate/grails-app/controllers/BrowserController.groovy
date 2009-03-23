import com.grailsinaction.legacy.db.*

class BrowserController {

    def index = {  redirect(action: 'list') }

	def list = {
		// Pass through to AJAX browser form	
	}
	
	def showDetails = {
		def branch = Branch.findByName(params.id)
		println "Branch is ${branch}"
		
		def writer = new StringWriter()
    	def html = new groovy.xml.MarkupBuilder(writer)
   		
		// Could do all this directly in a render() call but it's harder to debug
		html.div {
				div(id: 'manager') { 
					fieldset {
						legend('Manager Details')
						dl {
							dt('Name:')
							dd(branch.manager.name)
							dt('Rating:')
							dd(branch.manager.managementRating)
						}
					}
				}
				
				div(id: 'sections') { 
					branch.sections.each { section ->
						fieldset {
							legend('Section: ' + section.name)
							dl {
								dt('Start Date:')
								dd(section.start)
								
								dt('Files:')
								dd(section.files.size())
								
								section.files.each { sectToFile ->
									dl(style: 'padding: 1em; border: 1px dotted black') {
										dt('File Name: ')
										dd(sectToFile.file.name)
										dt('Type:')
										dd(sectToFile.file.resourceType.name)
										dt('Created:')
										dd(sectToFile.start)
										dt('Owner:')
										dd(sectToFile.file.owner.name)
									}
								}
								dt('Locations:')
								dd(section.locations.size())
								ul {
									section.locations.each { sectToLoc -> 
										li(sectToLoc.location.name)		
									}
								}	
								
							}
						}
					}
					
				}
				
				
			}
		println writer.toString()
		render(writer.toString())
		
	}
}
