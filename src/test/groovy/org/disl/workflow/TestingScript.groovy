package org.disl.workflow

@groovy.transform.BaseScript(org.disl.workflow.DislScript)
import org.disl.meta.TestDimensionTable

setExitOnFinish(false)

simulate TestDimensionTable
simulate TestingScript