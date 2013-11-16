/*
eventAllTestsStart = {
	runningTests = true

	tryToLoadTestTypes()

	[junit3TestTypeClassName, junit4TestTypeClassName].each { testTypeClassName ->
		def testTypeClass = softLoadClass(testTypeClassName)
		if (testTypeClass) {
			if (!functionalTests.any { it.class == testTypeClass }) {
				functionalTests << testTypeClass.newInstance('functional', 'functional')
			}
		}
	}
}
*/
